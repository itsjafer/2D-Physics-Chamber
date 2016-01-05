/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameInputProcessor;

/**
 *
 * @author kobed6328
 */
public class GameWorld {
    
    public GameWorld()
    {
    }
    
    public void update(GameInputProcessor inputProcessor)
    {
//        System.out.println(inputProcessor.keyDown(Input.Keys.W));
    }

    public void moveMarioUp() {
        System.out.println("JUMPING");
    }
    
}
