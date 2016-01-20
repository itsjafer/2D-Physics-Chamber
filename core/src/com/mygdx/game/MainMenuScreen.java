/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.gamestate.MyScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 *
 * @author Dmitry
 */
public class MainMenuScreen extends MyScreen {

    Skin skin;
    Stage stage;
    Table table;
    TextureAtlas atlas;
    InputMultiplexer im;
    TextButton startGame, saveGame, loadGame;
    GameScreen gameScreen;

    public MainMenuScreen(ScreenManager gameStateManager, GameScreen gameScreen) {
        super(gameStateManager);
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void init() {
        stage = new Stage();

        //Input multiplexer, giving priority to stage over gameinput
        im = new InputMultiplexer(stage, MyGdxGame.gameInput);
        // set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(im);

        //initialize skin by imlpementing the json file that implements the atlas
        // the json file has the buttonstyle,etc already coded into it, only need to call the name to use it
        skin = new Skin(Gdx.files.internal("ui-data/uiskin.json"));

        // Create a table that fills the screen, the buttons, etc go into this table
        table = new Table();
        table.setFillParent(true);
        table.align(Align.top | Align.center);
        stage.addActor(table);

        //create the buttons... b for button:
        startGame = new TextButton("Start Game", skin, "default");
        saveGame = new TextButton("Save Game", skin, "default");
        loadGame = new TextButton("Load Game", skin, "default");
        //add the buttons to the table
        table.add(startGame).pad(MyGdxGame.HEIGHT / 3, 20, 20, 20);
        table.row();
        table.add(saveGame).pad(20, 20, 20, 20);
        table.row();
        table.add(loadGame).pad(20, 20, 20, 20);

        // add the inputs. they're in a seperate method because of the length;
        //to make the code clearer
        addInputs();
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.getInputProcessor() != im) {
            Gdx.input.setInputProcessor(im);
        }
        processInput();
    }

    @Override
    public void processInput() {
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ESCAPE)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
        }
    }

    public void addInputs() {
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame.setText("Resume Game");
                startGame.setChecked(false);
                gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);

            }
        });
        saveGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveGame.setChecked(false);
                gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
            }
        });
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadGame.setChecked(false);
                gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
                System.out.println("loading");
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
