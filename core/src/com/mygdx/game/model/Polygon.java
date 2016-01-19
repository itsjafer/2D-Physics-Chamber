
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;

/**
 * For now lets not extends shape cause I'm not exactly sure how that's going to
 * be structured ^^ still need to fool around with circles before we can decide
 *
 * @author Dmitry
 */
public class Polygon {

    // All of the vertices of the polygon
    protected Vector2[] vertices;
    // the polygon's current velocity
    protected Vector2 velocity;
    // The polygon's center
    protected Vector2 center;

    // The polygon's center when its position was last saved
    protected Vector2 startPos = null;

    /**
     * Creates a polygon.
     *
     * @param vertices an array of Vector2.
     * @param friction NOT USED ATM
     */
    public Polygon(Vector2[] vertices) {

        // Initializes position info
        this.vertices = vertices;
        updateCenter();
        // The Polygon's startPos is initially set as its spawn location
        savePosition();

        // The polygon starts stationary
        velocity = new Vector2(0, 0);
    }

    /**
     * Moves the polygon in the direction of its velocity
     */
    public void move() {

        // Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(velocity);
        }
        updateCenter();
    }

    /**
     * Updates the polygon's startPos to its center
     */
    public void savePosition() {
        startPos = center.cpy();
    }

    /**
     * Recalculates the polygon's center
     */
    public void updateCenter() {
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

    public Vector2[] getVertices() {
        return vertices;
    }

    /**
     * WE SHOULD CONSIDER MOVING THIS OUT OF THE CLASS
     *
     * @param v1 the vector to be projected
     * @param v2 the vector being projected on
     * @return the scalar projection of v1 onto v2
     */
    public static float scalarProject(Vector2 v1, Vector2 v2) {

        float dotProduct = v1.cpy().dot(v2);
        float scalarProjection = dotProduct / v2.len();

        return scalarProjection;
    }

    public static Vector2 vectorProject(Vector2 v1, Vector2 v2) {

        float scalarProjection = scalarProject(v1, v2);
        Vector2 vectorProjection = v2.cpy().nor().scl(scalarProjection);

        return vectorProjection;
    }

    /**
     * CONSIDER MOVING THIS OUT OF THE CLASS
     *
     * @param axis the Axis onto which the polygon is to be projected.
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

    public Vector2 getNormal(Vector2 axis) {
        return new Vector2(-axis.y, axis.x);
    }

    /**
     * @return an array of normals for each axis on the polygon
     */
    public Vector2[] getNormals() {
        Vector2[] normals = new Vector2[vertices.length];
        Vector2 axis = null;
        for (int i = 0; i < vertices.length; i++) {
            // getting an edge between two vertices
            axis = vertices[i].cpy().sub(vertices[i + 1 == vertices.length ? 0 : i + 1]);
            // the normal is the negative reciprocal of the slope
            normals[i] = getNormal(axis);
        }
        return normals;
    }

    public boolean containsPoint(Vector2 point) {
        Vector2[] normals;

        Vector2 polyProjection;
        float pointProjection;

        normals = getNormals();
        for (Vector2 normal : normals) {
            polyProjection = projectPolygon(normal);
            pointProjection = scalarProject(point, normal);

            if (polyProjection.x > pointProjection || polyProjection.y < pointProjection) {
                return false;
            }
        }
        return true;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.add(velocity);
    }
}
