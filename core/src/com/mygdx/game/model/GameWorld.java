/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameWorld {

    private ArrayList<Polygon> polygons;
    private Player player;
    private Vector2 gravity;
    private Vector2 horizontalMovementAxis;
    static public Vector2 verticalMovementAxis;
    private Polygon finish;

    public GameWorld() {
        polygons = new ArrayList();
        updateGravity(new Vector2(0, -30));
    }

    public void update(float deltaTime) {

//        if (player != null && GameInputs.isKeyDown(GameInputs.Keys.UP)) {
//        if (player != null && GameInputs.isKeyJustPressed(GameInputs.Keys.UP)) {
        if (player != null) {
            player.applyAcceleration(gravity);
            player.move(deltaTime);
            if (!polygons.isEmpty()) {
                player.collideWithPolygons(polygons);
            }
            player.update();
        }
    }

    public void movePlayerRight() {
        player.applyAcceleration(horizontalMovementAxis.cpy().scl(Player.HORIZONTAL_ACCELERATION));
    }

    public void movePlayerLeft() {
        player.applyAcceleration(horizontalMovementAxis.cpy().scl(-Player.HORIZONTAL_ACCELERATION));
    }

    public void jumpPlayer() {


//        player.setVelocity(verticalMovementAxis.cpy().scl((float)Math.sqrt(Polygon.scalarProject(gravity, verticalMovementAxis)*(-2*Polygon.scalarProject(new Vector2(0, 100), verticalMovementAxis)))));
//        System.out.println(player.getVelocity());
//        
//        player.totalHeight = new Vector2(0, 0);
//        player.totalTime = 0f;
        if (player.canJump()) {
            player.setVelocity(new Vector2(0, 316));
            player.jump();
        }
    }

    /**
     * Creates a polygon based on the vectors passed in
     *
     * @param polygon arraylist of vertices of the polygon
     */
    public void createPolygon(ArrayList<Vector2> polygon, Color colour) {
        polygons.add(new Polygon(polygon.toArray(new Vector2[polygon.size()]), colour));
    }

    /**
     * Sets the gravity of the world, including direction and magnitude
     *
     * @param gravity - direction and magnitude
     */
    public void updateGravity(Vector2 gravity) {
        this.gravity = gravity;
        horizontalMovementAxis = Polygon.getNormal(gravity).nor();
        verticalMovementAxis = gravity.cpy().nor().scl(-1);
    }

    /**
     * Get the gravity of the world, including direction and magnitude
     *
     * @return gravity - direction and magnitude
     */
    public Vector2 getGravity() {
        return this.gravity;
    }

    /**
     * Getter method for the polygons arraylist
     *
     * @return
     */
    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    /**
     * Creates the player based on the vectors passed in
     *
     * @param playerPolygon arraylist of vertices
     */
    public void createPlayer(ArrayList<Vector2> playerPolygon, Color colour) {
        player = new Player(playerPolygon.toArray(new Vector2[playerPolygon.size()]), colour, this);
    }

    /**
     * Getter method for the player polygon
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public void deletePlayer() {
        player = null;
    }

    public void createFinish(ArrayList<Vector2> vertices, Color colour) {
        finish = new Polygon(vertices.toArray(new Vector2[vertices.size()]), colour);
    }
}
