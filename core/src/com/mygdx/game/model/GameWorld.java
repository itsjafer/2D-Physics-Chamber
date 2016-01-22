/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameWorld {

    private ArrayList<Polygon> polygons;
    private Player player;
    
    private Vector2 gravity;
    // Axis on which the player jumps (unit vector)
    private Vector2 horizontalMovementAxis;
    // Axis on which the player runs (unit vector)
    private Vector2 verticalMovementAxis;
    
    /**
     * Creates the game world
     */
    public GameWorld() {
        polygons = new ArrayList();
        init();
    }
    
    /**
     * Resets the defaults
     */
    public void reset()
    {
        init();
    }
    
    /**
     * Sets the default values
     */
    private void init()
    {
        player = null;
        polygons.clear();
        setGravity(new Vector2(0, -500));
    }

    /**
     * Updates the player
     * @param deltaTime 
     */
    public void update(float deltaTime) {
        if (player != null) {
            // gravity is applied here. any movement forces should have already been applied elsewhere
            player.applyAcceleration(gravity);
            // moves the player for one gametick
            player.move(deltaTime);
            // Collides the player with the polygons of the world
            if (!polygons.isEmpty()) {
                player.collideWithPolygons(polygons);
            }
            // update the player
            player.update();
        }
    }

    /**
     * Moves the player right
     */
    public void movePlayerRight() {
        // only apply movement if the player's speed is smaller than the running speed
        if (player.runningSpeed(horizontalMovementAxis) >= Player.RUN_SPEED)
            return;
        // get the "instantaneous" acceleration to achieve the desired velocity
        Vector2 accel = player.accelerationToVelocity(horizontalMovementAxis, horizontalMovementAxis.cpy().scl(Player.RUN_SPEED));
        player.applyAcceleration(accel);
    }

    /**
     * Moves the player left
     */
    public void movePlayerLeft() {
        // only apply movement if the player's speed is smaller than the running speed
        if (player.runningSpeed(horizontalMovementAxis) <= -Player.RUN_SPEED)
            return;
        // get the "instantaneous" acceleration to achieve the desired velocity
        Vector2 accel = player.accelerationToVelocity(horizontalMovementAxis, horizontalMovementAxis.cpy().scl(-Player.RUN_SPEED));
        player.applyAcceleration(accel);
    }

    /**
     * Jumps the player
     */
    public void jumpPlayer() {
        // the player should only jump when he is on ground
        if (player.onGround()) {
            // Vf^2 = Vi^2-2ad ..... Vf is zero (the peak of the jump).... therefore, Vi = Math.sqrt(-2ad)...
                // so all that's left to do is to scalar project the acceleration + displacement onto the desired movement axis
                // and apply that resulting Vi onto the desired axis
            Vector2 verticalVel = verticalMovementAxis.cpy().scl((float)Math.sqrt(-2*VectorMath.scalarProject(gravity, verticalMovementAxis)*verticalMovementAxis.cpy().scl(Player.JUMP_DISTANCE).len()));
            // get the "instantaneous" acceleration to achieve the desired velocity
            Vector2 accel = player.accelerationToVelocity(verticalMovementAxis, verticalVel);
            player.applyAcceleration(accel);
        }
    }

    /**
     * Creates a polygon and adds it to the list
     * @param polygon list of vertices of the polygon
     * @param colour the color of the new polygon
     */
    public void createPolygon(ArrayList<Vector2> polygon, Color colour) {
        polygons.add(new Polygon(polygon.toArray(new Vector2[polygon.size()]), colour));
    }

    /**
     * Updates the gravity and horizontal/vertical direction vectors
     * @param gravity the vector
     */
    public void setGravity(Vector2 gravity) {
        // update gravity
        this.gravity = gravity;
        // perpendicular to gravity
        horizontalMovementAxis = VectorMath.getNormal(gravity).nor();
        // parallel but opposite in dir to gravity
        verticalMovementAxis = gravity.cpy().nor().scl(-1);
    }

    /**
     * Get the gravity of the world, including direction and magnitude
     */
    public Vector2 getGravity() {
        return this.gravity;
    }

    /**
     * Get the polygon list
     * @return
     */
    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    /**
     * Creates the player based on the vectors passed in
     * @param playerPolygon arraylist of vertices
     * @param colour the color of the player
     */
    public void createPlayer(ArrayList<Vector2> playerPolygon, Color colour) {
        player = new Player(playerPolygon.toArray(new Vector2[playerPolygon.size()]), colour);
    }

    /**
     * Get the player
     * @return
     */
    public Player getPlayer() {
        return player;
    }
}
