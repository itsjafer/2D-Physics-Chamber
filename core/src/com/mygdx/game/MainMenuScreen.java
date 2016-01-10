/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.mygdx.game.input.GameKeys;
import com.mygdx.game.gamestate.GameScreenManager;
import com.mygdx.game.gamestate.MyScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Dmitry
 */
public class MainMenuScreen extends MyScreen{

    BitmapFont font;
    SpriteBatch spriteBatch;
    
    public MainMenuScreen(GameScreenManager gameStateManager)
    {
        super(gameStateManager);
    }
    
    @Override
    public void show() {
    }

    @Override
    public void render(float f) {
        spriteBatch.begin();
        font.draw(spriteBatch, "Main Menu Screen\n\nPress <Enter> to start", 200, 200);
        spriteBatch.end();
    }
    
    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    @Override
    public void update(float deltaTime) {
        processInput();
    }

    @Override
    public void processInput() {
        if (GameKeys.isKeyJustPressed(GameKeys.Keys.ENTER))
        {
            gameStateManager.setGameState(GameScreenManager.GameStates.MAIN_GAME);
        }
    }
    
    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
    
}
