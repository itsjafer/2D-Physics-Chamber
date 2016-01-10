/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.gamestate;

import com.mygdx.game.GameScreen;
import com.mygdx.game.MainMenuScreen;

/**
 * Controls the GameStates for the entire game. It basically mimics the screen managing functions of the badlogic Game class
 * @author Dmitry
 */
public class GameScreenManager {

    // the MyScreen currently being shown on screen
    private MyScreen currentGameState;
    // a "permanent" instance of the main game so that the game doesn't restart every time this gameinstance is activated
    /// Another approach to thsi would be to store all fo the game states in a hashmpa here in this class and then IF we want it to rest
    //// (like the main menu), we just switch to it and call "init()"
    private GameScreen gameScreenInstance;
    
    // All of the game states for the game
    public static enum GameStates{
        MENU, MAIN_GAME;
    }
    /**
     * Creates a MyScreen manager.
     * @param startingGameState the starting game state.
     */
    public GameScreenManager(GameStates startingGameState)
    {
        setGameState(startingGameState);
    }
    /**
     * Sets the current game state
     * @param gameState the enum value of the target game state
     */
    public void setGameState(GameStates gameState)
    {
        //// we might not need this
        // Dispose of the current game state as it is going to be switched
        if (currentGameState != null)
            currentGameState.dispose();
        
        // Since the gameState variable is an enum, identify the target game state and initialize it separately
        switch(gameState)
        {
            case MAIN_GAME:
                // To avoid restarting the game, only create a new instance if it hasn't yet been created.
                if (gameScreenInstance == null)
                    gameScreenInstance = new GameScreen(this);
                currentGameState = gameScreenInstance;
                break;
            case MENU:
                currentGameState = new MainMenuScreen(this);
                break;
        }
    }
    /**
     * Draws the current game state
     * @param deltaTime the amount of time the last frame took to be rendered.
     */
    public void render(float deltaTime)
    {
        currentGameState.render(deltaTime);
    }
    
    /**
     * Updates the current game state
     * @param deltaTime the amount of time the last frame took to be rendered
     */
    public void update(float deltaTime)
    {
        currentGameState.update(deltaTime);
    }
    
}
