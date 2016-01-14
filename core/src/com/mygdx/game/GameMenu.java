/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.gamestate.MyScreen;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.input.GameInputs;

/**
 *
 * @author branc2347
 */
public class GameMenu {

    Skin skin;
    Stage stage;
    SpriteBatch batch;
    TextureAtlas atlas;

    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("ui/atlas.pack");

        //initialize skin
        skin = new Skin(atlas);

        //add the images for the first button
        skin.add("buttonUp", (skin.getRegion("button.up")), TextureRegion.class);
        skin.add("buttonDown", (skin.getRegion("button.down")), TextureRegion.class);

        //add a font for the button
        skin.add("default", new BitmapFont());

        //configure what the button looks like in each of these cases:
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("buttonUp");
        textButtonStyle.down = skin.newDrawable("buttonDown");
        textButtonStyle.checked = skin.newDrawable("buttonDown", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("buttonUp", Color.DARK_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a table that fills the screen, the buttons, etc go into this table
        Table table = new Table();
        table.setFillParent(true);
        table.align(Align.topLeft);
        stage.addActor(table);

        //create a button for the button picure
        final TextButton button = new TextButton("Click here", skin);
        table.add(button).pad(5, 5, 5, 5);

        //taken from the internet:
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });
    }

    public void update(float deltaTime) {
        processInput();
    }

    public void processInput() {
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void show() {
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
