/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.gamescreen;

import com.badlogic.gdx.Screen;

/**
 * A modified version of the Screen class that draws screen content, updates screen content, and handles input for the screen content
 * @author Dmitry
 */
public abstract class MyScreen implements Screen{
    
    // Has an instance of the game state manager so that individual MyScreen instances can invoke switched to other MyScreen instances
    protected ScreenManager gameStateManager;
    /**
     * Creates a MyScreen object
     * @param gameStateManager the gameStateManager
     */
    public MyScreen(ScreenManager gameStateManager)
    {
        this.gameStateManager = gameStateManager;
        // Upon creation, initialize this object
        init();
    }
    
    /**
     * Initializes the MyScreen object
     */
    public abstract void init();
    /**
     * Updates content
     * @param deltaTime the time it took to render the last frame
     */
    public abstract void update(float deltaTime);
    /**
     * Responds to user input
     */
    public abstract void processInput();
    
    /**
     * Resizes the screen
     * @param width new screen width
     * @param height new screen height
     */
    @Override
    public abstract void resize(int width, int height);
    
}
