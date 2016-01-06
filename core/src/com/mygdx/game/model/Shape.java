/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author branc2347
 */
public abstract class Shape {

    private Vector2[] vertices;
    private Vector2 velocity = new Vector2(0, 0);
    private float friction;
    private Vector2 startPos;

    public Shape(float friction, Vector2 startPos) {
        this.friction = friction;
        this.startPos = startPos;
    }

    public void setVelocity(float diffX, float diffY) {
        velocity.set(diffX, diffY);
    }

    public void setFriction(float newFriction) {
        this.friction = newFriction;
    }

    public abstract void move();

    public abstract void goHome(float speedFactor);

    public abstract void updateHome();

    public static float projectVector(Vector2 v1, Vector2 v2) {
        return v1.dot(v2) / v2.len();
    }

    public abstract Vector2 project(Vector2 axis);
}
