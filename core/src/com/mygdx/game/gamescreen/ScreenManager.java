/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.gamescreen;

import com.mygdx.game.GameMenu;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.MyGdxGame;
import java.util.HashMap;

/**
 * Controls the game screens for the entire game. It basically mimics the screen
 * managing functions of the badlogic Game class, but adds some input flow, and
 * separates logic from drawing
 *
 * @author Dmitry
 */
public class ScreenManager {

    // All of the screens that have already been created
    HashMap<GameScreens, MyScreen> activeScreens;
    // the current screen
    private MyScreen currentGameState;

    // All of the possible game screens
    public static enum GameScreens {

        MAIN_MENU, MAIN_GAME, GAME_MENU;
    }

    /**
     * Creates the screen manager manager.
     *
     * @param startingScreen the initial screen.
     */
    public ScreenManager(GameScreens startingScreen) {
        activeScreens = new HashMap<GameScreens, MyScreen>();
        setGameScreen(startingScreen);
    }

    /**
     * Sets the current game screen
     *
     * @param gameState the enum value of the target game state
     */
    public void setGameScreen(GameScreens screen) {
        // Dispose of the current screen as it is going to be switched
        if (currentGameState != null) {
            currentGameState.dispose();
        }

        ////////////////// IMPORTANT  FIX THE DEFAULT THING... WITH THE PARAMATER THING SAVE/LOAD..

        // Initialize each screen based on its requirements
        switch (screen) {
            case MAIN_MENU:
                // To avoid restarting the game, only create a new instance if it hasn't yet been created.
                if (!activeScreens.containsKey(screen)) {
                    activeScreens.put(screen, new MainMenuScreen(this, (GameScreen) activeScreens.get(GameScreens.MAIN_GAME)));
                }
                currentGameState = activeScreens.get(screen);
                break;
            case GAME_MENU:
                if (!activeScreens.containsKey(screen)) {
                    activeScreens.put(screen, new GameMenu(this, (GameScreen) activeScreens.get(GameScreens.MAIN_GAME)));
                }
                currentGameState = activeScreens.get(screen);
                break;
            case MAIN_GAME:
                if (!activeScreens.containsKey(screen)) {
                    activeScreens.put(screen, new GameScreen(this));
                }
                currentGameState = activeScreens.get(screen);
                break;
        }
    }

    /**
     * Draws the current game state
     *
     * @param deltaTime the amount of time the last frame took to be rendered.
     */
    public void render(float deltaTime) {
        currentGameState.render(deltaTime);
    }

    /**
     * Updates the current game state
     *
     * @param deltaTime the amount of time the last frame took to be rendered
     */
    public void update(float deltaTime) {
        currentGameState.update(deltaTime);
    }
}
