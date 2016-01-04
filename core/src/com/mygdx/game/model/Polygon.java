/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Dmitry
 */
public class Polygon {
    
    private Vector2[] vertices;
    private Vector2 velocity = new Vector2(0, 0);
    private float friction;
    private Vector2 startPos = null;
    
    public Polygon(Vector2[] vertices, float friction)
    {
        this.vertices = vertices;
        this.friction = friction;
        updateHome();
    }
    
    public void setVelocity(float diffX, float diffY)
    {
        velocity.set(diffX, diffY);
    }
    
    public void setFriction(float newFriction)
    {
        this.friction = newFriction;
    }
    
    public void move()
    {
        for (Vector2 vertex: vertices)
        {
            vertex.x += velocity.x;
            vertex.y += velocity.y;
        }
        
        velocity.scl(friction);
        if (velocity.len() <= 0.01)
            velocity.set(0, 0);
    }
    public void goHome(float speedFactor)
    {
        velocity.x = (startPos.x-vertices[0].x)*speedFactor;
        velocity.y = (startPos.y-vertices[0].y)*speedFactor;
        move();
    }
    public void updateHome()
    {
        startPos = vertices[0].cpy();
    }
    
    public Vector2[] getVertices()
    {
        return vertices;
    }
    
    public static float projectVector(Vector2 v1, Vector2 v2)
    {
        return v1.dot(v2)/v2.len();
    }
    
    public Vector2 project(Vector2 axis)
    {
        float min = vertices[0].dot(axis);
        float max = min;
        float dot = 0f;
        for (int i = 1; i < vertices.length; i ++)
        {
            dot = vertices[i].dot(axis);
            if (dot < min)
            {
                min = dot;
            }
            if (dot > max)
            {
                max = dot;
            }
        }
        return new Vector2 (min/axis.len(), max/axis.len());
    }
    
    public Vector2[] getEdges()
    {
        Vector2[] edges = new Vector2[vertices.length];
        Vector2 edge;
        for (int i = 0; i < vertices.length; i ++)
        {
            edge = vertices[i].cpy().sub(vertices[i+1 == vertices.length ? 0: i+1]);
            edges[i] = edge;
        }
        return edges;
    }
    
    public Vector2[] getNormals()
    {
        Vector2[] normals = new Vector2[vertices.length];
        Vector2 axis = null;
        for (int i = 0; i < vertices.length; i ++)
        {
            axis = vertices[i].cpy().sub(vertices[i+1 == vertices.length ? 0: i+1]);
            normals[i] = new Vector2(-axis.y, axis.x);
        }
        return normals;
    }
    
}
