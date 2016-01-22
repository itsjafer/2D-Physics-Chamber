/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class VectorMath {
    
    /**
     * Gets the normal for an axis
     * @param axis the axis for which the normal is obtained
     * @return the normal
     */
    public static Vector2 getNormal(Vector2 axis) {
        // simply get the negative reciprocal of the slope
        return new Vector2(-axis.y, axis.x);
    }
    
    /**
     * Scalar projects a vector onto another vector
     * @param v1 the vector to be projected
     * @param v2 the vector onto which v1 is projected
     * @return the scalar projection of v1 onto v2
     */
    public static float scalarProject(Vector2 v1, Vector2 v2) {
        float dotProduct = v1.cpy().dot(v2);
        float scalarProjection = dotProduct / v2.len();

        return scalarProjection;
    }
    
    /**
     * Vector projects a vector onto another vector
     * @param v1 the vector to be projected
     * @param v2 the vector onto which v1 is projected
     * @return the vector projection of v1 onto v2
     */
    public static Vector2 vectorProject(Vector2 v1, Vector2 v2) {
        // first find the scalar projection
        float scalarProjection = scalarProject(v1, v2);
        // then apply that magnitude onto the target vector
        Vector2 vectorProjection = v2.cpy().nor().scl(scalarProjection);

        return vectorProjection;
    }
}
