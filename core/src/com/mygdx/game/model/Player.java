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
        velocity = new Vector2(0, -5);
    }
    
    public void move(float deltaTime)
    {
        // Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(velocity.cpy().scl(deltaTime));
        }
        updateCenter();
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
                }
            }
            if (collided)
                System.out.println("COLLIDED");
        }
    }
    
}
