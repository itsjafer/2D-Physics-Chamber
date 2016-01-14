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

    public Player(Vector2[] vertices, float friction) {
        super(vertices, friction);
        velocity = new Vector2();
    }
    
    public void move(float deltaTime)
    {
        // Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(velocity.cpy().scl(deltaTime));
        }
        updateCenter();
    }
    
    /**
     * Instantly moves the polygon to a new position by a fixed amount
     * @param deltaTime the time it took to render the last frame
     * @param displacement the amount to move the player and the direction
     */
    public void bump(Vector2 displacement)
    {
        for (Vector2 vertex: vertices)
        {
            vertex.add(displacement);
        }
    }
    
    public void setVelocity(Vector2 velocity)
    {
        this.velocity.add(velocity);
    }
    public void collideWithPolygons(ArrayList<Polygon> polygons)
    {
        // The player's normals
        Vector2[] normals1 = getNormals();
        // The other polygon's normals
        Vector2[] normals2;
        // The player's projection
        Vector2 projection1;
        // The other polygon's projection
        Vector2 projection2;
        
        float collisionDepth = Float.MAX_VALUE;
        Vector2 movementAxis = null;
        
        // Iterate through all the polygons and check for a collision on a 1 to 1 basis
        for (Polygon otherPoly: polygons)
        {
            boolean collided = true;
            // Check all of the player's normals
            for (Vector2 normal: normals1)
            {
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);
                
                // If one normal doesn't contain a collision, then no collision occurs at all
                if (projection1.x > projection2.y || projection1.y < projection2.x)
                {
                    collided = false;
                    break;
                    // Execution below this point indicates an intersection has occured
                }
                // the intersection depth for the current intersection
                float intersection;
                
                // Check the two possible intersection lengths and pick the smaller one
                if (Math.abs(projection2.y-projection1.x) < Math.abs(projection2.x-projection1.y))
                {
                    intersection = projection2.y-projection1.x;
                }
                else
                {
                    intersection = projection2.x-projection1.y;
                }
                
                // If the current intersection depth is smaller than the overall intersection depth, udpate the overall intersection depth and the overall intersection axis
                if (Math.abs(intersection) < Math.abs(collisionDepth))
                {
                    collisionDepth = intersection;
                    movementAxis = normal;
                }
            }   
            
            // Check all of the other polygon's normals
            normals2 = otherPoly.getNormals();
            for (Vector2 normal: normals2)
            {
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);
                
                // If one normal doesn't contain a collision, then no collision occurs at all
                if (projection1.x > projection2.y || projection1.y < projection2.x)
                {
                    collided = false;
                    break;
                    // Execution below this point indicates an intersection has occured
                }
                // the intersection depth for the current intersection
                float intersection;
                
                // Check the two possible intersection lengths and pick the smaller one
                if (Math.abs(projection2.y-projection1.x) < Math.abs(projection2.x-projection1.y))
                {
                    intersection = projection2.y-projection1.x;
                }
                else
                {
                    intersection = projection2.x-projection1.y;
                }
                
                // If the current intersection depth is smaller than the overall intersection depth, udpate the overall intersection depth and the overall intersection axis
                if (Math.abs(intersection) < Math.abs(collisionDepth))
                {
                    collisionDepth = intersection;
                    movementAxis = normal;
                }
            }
            if (collided)
            {
                // TESTING
//                System.out.println("COLLIDED");
//                System.out.println("Collision depth: " + collisionDepth);
//                System.out.println("Rebounding axis: " + movementAxis);
                
                // Moves the player instantly by the intersection magnitude
                    // Get the unit vector of movement axis
                movementAxis.scl(1f/movementAxis.len());
                    // Multiply that unit vector by the displacement depth
                movementAxis.scl(collisionDepth);
                    // Apply this displacement vector to the player's current position
                bump(movementAxis);
            }
        }
    }
}
