/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author branc2347
 */
public class Player extends Polygon {

    private Vector2 acceleration;
    private float friction = 0.1f;
    private float restitution = 1f;
    private boolean jumping = false;

    public Player(Vector2[] vertices) {
        super(vertices);
        velocity = new Vector2();
        acceleration = new Vector2();
    }

    public void jump() {
        jumping = true;
    }

    public void move(float deltaTime) {
        if (jumping) {
            velocity.y = 200f;
        }
//        Vector2 movement = velocity.cpy().add(acceleration.cpy().scl(0.5f));
        Vector2 movement = velocity.cpy().scl(deltaTime).add(acceleration.cpy().scl(0.5f * deltaTime * deltaTime));
        velocity.add(acceleration);

//         Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(movement);
        }
        updateCenter();
    }
    
    public void collidePhysics(Vector2 collidingAxis, float collisionDepth)
    {
//        bump(new Vector2(0, 10000));
        
        System.out.println("Col depth: " + collisionDepth);
//        System.out.println("VELOCITY PENETRATED: " + velocity);
//        
//        float velBeforePenetration = (float)Math.sqrt(velocity.len()*velocity.len() - 2f*acceleration.len()*(Math.abs(collisionDepth)));
//        velocity.scl(1f/velocity.len());
//        velocity.scl(velBeforePenetration);
//        
//        System.out.println("ADJUSTED VELOCITY FOR OUT OF PENETRATION (" + collisionDepth + "): " + velocity);
//        
        Vector2 displacement = getNormal(collidingAxis).scl(1f/collidingAxis.len()).scl(-collisionDepth);
        System.out.println("coll Axis: " + collidingAxis);
        System.out.println("normal:  " + getNormal(collidingAxis));
        System.out.println("Unit vector: " + getNormal(collidingAxis).scl(1f/collidingAxis.len()));
        System.out.println("Collision deptH: " + collisionDepth);
        System.out.println("Displacement vector: " + getNormal(collidingAxis).scl(1f/collidingAxis.len()).scl(collisionDepth));
        bump(new Vector2(0, 111));
//        bump(displacement);
//        System.out.println("WOW: " + (displacement.len()-collisionDepth));
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

    public void collideWithPolygons(ArrayList<Polygon> polygons) {
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
                if (projection1.x > projection2.y || projection1.y < projection2.x) {
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
                if (projection1.x > projection2.y || projection1.y < projection2.x) {
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
            if (collided) {
                // TESTING
//                System.out.println("COLLIDED");
//                System.out.println("Collision depth: " + collisionDepth);
//                System.out.println("Rebounding axis: " + movementAxis);

                // Moves the player instantly by the intersection magnitude
                collidePhysics(collidingAxis, collisionDepth);

                return;
            }
        }
    }
}
