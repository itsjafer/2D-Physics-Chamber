package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;

public class MyGdxGame extends ApplicationAdapter {

    UI gameUI;
    World world;
    
    @Override
    public void create () {
        gameUI = new UI();
        world = new World();
    }

    @Override
    public void render () {
        world.update();
        gameUI.render(world);
    }
}
