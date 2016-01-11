/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.gamestate.MyScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Dmitry
 */
public class MainMenuScreen extends MyScreen {

    BitmapFont font;
    SpriteBatch spriteBatch;

    public MainMenuScreen(ScreenManager gameStateManager) {
        super(gameStateManager);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float f) {
        spriteBatch.begin();
        //font.draw(spriteBatch, "Main Menu Screen\n\nPress <Enter> to start", MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2);
        drawCenteredString(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, "Main Menu Screen\n\nPress <Enter> to start");
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
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ENTER)) {
            gameStateManager.setGameState(ScreenManager.GameStates.MAIN_GAME);
        }
    }

    private void drawCenteredString(float xPos, float yPos, String text) {
        //create a GlyphLayout, which is a new way to measure font metrics based on BitmapFont
        GlyphLayout layout = new GlyphLayout(); 
        
        //set the text based on the font and text to be centered
        layout.setText(font, text);
        
        //get the metrics of the String
        float layoutWidth = layout.width;
        float layoutHeight = layout.height;
        
        //draw the centered string
        font.draw(spriteBatch, text, (xPos - layoutWidth) / 2, (MyGdxGame.HEIGHT + layoutHeight) / 2);
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
