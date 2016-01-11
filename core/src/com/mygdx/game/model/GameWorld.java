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
    
    public GameWorld()
    {
        potentialPolygon = new ArrayList();
        polygons = new ArrayList();
    }
    
    public void setPotentialPolygon(ArrayList<Vector2> potentialPolygon)
    {
        /// CONSIDER USING THE INSIDE OUT EVEN ODD CHECK THING TO MAKE SURE THE POLYGON IS CONVEX
        this.potentialPolygon = potentialPolygon;
    }
    
    public void createPolygon(ArrayList<Vector2> polygon)
    {
        polygons.add(new Polygons(polygon.toArray(new Vector2[polygon.size()]), 0));
    }
    
    public ArrayList<Polygons> getPolygons()
    {
        return polygons;
    }
}
