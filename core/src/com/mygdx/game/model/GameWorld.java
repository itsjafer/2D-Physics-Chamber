/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameWorld {

    private ArrayList<Vector2> potentialPolygon;
    private ArrayList<Polygon> polygons;
    private Player player;
    
    private Vector2 gravity;

    public GameWorld() {
        potentialPolygon = new ArrayList();
        polygons = new ArrayList();
        gravity = new Vector2(3.5f, -2.6f);
    }

    public void update(float deltaTime) {
        if (player != null) {
            player.applyAcceleration(gravity);
            player.move(deltaTime);
            if (!polygons.isEmpty()) {
                player.collideWithPolygons(polygons);
            }
            player.update();
        }
    }

    public void setPotentialPolygon(ArrayList<Vector2> potentialPolygon) {
        this.potentialPolygon = potentialPolygon;
    }

    /**
     * Creates a polygon based on the vectors passed in
     * @param polygon arraylist of vertices of the polygon
     */
    public void createPolygon(ArrayList<Vector2> polygon) {
        polygons.add(new Polygon(polygon.toArray(new Vector2[polygon.size()]), 0));
    }

    /**
     * Getter method for the polygons arraylist
     * @return 
     */
    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    /**
     * Creates the player based on the vectors passed in
     * @param playerPolygon arraylist of vertices
     */
    public void createPlayer(ArrayList<Vector2> playerPolygon) {
        player = new Player(playerPolygon.toArray(new Vector2[playerPolygon.size()]), 0);
    }

    /**
     * Getter method for the player polygon
     * @return 
     */
    public Player getPlayer() {
        return player;
    }
}
