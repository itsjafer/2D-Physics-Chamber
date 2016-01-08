
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.model.GameWorld;

public class MyGdxGame extends ApplicationAdapter {

    // The in-game UI
    UI gameUI;
    GameWorld world;
    // this input processor is specific to the in-game environment
    GameInputProcessor gameInputManager;
    // controls the various (TO BE ADDED) input processors for the game
    InputMultiplexer multiplexer;
    
    /**
     * Creates the game
     */
    @Override
    public void create() {
        
        // Initialize the game elements
        gameUI = new UI();
        world = new GameWorld();
        
        /////////////// MULTIPLEXER CODE.. UNTIL WE ACTUALLY USE MULTIPLE INPUT STREAMS DON'T USE THIS
        //////////////////// CAIUS THIS MEANS U ////////////////////////
//        // Create the multiplexer, and add to it the various input processors
//        multiplexer = new InputMultiplexer();
//        multiplexer.addProcessor(new GameInputProcessor(world));
//        // The multiplexer is now the main input controller for the game
        
        // Sets the game input processor to 
        gameInputManager = new GameInputProcessor(world);
        Gdx.input.setInputProcessor(gameInputManager);
        
    }

    /**
     * The main game loop
     */
    @Override
    public void render() {
        // Updates all of the elements in the world <<< CAIUS U NEED TO CHANGE THIS SINCE WE'RE USING
        // AN INPUT MULTIPLEXER... U MIGHT EVEN NEED TO CHANGE THE ENTIRE INPUT FLOW
        world.update(gameInputManager);
        // renders the game world
        gameUI.render();
    }

    /**
     * Resizes the window
     * @param width the new window width
     * @param height the new window height
     */
    @Override
    public void resize(int width, int height) {
        gameUI.resize(width, height);
    }
}
