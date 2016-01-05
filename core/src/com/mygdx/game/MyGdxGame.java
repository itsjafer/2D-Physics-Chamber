package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.model.GameWorld;

public class MyGdxGame extends ApplicationAdapter {

    UI gameUI;
    GameWorld world;
    GameInputProcessor gameInputManager;
    InputMultiplexer multiplexer;
    
    
    @Override
    public void create() {
        
        gameUI = new UI(0, 0);
        world = new GameWorld();
        
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GameInputProcessor(world));
        Gdx.input.setInputProcessor(multiplexer);
        
    }

    @Override
    public void render() {
        world.update(gameInputManager);
        gameUI.render();
    }

    @Override
    public void resize(int width, int height) {
        gameUI.resize(width, height);
    }
}
