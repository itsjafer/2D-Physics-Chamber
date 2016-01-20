/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.gamestate.MyScreen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 *
 * @author Dmitry
 */
public class MainMenuScreen extends MyScreen {

    Skin skin;
    Stage stage, stage2;
    Table table, table2;
    TextureAtlas atlas;
    InputMultiplexer im, im2;
    TextButton startGame, saveGame, loadGame, slot1, slot2, slot3;
    TextArea inputSlot1, inputSlot2, inputSlot3;
    GameScreen gameScreen;
    String input;
    boolean isSaving, isLoading, typing;
    private InputMultiplexer lastUsedMultiplexer;

    public MainMenuScreen(ScreenManager gameStateManager, GameScreen gameScreen) {
        super(gameStateManager);
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (isLoading || isSaving) {
            stage2.act(delta);
            stage2.draw();
        } else {
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void init() {
        stage = new Stage();
        stage2 = new Stage();

        //input for saving levels
        input = "";
        typing = false;
        //to determine whether user wants to save or load levels
        isLoading = false;
        isSaving = false;

        //Input multiplexer, giving priority to stage over gameinput
        im = new InputMultiplexer(stage, MyGdxGame.gameInput);
        im2 = new InputMultiplexer(stage2, MyGdxGame.gameInput);

        //set the processor to the initial default one
        lastUsedMultiplexer = im;

        // set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(im);

        //initialize skin by imlpementing the json file that implements the atlas
        // the json file has the buttonstyle,etc already coded into it, only need to call the name to use it
        skin = new Skin(Gdx.files.internal("ui-data/uiskin.json"));

        // Create a table that fills the screen, the buttons, etc go into this table
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        table.align(Align.top | Align.center);


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

        // Create a table that fills the screen, the buttons, etc go into this table
        table2 = new Table();
        stage2.addActor(table2);
        table2.setFillParent(true);
        table2.align(Align.top | Align.center);

        //create the buttons... b for button:
        slot1 = new TextButton("               Slot 1:              \n\n", skin, "default");
        slot2 = new TextButton("               Slot 2:              \n\n", skin, "default");
        slot3 = new TextButton("               Slot 3:              \n\n", skin, "default");
        inputSlot1 = new TextArea("Empty", skin);
        inputSlot2 = new TextArea("Empty", skin);
        inputSlot3 = new TextArea("Empty", skin);

        //add the buttons to the table
        table2.add(slot1).pad(MyGdxGame.HEIGHT / 4, 20, 20, 20);
        table2.add(inputSlot1).padTop(MyGdxGame.HEIGHT / 4 - 20);
        table2.row();
        table2.add(slot2).pad(20, 20, 20, 20);
        table2.add(inputSlot2);
        table2.row();
        table2.add(slot3).pad(20, 20, 20, 20);
        table2.add(inputSlot3);

        // add the inputs. they're in a seperate method because of the length;
        // to make the code clearer
        addInputs();
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.getInputProcessor() != lastUsedMultiplexer) {
            Gdx.input.setInputProcessor(lastUsedMultiplexer);
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
                isSaving = true;
                Gdx.input.setInputProcessor(im2);
                lastUsedMultiplexer = im2;
            }
        });
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadGame.setChecked(false);
                isLoading = true;
                Gdx.input.setInputProcessor(im2);
                lastUsedMultiplexer = im2;

            }
        });
        slot1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("hello");
                if (isSaving) {
                    gameScreen.world.saveLevel(1);
                    slot1.setText("               Slot 1:              \n\n" + inputSlot1.getText());
                } else if (isLoading) {
                    gameScreen.world.loadLevel(1);
                }
            }
        });
        inputSlot1.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return super.keyDown(event, keycode); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
        System.out.println("sdasd  " + inputSlot1.getDefaultInputListener());
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
