
package com.mygdx.game;

import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.GameInputProcessor;
import com.mygdx.game.gamestate.GameScreenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MyGdxGame extends Game {

    // The game state manager for the entire game
    GameScreenManager gameStateManager;
    
    // screen width and height
    public static int WIDTH, HEIGHT;
    
    /**
     * Creates the game
     */
    @Override
    public void create() {
        // initialize width and height
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        // The game state manager starts out showing the menu screen
        gameStateManager = new GameScreenManager(GameScreenManager.GameStates.MENU);
        // The game input processor is gonna distribute all of the input for the game
        Gdx.input.setInputProcessor(new GameInputProcessor());
    }

    /**
     * The main game loop
     */
    @Override
    public void render() {
        // Clear the screen
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // First update the current gamestate
        gameStateManager.update(Gdx.graphics.getDeltaTime());
        // Then draw it
        gameStateManager.render(Gdx.graphics.getDeltaTime());
        // Update the GameKeys key states
        GameKeys.update();
    }

    /**
     * Resizes the window
     * @param width the new window width
     * @param height the new window height
     */
    @Override
    public void resize(int width, int height) {
    }
}
