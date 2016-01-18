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
public class Player extends Polygon {

    private Vector2 acceleration;
    private float friction = 0f;
    private float restitution = 0f;
    private boolean jumping = false;
    
    float delF;
    
    public float HIGHEST = Float.MIN_VALUE;

    public Player(Vector2[] vertices) {
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
//        System.out.println("COLLLLLLLIDDDEE");
//        System.out.println("Collision depth: " + collisionDepth);
        Vector2 collidingNormal = getNormal(collidingAxis);
        
        collidingNormal.nor().scl(-collisionDepth);
//        System.out.println("Colliding normal vector: " + collidingNormal);
//        System.out.println("colliding normal length (should be same as collision depth): " + collidingNormal.len());
        
        // LOL no
        // magnitude of displacement vector = |collidingNormal|^2|vel|/collidingNormal.vel
        float displacementMag = collidingNormal.cpy().dot(collidingNormal) * velocity.len() / (collidingNormal.cpy().dot(velocity));
//        System.out.println("displacement mag: " + displacementMag);
        Vector2 displacement = velocity.cpy().nor().scl(displacementMag);
//        System.out.println("DISPLACEMENT: " + displacement);
        
        bump(displacement);
        
//        System.out.println("INITIAL VEL: " + velocity + "    DELTAT " + delF);
//        System.out.println("IN THE SQR ROOT" + (velocity.len()*velocity.len()-2*acceleration.len()*displacement.len()));
//        System.out.println("VELOCITY LEN: " + velocity.len() + " VEL SQ " + velocity.len()*velocity.len() + " ACCEL: " + acceleration.len() + " DISPL " + displacement.len());
//        System.out.println("DISPLACED VEL: " + velocity);
        
//        System.out.println("LOL WHAT: " + (velocity.len()*velocity.len()-2*acceleration.len()*displacement.len()));
        
//        velocity.nor().scl((float)Math.sqrt((velocity.len()*velocity.len()-2*acceleration.len()*displacement.len())));
        
//        System.out.println("ATTTEMPTINT TO PROJECT VEL: " + velocity + " COLL AXIS " + collidingAxis + " ->>> " + vectorProject(velocity, collidingAxis));
        // FRICTION:
//        System.out.println("Velocity in penetration: " + velocity);
        
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
        
//        System.out.println("normal velocity: " + normalComponent);
        
        adjustedSpeed = (float)(Math.sqrt(Math.abs(normalComponent.len()*normalComponent.len()-2*vectorProject(acceleration, normal).len()*vectorProject(displacement, normal).len())));
        normalComponent.nor().scl(adjustedSpeed);
        normalComponent.scl(restitution);
        
//        System.out.println("completed normal velocity: "  + normalComponent);
//        System.out.println("RESTITUTION: " + verticalComponent + "    " + vectorProject(velocity, getNormal(collidingAxis)));
        
        velocity = parallelComponent.add(normalComponent);
        
//        System.out.println("completed velocity: " + velocity);
//        System.out.println("NEW VELOCITy: " + velocity);
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

    public boolean collideWithPolygons(ArrayList<Polygon> polygons) {
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
            if (collided && Math.abs(collisionDepth) > 1e-4)
            {
                // TESTING
//                System.out.println("COLLIDED");
//                System.out.println("Collision depth: " + collisionDepth);
//                System.out.println("Rebounding axis: " + movementAxis);

                // Moves the player instantly by the intersection magnitude
                collidePhysics(collidingAxis, collisionDepth);

                return true;
            }
        }
        return false;
    }
}
