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
    private float restitution = 1f;
    private boolean jumping = false;
    
    float collisionDepth;
    Vector2 collisionAxis;
    boolean collided;
    Vector2 movement;
    
    Vector2 velocity;
    Vector2 center;
    Vector2 startPos;
    
    public Player(Vector2[] vertices) {
        super(vertices);
        velocity = new Vector2();
        acceleration = new Vector2();
        
        center = new Vector2();
        updateCenter();
        startPos = center.cpy();
        
        collisionAxis = new Vector2();
        collisionDepth = 0f;
        collided = false;
        movement = new Vector2();
    }

    public void jump() {
        jumping = true;
    }

    public void move(float deltaTime) {
        
        
        movement = velocity.cpy().scl(deltaTime).add(acceleration.cpy().scl(0.5f * deltaTime * deltaTime));
        velocity.add(acceleration.cpy().scl(deltaTime));

//         Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(movement);
        }
        updateCenter();
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
    
    public void collidePhysics()
    {
        Vector2 displacement = getNormal(collisionAxis).nor().scl(-collisionDepth);
        bump(displacement);
    }
    
    /**
     * Resets the player's position to the initial creation position. Also
     * resets momentum
     */
    public void reset()
    {
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

    public void updateCollisionStatus(ArrayList<Polygon> polygons)
    {
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
            if (collided)
            {
                collisionDepth = collisionDepthLocal;
                collisionAxis = collisionAxisLocal;
                return;
            }
        }
        
    }
    
    public void collideWithPolygons(ArrayList<Polygon> polygons) {
        updateCollisionStatus(polygons);
        if (collided)
        {
            int safety = 0;
            while (collided)
            {
                collidePhysics();
                updateCollisionStatus(polygons);
                safety ++;
                if (safety > 10)
                {
                    break;
                }
            }
            Vector2 parallelComponent = vectorProject(velocity, collisionAxis);
            parallelComponent.scl(1f-friction);

            Vector2 normalComponent = vectorProject(velocity, getNormal(collisionAxis));
            normalComponent.scl(-restitution);
            
            velocity = parallelComponent.add(normalComponent);
            
            System.out.println("new velocity: " +velocity);
        }
    }
}
