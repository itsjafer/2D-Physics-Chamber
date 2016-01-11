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
    
    public GameWorld()
    {
        potentialPolygon = new ArrayList();
    }
    
    public void setPotentialPolygon(ArrayList<Vector2> potentialPolygon)
    {
        /// CONSIDER USING THE INSIDE OUT EVEN ODD CHECK THING TO MAKE SURE THE POLYGON IS CONVEX
        this.potentialPolygon = potentialPolygon;
    }
    
}
