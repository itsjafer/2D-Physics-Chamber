/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import static com.mygdx.game.model.Polygon.vectorProject;
import java.util.ArrayList;

/**
 *
 * @author branc2347
 */
public class Player extends Polygon {

    public static final float RUN_SPEED = 70f;
    public static final float JUMP_DISTANCE = 100f;
    
    private Vector2 acceleration;
    private float friction = 0f;
    private float restitution = 1f;
    
    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
    private boolean onGround = false;
    float collisionDepth;
    Vector2 collisionAxis;
    boolean collided;
    Vector2 velocity;
    Vector2 center;
    Vector2 startPos;
    Vector2 totalHeight = new Vector2(0, 0);
    float totalTime = 0f;
    private float rotationSpeed;
    private float rotation;

    public Player(Vector2[] vertices, Color colour) {
        super(vertices, colour);
        velocity = new Vector2();
        acceleration = new Vector2();

        center = new Vector2();
        updateCenter();
        startPos = center.cpy();

        collisionAxis = new Vector2();
        collisionDepth = 0f;
        collided = false;

        rotationSpeed = 0f;
        rotation = 0f;

        onGround = false;
    }

    public void move(float deltaTime) {
        
        onGround = false;
        totalTime += deltaTime;

        Vector2 movement = velocity.cpy().scl(deltaTime).add(acceleration.cpy().scl(0.5f * deltaTime * deltaTime));
        totalHeight.add(movement);
        velocity.add(acceleration.cpy().scl(deltaTime));
//         Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(movement);
        }
        updateCenter();
        rotate(deltaTime);
    }

    public Vector2 getCenter() {
        return center;
    }

    public boolean onGround() {
        return onGround;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void rotate(float deltaTime) {
        for (Vector2 vertex : vertices) {
            float existingAngle = (float) Math.atan2(vertex.y - center.y, vertex.x - center.x);
            float radius = (vertex.cpy().sub(center)).len();

            float newX = center.x + radius * (float) Math.cos(existingAngle + rotationSpeed * deltaTime);
            float newY = center.y + radius * (float) Math.sin(existingAngle + rotationSpeed * deltaTime);

            vertex.set(newX, newY);

        }
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

    public void collidePhysics() {
        if (scalarProject(velocity, MyGdxGame.WORLD.getGravity()) > 0) {
            System.out.println("CAN JUMP");
            onGround = true;
        }

        Vector2 displacement = getNormal(collisionAxis).nor().scl(-collisionDepth);
        bump(displacement);

//        rotationSpeed += (float)Math.toRadians(displacement.angle());
        if (!Float.isNaN(displacement.len() * (float) Math.atan2(displacement.y, displacement.x) * Math.signum((float) Math.cos(displacement.y / displacement.x)))) {
            rotationSpeed += displacement.len() * (float) Math.atan2(displacement.y, displacement.x) * Math.signum(Math.cos(displacement.y / displacement.x) * restitution);
            rotationSpeed *= -1;
        }
    }

    /**
     * Resets the player's position to the initial creation position. Also
     * resets momentum
     */
    public void reset() {
        bump(startPos.cpy().sub(center));
        updateCenter();
        velocity.set(new Vector2(0, 0));
        System.out.println(rotation);
        rotationSpeed = -rotation;
        rotate(1);
        rotation = 0;
        rotationSpeed = 0;
    }

    public void update() {
        acceleration.set(Vector2.Zero);
    }

    public void applyAcceleration(Vector2 acceleration) {
        this.acceleration.add(acceleration);
    }

    public void updateCollisionStatus(ArrayList<Polygon> polygons) {
        collided = false;
        // The player's normals
        Vector2[] normals1 = getNormals();
        // The other polygon's normals
        Vector2[] normals2;
        // The player's projection
        Vector2 projection1;
        // The other polygon's projection
        Vector2 projection2;

        float collisionDepthLocal = Float.MAX_VALUE;
        Vector2 collisionAxisLocal = Vector2.Zero;

        // Iterate through all the polygons and check for a collision on a 1 to 1 basis
        for (Polygon otherPoly : polygons) {
            collided = true;
            // Check all of the player's normals
            for (Vector2 normal : normals1) {
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);

                // If one normal doesn't contain a collision, then no collision occurs at all
                if (projection1.x >= projection2.y || projection1.y <= projection2.x) {
                    collided = false;
                    break;
                    // Execution below this point indicates an intersection has occured
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
                    collisionAxisLocal = getNormal(normal);
                }
            }

            // Check all of the other polygon's normals
            normals2 = otherPoly.getNormals();
            for (Vector2 normal : normals2) {
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);

                // If one normal doesn't contain a collision, then no collision occurs at all
                if (projection1.x >= projection2.y || projection1.y <= projection2.x) {
                    collided = false;
                    break;
                    // Execution below this point indicates an intersection has occured
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
                    collisionAxisLocal = getNormal(normal);
                }
            }
            if (collided) {
                collisionDepth = collisionDepthLocal;
                collisionAxis = collisionAxisLocal;
                return;
            }
        }

    }

    public void collideWithPolygons(ArrayList<Polygon> polygons) {
        updateCollisionStatus(polygons);
        if (collided) {
            int safety = 0;
            while (collided) {
                collidePhysics();
                updateCollisionStatus(polygons);
                safety++;
                if (safety > 10) {
                    break;
                }
            }
            Vector2 parallelComponent = vectorProject(velocity, collisionAxis);
            parallelComponent.scl(1f - friction);

            Vector2 normalComponent = vectorProject(velocity, getNormal(collisionAxis));
            normalComponent.scl(-restitution);

            velocity = parallelComponent.add(normalComponent);
        }
    }
    
    public float runningSpeed(Vector2 horizontalMovementAxis)
    {
        return scalarProject(velocity, horizontalMovementAxis);
    }
    public Vector2 runningVelocity(Vector2 horizontalMovementAxis)
    {
        return vectorProject(velocity, horizontalMovementAxis);
    }
    
    public Vector2 accelerationToVelocity(Vector2 movementAxis, Vector2 desiredMovementVelocity)
    {
        return desiredMovementVelocity.sub(vectorProject(velocity, movementAxis)).scl(1f/Gdx.graphics.getDeltaTime());
    }
}
