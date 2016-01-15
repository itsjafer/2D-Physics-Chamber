/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.input.GameInputs;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameWorld {

    private ArrayList<Polygon> polygons;
    private Player player;
    private Vector2 gravity;

    public GameWorld() {
        polygons = new ArrayList();
        gravity = new Vector2(0, -9.8f);
    }

    public void update(float deltaTime) {

//        if (player != null && GameInputs.isKeyDown(GameInputs.Keys.UP)) {
        if (player != null) {
            player.applyAcceleration(gravity);
            player.move(deltaTime);
            if (!polygons.isEmpty()) {
                player.collideWithPolygons(polygons);
            }
            player.update();
        }
    }

    /**
     * Creates a polygon based on the vectors passed in
     *
     * @param polygon arraylist of vertices of the polygon
     */
    public void createPolygon(ArrayList<Vector2> polygon) {
        polygons.add(new Polygon(polygon.toArray(new Vector2[polygon.size()])));
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
    public void createPlayer(ArrayList<Vector2> playerPolygon) {
        player = new Player(playerPolygon.toArray(new Vector2[playerPolygon.size()]));
    }

    /**
     * Getter method for the player polygon
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public void saveLevel() {
        //save polygon vertices 
        //save player vertices
        for (Polygon polygon : polygons) {
            for (Vector2 vertice : polygon.getVertices()) {
                System.out.println(vertice);
            }
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("level.txt"));
            for (int i = 0; i < 4; i++) {
                out.write("test " + "\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }
}
