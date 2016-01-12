/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author kobed6328
 */
public class GameWorld {

    private ArrayList<Vector2> potentialPolygon;
    private ArrayList<Polygons> polygons;
    private Player player;

    public GameWorld() {
        potentialPolygon = new ArrayList();
        polygons = new ArrayList();
    }
    
    public void update(float deltaTime)
    {
        if (player != null)
        {
            player.move(deltaTime);
            if (!polygons.isEmpty())
            {
                player.collideWithPolygons(polygons);
            }
        }
    }

    public void setPotentialPolygon(ArrayList<Vector2> potentialPolygon) {
        this.potentialPolygon = potentialPolygon;
    }

    public void createPolygon(ArrayList<Vector2> polygon) {
        polygons.add(new Polygons(polygon.toArray(new Vector2[polygon.size()]), 0));
    }

    public ArrayList<Polygons> getPolygons() {
        return polygons;
    }

    public void createPlayer() {
        if (player == null) {
            Vector2[] playerVertices = {new Vector2(100, 125), new Vector2(112, 125), new Vector2(112, 100), new Vector2(100, 100)};
            player = new Player(playerVertices, 0);
        }
    }
    
    public Player getPlayer()
    {
        return player;
    }
}
