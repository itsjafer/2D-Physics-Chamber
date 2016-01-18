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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    public MainMenuScreen(ScreenManager gameStateManager) {
        super(gameStateManager);
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
        gameScreen = new GameScreen(gameStateManager);

        //Input multiplexer, giving priority to stage over gameinput
        im = new InputMultiplexer(this.stage, MyGdxGame.gameInput);
        // set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(im);

        //initialize the atlas containing button textures
        atlas = new TextureAtlas("ui/atlas.pack");

        //initialize skin by imlpementing atlas
        skin = new Skin(atlas);

        //add the images for the default button and its various states
        skin.add("buttonUp", (skin.getRegion("button.up")), TextureRegion.class);
        skin.add("buttonDown", (skin.getRegion("button.down")), TextureRegion.class);

        //add a font to be used by objects that use skin
        skin.add("default", new BitmapFont());

        //configure what the default button looks like (button style) :
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("buttonUp");
        textButtonStyle.down = skin.newDrawable("buttonDown");
        textButtonStyle.checked = skin.newDrawable("buttonDown", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("buttonUp", Color.DARK_GRAY);
        textButtonStyle.font = skin.getFont("default");

        //add the button style defined above to 'skin' under the name "default" (it doesn't interfere with font)
        skin.add("defaultButton", textButtonStyle);

        // Create a table that fills the screen, the buttons, etc go into this table
        table = new Table();
        table.setFillParent(true);
        table.align(Align.top | Align.center);
        stage.addActor(table);

        //create the buttons... b for button:
        startGame = new TextButton("Start Game", skin, "defaultButton");
        saveGame = new TextButton("Save Game", skin, "defaultButton");
        loadGame = new TextButton("Load Game", skin, "defaultButton");
        //add the buttons to the table
        table.add(startGame).pad(MyGdxGame.HEIGHT / 3, 20, 20, 20);
        table.row();
        table.add(saveGame).pad(20, 20, 20, 20);
        table.row();
        table.add(loadGame).pad(20, 20, 20, 20);
        //taken from the internet:
        //        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        //        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        //        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        //        // revert the checked state.
    }

    @Override
    public void update(float deltaTime) {
        processInput();
    }

    @Override
    public void processInput() {
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ESCAPE)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
        }
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame.setChecked(false);
                startGame.setText("Resume Game");
                gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
            }
        });
        saveGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame.setChecked(false);
            }
        });
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame.setChecked(false);
            }
        });
    }
//    private void drawCenteredString(float xPos, float yPos, String text) {
//        //create a GlyphLayout, which is a new way to measure font metrics based on BitmapFont
//        GlyphLayout layout = new GlyphLayout();
//
//        //set the text based on the font and text to be centered
//        layout.setText(font, text);
//
//        //get the metrics of the String
//        float layoutWidth = layout.width;
//        float layoutHeight = layout.height;
//
//        //draw the centered string
//        font.draw(spriteBatch, text, (xPos - layoutWidth) / 2, (MyGdxGame.HEIGHT + layoutHeight) / 2);
//    }

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
