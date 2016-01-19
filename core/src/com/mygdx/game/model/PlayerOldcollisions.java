/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.model.Polygon.vectorProject;
import java.util.ArrayList;

/**
 *
 * @author branc2347
 */
public class PlayerOldcollisions extends Polygon {

    private Vector2 acceleration;
    private float friction = 0f;
    private float restitution = 0f;
    private boolean jumping = false;
    
    float delF;
    
    public float HIGHEST = Float.MIN_VALUE;

    public PlayerOldcollisions(Vector2[] vertices) {
        super(vertices);
        velocity = new Vector2();
        acceleration = new Vector2();
        
        for (Vector2 vertex: vertices)
        {
            if (vertex.y > HIGHEST)
            {
                HIGHEST = vertex.y;
            }
        }
    }

    public void jump() {
        jumping = true;
    }

    public void move(float deltaTime) {
        
//        if (Float.isNaN(velocity.x))
//            velocity.x = 0;
//        if (Float.isNaN(velocity.y))
//            velocity.y = 0;
        
        delF += deltaTime;
        if (jumping) {
            velocity.y = 200f;
        }
//        Vector2 movement = velocity.cpy().add(acceleration.cpy().scl(0.5f));
        Vector2 movement = velocity.cpy().scl(deltaTime).add(acceleration.cpy().scl(0.5f * deltaTime * deltaTime));
        velocity.add(acceleration.cpy().scl(deltaTime));

//         Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(movement);
        }
        updateCenter();
    }
    
    public void collidePhysics(Vector2 collidingAxis, float collisionDepth)
    {
        System.out.println("VELOCITY WITH WHICH IT COLLIDED!!: " + velocity);
        
        System.out.println("Collision depth: " + collisionDepth);
        Vector2 collidingNormal = getNormal(collidingAxis);
        System.out.println("Colliding normal: " + collidingNormal);
        
        collidingNormal.nor().scl(-collisionDepth);
        System.out.println("Colliding normal vector: " + collidingNormal);
        System.out.println("Dot: " + collidingNormal.cpy().dot(velocity));
        
        if (velocity.x == 0 && velocity.y == 0 || collidingNormal.cpy().dot(velocity) == 0)
        {
            bump(collidingNormal);
            return;
        }
        // magnitude of displacement vector = |collidingNormal|^2|vel|/collidingNormal.vel
        float displacementMag = collidingNormal.cpy().dot(collidingNormal) * velocity.len() / (collidingNormal.cpy().dot(velocity));
        System.out.println("Displacement Mag: " + displacementMag);
        Vector2 displacement = velocity.cpy().nor().scl(displacementMag);
        System.out.println("DISPLACEMENT: " + displacement);
        
        bump(displacement);
        
        
        float adjustedSpeed = 0f;
        Vector2 parallelComponent = vectorProject(velocity, collidingAxis);
        
//        System.out.println("parallel velocity: " + parallelComponent);
        
        adjustedSpeed = (float)(Math.sqrt(Math.abs(parallelComponent.len()*parallelComponent.len()-2*vectorProject(acceleration, collidingAxis).len()*vectorProject(displacement, collidingAxis).len())));
        parallelComponent.nor().scl(adjustedSpeed);
        parallelComponent.scl(1f-friction);
//        System.out.println(horizontalComponent);
        
//        System.out.println("completed parallel velocity: " + parallelComponent);
        
//        System.out.println("FRICTION: " + horizontalComponent + "   " + vectorProject(velocity, collidingAxis));
        
        // RESTITUTION:
        Vector2 normal = getNormal(collidingAxis);
        Vector2 normalComponent = vectorProject(velocity, normal);
        System.out.println("Normal component: " + normalComponent);
        
//        System.out.println("normal velocity: " + normalComponent);
        
        adjustedSpeed = (float)(Math.sqrt(Math.abs(normalComponent.len()*normalComponent.len()-2*vectorProject(acceleration, normal).len()*vectorProject(displacement, normal).len())));
        normalComponent.nor().scl(adjustedSpeed);
        normalComponent.scl(-restitution);
//        System.out.println("completed normal velocity: "  + normalComponent);
//        System.out.println("RESTITUTION: " + verticalComponent + "    " + vectorProject(velocity, getNormal(collidingAxis)));
        
        velocity = parallelComponent.add(normalComponent);
        
        System.out.println("NEW VELOCITy: " + velocity);
    }
    /**
     * Resets the player's position to the initial creation position. Also
     * resets momentum
     */
    public void goHome() {
        bump(startPos.cpy().sub(center));
        velocity.set(new Vector2(0, 0));
    }

    /**
     * Instantly moves the polygon to a new position by a fixed amount
     *
     * @param displacement the amount to move the player and the direction
     */
    public void bump(Vector2 displacement) {
        for (Vector2 vertex : vertices) {
            vertex.add(displacement);
        }
    }

    public void update() {
        acceleration.set(Vector2.Zero);
        jumping = false;
    }

    public void applyAcceleration(Vector2 acceleration) {
        this.acceleration.add(acceleration);
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.add(velocity);
    }

    public Vector2 collideWithPolygons(ArrayList<Polygon> polygons, Vector2 prev) {
        // The player's normals
        Vector2[] normals1 = getNormals();
        // The other polygon's normals
        Vector2[] normals2;
        // The player's projection
        Vector2 projection1;
        // The other polygon's projection
        Vector2 projection2;

        float collisionDepth = Float.MAX_VALUE;
        Vector2 collidingAxis = null;

        // Iterate through all the polygons and check for a collision on a 1 to 1 basis
        for (Polygon otherPoly : polygons) {
            boolean collided = true;
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
                if (Math.abs(intersection) < Math.abs(collisionDepth)) {
                    collisionDepth = intersection;
                    collidingAxis = getNormal(normal);
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
                if (Math.abs(intersection) < Math.abs(collisionDepth)) {
                    collisionDepth = intersection;
                    collidingAxis = getNormal(normal);
                }
            }
            if (collided && !Float.isInfinite(collisionDepth) && collidingAxis != null)
            {
                if (collidingAxis.x == prev.x && collidingAxis.y == prev.y)
                    return prev;
                collidePhysics(collidingAxis, collisionDepth);
            }
        }
        return collidingAxis;
    }
}
