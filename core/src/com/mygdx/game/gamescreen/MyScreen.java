
package com.mygdx.game.gamescreen;

import com.badlogic.gdx.Screen;

/**
 * A modified version of the Screen class that separates drawing and logic, and handles input
 * @author Dmitry
 */
public abstract class MyScreen implements Screen{
    
    // Has an instance of the game state manager so that individual screen instances switch the current screen
    protected ScreenManager gameStateManager;
    
    /**
     * Creates a screen object
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
}
