/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;

/**
 * The player
 *
 * @author Dmitry, Jafer, Caius
 */
public class Player extends Polygon {

    // MOVEMENT STUFF
    public static final float RUN_SPEED = 150f;
    public static final float JUMP_DISTANCE = 100f;

    private Vector2 acceleration;
    private Vector2 velocity;
    private Vector2 center;
    // the player's spawn position
    private Vector2 startPos;

    private boolean onGround;

    private boolean doRotate;
    private float rotation;
    private float rotationSpeed;

    private boolean collided;
    private float collisionDepth;
    private Vector2 collisionAxis;

    private final GameWorld world = MyGdxGame.WORLD;

    /**
     * Creates the player
     *
     * @param vertices the list of vertices that define the player's polygon
     * @param colour the player's colour
     */
    public Player(Vector2[] vertices, Color colour) {
        super(vertices, colour);

        velocity = new Vector2();
        acceleration = new Vector2();
        center = new Vector2();

        collisionAxis = new Vector2();

        init();
    }

    /**
     * Set defaults
     */
    private void init() {
        velocity.set(0, 0);
        acceleration.set(0, 0);
        updateCenter();
        // the player starts at this position
        startPos = center.cpy();

        collided = false;
        collisionAxis.set(0, 0);
        collisionDepth = 0f;

        rotation = 0f;
        rotationSpeed = 0f;

        onGround = false;
    }

    /**
     * Reset to defaults
     */
    public void reset() {
        // Undo rotation (rotate opposite direction for 1 second)
        rotationSpeed = -rotation;
        rotate(1);
        // Bring the player back to the start
        bump(startPos.cpy().sub(center));
        // set the rest of the defaults
        init();
    }

    /**
     * Move the player
     *
     * @param deltaTime the time to rotate for
     */
    public void move(float deltaTime) {
        // d = vi*t + 1/2*a*t^2
        Vector2 movement = velocity.cpy().scl(deltaTime).add(acceleration.cpy().scl(0.5f * deltaTime * deltaTime));
        // vf = vi + at
        velocity.add(acceleration.cpy().scl(deltaTime));

        // Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(movement);
        }
        updateCenter();

        // Only rotate the player if rotation is turned on
        if (doRotate) {
            rotate(deltaTime);
        }

        // Assume the player isn't on ground anymore
        onGround = false;
    }

    /**
     * @return the player's center
     */
    public Vector2 getCenter() {
        return center;
    }

    /**
     * @return whether or not the player is on ground
     */
    public boolean onGround() {
        return onGround;
    }

    /**
     * @return the player's velocity
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Set the player's velocity
     *
     * @param velocity
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Rotates the player
     *
     * @param deltaTime the time to rotate for
     */
    public void rotate(float deltaTime) {
        // rotate each vertex about the center
        for (Vector2 vertex : vertices) {
            // get the current angle between this vertex and the center
            float existingAngle = (float) Math.atan2(vertex.y - center.y, vertex.x - center.x);
            // the distance between this vertex and the center
            float radius = (vertex.cpy().sub(center)).len();
            // apply rotation
            float newX = center.x + radius * (float) Math.cos(existingAngle + rotationSpeed * deltaTime);
            float newY = center.y + radius * (float) Math.sin(existingAngle + rotationSpeed * deltaTime);

            vertex.set(newX, newY);
        }
        // the total rotation has now increased
        rotation += rotationSpeed * deltaTime;
    }

    /**
     * Recalculates the polygon's center
     */
    private void updateCenter() {
        if (center == null) {
            center = new Vector2();
        }

        // The center is the average of the x and y coordinates of all the vertices
        float x = 0, y = 0;
        for (Vector2 vertex : vertices) {
            x += vertex.x;
            y += vertex.y;
        }
        x /= vertices.length;
        y /= vertices.length;

        center.set(x, y);
    }

    /**
     * Apply collision to the player
     */
    public void doCollision() {
        // If the player collides while going downwards, he can jump
        // This obviously lets the player walljump
        if (VectorMath.scalarProject(velocity, world.getVerticalMovementAxis()) < 0) {
            onGround = true;
        }
        // the minimum translation vector (amount player must be moved to be out of collision)
        Vector2 mtv = VectorMath.getNormal(collisionAxis).nor().scl(-collisionDepth);
        // move the player out of collision
        bump(mtv);

        // some test rotation code... it's just rotating the player by the mtv... completely incorrect and poorly written
        if (doRotate) {
            if (!Float.isNaN(mtv.len() * (float) Math.atan2(mtv.y, mtv.x) * Math.signum((float) Math.cos(mtv.y / mtv.x)))) {
                rotationSpeed += mtv.len() * (float) Math.atan2(mtv.y, mtv.x) * Math.signum(Math.cos(mtv.y / mtv.x) * world.getRestitution());
                rotationSpeed *= -1;
            }
        }
    }

    /**
     * Clears the player's acceleration. Used to maintain newton's first law of
     * motion (only applied forces change a body's velocity)
     */
    public void clearAcceleration() {
        acceleration.set(Vector2.Zero);
    }

    /**
     * Add acceleration to the player's current acceleration
     *
     * @param acceleration the ammount to add
     */
    public void applyAcceleration(Vector2 acceleration) {
        this.acceleration.add(acceleration);
    }

    /**
     *
     * @param polygons
     */
    public void checkForCollisions(ArrayList<Polygon> polygons) {
        // assume the player hasn't collided
        collided = false;
        // make sure polygons isn't empty
        if (polygons.isEmpty()) {
            return;
        }

        // The player's normals
        Vector2[] normals1 = getNormals();
        // The other polygon's normals
        Vector2[] normals2;
        // The player's projection
        Vector2 projection1;
        // The other polygon's projection
        Vector2 projection2;

        // the collision depth is maxed out and is to be reduced throughout the checks
        float collisionDepthLocal = Float.MAX_VALUE;
        // the collision axis is zeroed and is to be set through the checks
        Vector2 collisionAxisLocal = Vector2.Zero;

        // Iterate through all the polygons and check for a collision on a 1 to 1 basis
        for (Polygon otherPoly : polygons) {
            // assume collided is true until a collision is not found
            collided = true;
            // Check all of the player's normals
            for (Vector2 normal : normals1) {
                // project player and other polygon onto normal
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);

                // If one normal doesn't contain a collision, then no collision occurs at all for this polygon
                if (projection1.x >= projection2.y || projection1.y <= projection2.x) {
                    collided = false;
                    break;
                }

                // the intersection depth for the current intersection
                float intersection;

                // Check the two possible intersection lengths and pick the smaller one
                if (Math.abs(projection2.y - projection1.x) < Math.abs(projection2.x - projection1.y)) {
                    intersection = projection2.y - projection1.x;
                } else {
                    intersection = projection2.x - projection1.y;
                }

                // If the current intersection depth is smaller than the overall intersection depth, udpate the overall intersection depth and the overall intersection axis
                if (Math.abs(intersection) < Math.abs(collisionDepthLocal)) {
                    collisionDepthLocal = intersection;
                    collisionAxisLocal = VectorMath.getNormal(normal);
                }
            }

            normals2 = otherPoly.getNormals();
            // Check all of the polygon's normals
            for (Vector2 normal : normals2) {
                // project player and other polygon onto normal
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);

                // If one normal doesn't contain a collision, then no collision occurs at all for this polygon
                if (projection1.x >= projection2.y || projection1.y <= projection2.x) {
                    collided = false;
                    break;
                }

                // the intersection depth for the current intersection
                float intersection;

                // Check the two possible intersection lengths and pick the smaller one
                if (Math.abs(projection2.y - projection1.x) < Math.abs(projection2.x - projection1.y)) {
                    intersection = projection2.y - projection1.x;
                } else {
                    intersection = projection2.x - projection1.y;
                }

                // If the current intersection depth is smaller than the overall intersection depth, udpate the overall intersection depth and the overall intersection axis
                if (Math.abs(intersection) < Math.abs(collisionDepthLocal)) {
                    collisionDepthLocal = intersection;
                    collisionAxisLocal = VectorMath.getNormal(normal);
                }
            }

            // stop at the first collision
            if (collided) {
                // update global collision depth & axis
                collisionDepth = collisionDepthLocal;
                collisionAxis = collisionAxisLocal;
                return;
            }
        }
    }

    public void collideWithPolygons(ArrayList<Polygon> polygons) {
        checkForCollisions(polygons);

        if (collided) {
            // Run the collision loop 10 times just to be safe that the polygon isn't colliding with anything (since the player could be colliding with multiple at once)
            // A way to improve this would be to store a list of all the polygons currently in collision with  the player, and then run this loop while that list is not empty
            // in this loop, the checks will be made for these polygons, as well as the rest in the world (since moving the player out of collision can cause other collisions)
            int numChecks = 0;
            while (collided && numChecks < 10) {
                doCollision();
                checkForCollisions(polygons);
                numChecks++;
            }

            ///// Apply movement transformations on the basis of the last polygon the player has collided with
            // The component of velocity parallel to the collision axis
            Vector2 parallelComponent = VectorMath.vectorProject(velocity, collisionAxis);
            // friction 0 means 100% conservation of momentum
            parallelComponent.scl(1f - world.getFriction());

            // The component of velocity normal to the collision axis
            Vector2 normalComponent = VectorMath.vectorProject(velocity, VectorMath.getNormal(collisionAxis));
            // the negative is needed for a bounce
            normalComponent.scl(-world.getRestitution());

            // the new velocity is the sum of both transformed components
            velocity = parallelComponent.add(normalComponent);
        }
    }

    /**
     * @param movementAxis the axis onto which the velocity is to be projected
     * @return the magnitude of the player's velocity projected onto the
     * movement axis
     */
    public float getRunningSpeed(Vector2 movementAxis) {
        return VectorMath.scalarProject(velocity, movementAxis);
    }

    /**
     * @param movementAxis the axis onto which the velocity is to be projected
     * @return the player's velocity projected onto the movement axis
     */
    public Vector2 getRunningVelocity(Vector2 movementAxis) {
        return VectorMath.vectorProject(velocity, movementAxis);
    }

    /**
     * @param movementAxis the direction of the movement
     * @param desiredMovementVelocity
     * @param time
     * @return the necessary acceleration to set the player's velocity to the
     * desired velocity in the desired direction, in the desired time
     */
    public Vector2 getInstantaneousAcceleration(Vector2 movementAxis, Vector2 desiredMovementVelocity, float time) {
        return desiredMovementVelocity.sub(VectorMath.vectorProject(velocity, movementAxis)).scl(1f / time);
    }

    void setRotate(boolean doRotate) {
        this.doRotate = doRotate;
    }
}
