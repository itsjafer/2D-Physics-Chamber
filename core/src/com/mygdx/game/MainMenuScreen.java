/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.gamescreen.ScreenManager;
import com.mygdx.game.gamescreen.MyScreen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.model.LevelLoader;

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
    TextButton startGame, saveGame, loadGame, slot1, slot2, slot3, returnToMenu;
    String slot1Text, slot2Text, slot3Text;
    TextField inputSlot1, inputSlot2, inputSlot3;
    Label notification;
    GameScreen gameScreen;
    String input;
    boolean isSaving, isLoading, typing;
    private InputMultiplexer lastUsedMultiplexer;
    InputListener inputListen = new InputListener();
    private LevelLoader loader;

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

        //create loader
        loader = new LevelLoader();

        //input for saving levels
        input = "";
        typing = false;

        //to determine whether user wants to save or load levels
        isLoading = false;
        isSaving = false;

        //the saved text describing the 3 slots of load/save
        slot1Text = "Blank";
        slot2Text = "Blank";
        slot3Text = "Blank";

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

        //create the buttons for table 2
        slot1 = new TextButton("Slot 1:\n\n" + "'" + slot1Text + "'", skin, "default");
        slot2 = new TextButton("Slot 2:\n\n" + "'" + slot2Text + "'", skin, "default");
        slot3 = new TextButton("Slot 3:\n\n" + "'" + slot3Text + "'", skin, "default");
        returnToMenu = new TextButton("Return to Menu", skin);
        notification = new Label("Saved to slot1", skin);
        inputSlot1 = new TextField(slot1Text, skin);
        inputSlot2 = new TextField(slot2Text, skin);
        inputSlot3 = new TextField(slot3Text, skin);

        //add the buttons to the table
        table2.add(returnToMenu).padTop(20);
        table2.add(notification).size(100, 30);
        table2.row();
        table2.add(slot1).pad(20, 20, 20, 20).size(200, 100);
        table2.add(inputSlot1).padTop(30);
        table2.row();
        table2.add(slot2).pad(20, 20, 20, 20).size(200, 100);
        table2.add(inputSlot2);
        table2.row();
        table2.add(slot3).pad(20, 20, 0, 20).size(200, 100);
        table2.add(inputSlot3);
        table2.row();

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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (!inputSlot1.getText().equalsIgnoreCase(slot1Text) && !inputSlot1.getText().equals("Blank")) {
                slot1Text = inputSlot1.getText();
            }
            if (!inputSlot2.getText().equalsIgnoreCase(slot2Text) && !inputSlot2.getText().equals("Blank")) {
                slot2Text = inputSlot2.getText();
            }
            if (!inputSlot3.getText().equalsIgnoreCase(slot3Text) && !inputSlot3.getText().equals("Blank")) {
                slot3Text = inputSlot3.getText();
            }
        }

        //this tests the user's input, whether they have clicked load . if they have, give them the option of going to main menu
        if (notification.isVisible() && notification.getText().charAt(0) == 'L') {
            returnToMenu.setText("Go to Game");
        } else if (!notification.isVisible() && !returnToMenu.getText().equals("Return to Menu")) {
            returnToMenu.setText("Return to Menu");
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
                notification.setVisible(false);
                isSaving = true;
                Gdx.input.setInputProcessor(im2);
                lastUsedMultiplexer = im2;
            }
        });
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadGame.setChecked(false);
                notification.setVisible(false);
                isLoading = true;
                Gdx.input.setInputProcessor(im2);
                lastUsedMultiplexer = im2;
            }
        });
        slot1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSaving) {
                    slot1.setText("Slot 1:\n\n" + "'" + slot1Text + "'");
                    notification.setVisible(true);
                    notification.setText("Saved to slot 1");
                    loader.saveLevel(0);

                } else if (isLoading) {
                    notification.setVisible(true);
                    notification.setText("Loaded slot 1");
                    loader.loadLevel(0);
                }
            }
        });
        slot2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSaving) {
                    slot2.setText("Slot 2:\n\n" + "'" + slot2Text + "'");
                    notification.setVisible(true);
                    notification.setText("Saved to slot 2");
                    loader.saveLevel(1);

                } else if (isLoading) {
                    notification.setVisible(true);
                    notification.setText("Loaded slot 2");
                    loader.loadLevel(1);
                }
            }
        });
        slot3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSaving) {
                    slot3.setText("Slot 3:\n\n" + "'" + slot3Text + "'");
                    notification.setVisible(true);
                    notification.setText("Saved to slot 3");
                    loader.saveLevel(2);

                } else if (isLoading) {
                    notification.setVisible(true);
                    notification.setText("Loaded slot 3");
                    loader.loadLevel(2);
                }
            }
        });
        returnToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (notification.isVisible() && notification.getText().charAt(0) == 'L') {
                    gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
                    Gdx.input.setInputProcessor(im);
                    lastUsedMultiplexer = im;
                    isLoading = false;
                    isSaving = false;
                } else {
                    Gdx.input.setInputProcessor(im);
                    lastUsedMultiplexer = im;
                    isLoading = false;
                    isSaving = false;
                }

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
