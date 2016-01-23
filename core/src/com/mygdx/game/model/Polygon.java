
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * A custom polygon object, to be used as the base object in the world
 * @author Dmitry, Jafer, Caius
 */
public class Polygon {

    // All of the vertices of the polygon
    protected Vector2[] vertices;
    // The polygon's colour
    protected Color polygonColour;

    /**
     * Creates a polygon.
     * @param vertices the vertices defining the shape
     * @param colour the color of the polygon
     */
    public Polygon(Vector2[] vertices, Color colour) {

        // Initializes position info
        this.vertices = vertices;
        //get the colour set from the game by the game menu 
        this.polygonColour = colour;
    }

    /**
     * @return the polygon's color
     */
    public Color getColor() {
        return polygonColour;
    }

    /**
     * @return the vertices of the polygon
     */
    public Vector2[] getVertices() {
        return vertices;
    }

    /**
     * Project the polygon onto an axis
     * @param axis the axis onto which the polygon is to be projected.
     * @return the polygon's projection onto the axis.
     */
    public Vector2 projectPolygon(Vector2 axis) {
        // set the minimum projection length to the first vertex
        float min = vertices[0].dot(axis);
        // the max will be the min for now
        float max = min;
        // At first, only the dot products will be compared and stored
        float dot = 0f;
        // Start at the second vertex since the first one was already checked
        for (int i = 1; i < vertices.length; i++) {
            dot = vertices[i].dot(axis);
            // Adjust max/min
            if (dot < min) {
                min = dot;
            }
            if (dot > max) {
                max = dot;
            }
        }

        // Now that the max and min dot product have been calculated, they can be converted into vector projections, thereby providing the bounds of the polygon's projection the given axis
        return new Vector2(min / axis.len(), max / axis.len());
    }

    /**
     * @return an array of normals for each axis on the polygon
     */
    public Vector2[] getNormals() {
        Vector2[] normals = new Vector2[vertices.length];
        Vector2 axis = null;
        // Loop through all the vertices and wrap around for the edges. Then store the enormal of each edge
        for (int i = 0; i < vertices.length; i++) {
            // getting an edge between two vertices
            axis = vertices[i].cpy().sub(vertices[i + 1 == vertices.length ? 0 : i + 1]);
            // the normal is the negative reciprocal of the slope
            normals[i] = VectorMath.getNormal(axis);
        }
        return normals;
    }

    /**
     * Checks whether or not a point is contained within the polygon
     * @param point the point to be checked
     * @return true of the point is contained; otherwise, return false
     */
    public boolean containsPoint(Vector2 point) {
        // The normals to each edge
        Vector2[] normals = getNormals();

        // The projection of the polygon
        Vector2 polyProjection;
        // The "projection" of the point
        float pointProjection;

        // Loop through all the normals and project onto them
        for (Vector2 normal : normals) {
            polyProjection = projectPolygon(normal);
            pointProjection = VectorMath.scalarProject(point, normal);

            // if there is no overlap on this normal, return false since there is no overlap anywhere
            if (polyProjection.x > pointProjection || polyProjection.y < pointProjection) {
                return false;
            }
        }
        // at this point there is a definite overlap
        return true;
    }

    /**
     * Instantly moves the polygon by a fixed amount
     * @param displacement the displacement vector
     */
    public void bump(Vector2 displacement) {
        // move all the vertices by the same displacement
        for (Vector2 vertex : vertices) {
            vertex.add(displacement);
        }
    }
    
}
