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
import com.mygdx.game.gamestate.MyScreen;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.input.GameInputs;

/**
 *
 * @author branc2347
 */
public class GameMenu extends MyScreen {

    Skin skin;
    Stage stage;
    SpriteBatch batch;
    TextureAtlas atlas;

    public GameMenu(ScreenManager gameStateManager) {
        super(gameStateManager);
    }

    @Override
    public void init() {
        batch = new SpriteBatch();
        stage = new Stage();
        atlas = new TextureAtlas("ui/atlas.pack");

        //initialize skin
        skin = new Skin(atlas);
//        skin.addRegions(atlas);
        
//        System.out.println(skin.get("button.up", Texture.class));
        
//        for (Texture text: atlas.getTextures())
//        {
//            System.out.println(text);
//        }
        
//        skin.add("buttonUp", skin.get("button.up", TextureRegion.class));
        
//        System.out.println(skin.get("button.up", TextureRegion.class).getTexture().getWidth());
//        System.out.println(skin.getRegion("button.up").getRegionWidth());
        
        skin.add("buttonUp", (skin.getRegion("button.down")), TextureRegion.class);
        
        //make a skin for the first button, named logo
//        skin.add("buttonUp", (atlas.findRegion("button.up").getTexture()));
//        System.out.println(atlas.findRegion("button.up").getTexture().getWidth());
        //add a font for the button
        skin.add("default", new BitmapFont());
        

        //configure what the button looks like in each of these cases:
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("buttonUp", Color.DARK_GRAY);
//        textButtonStyle.down = skin.newDrawable("button", Color.DARK_GRAY);
//        textButtonStyle.checked = skin.newDrawable("button", Color.BLUE);
//        textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a table that fills the screen, the buttons, etc go into this table
        Table table = new Table();
        table.setBounds(0, 0, MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2);
        stage.addActor(table);

        //create a button for the button picure
        final TextButton button = new TextButton("Click here, noob", skin, "default");
        table.add(button);

        //taken from the internet:
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("click, click, " + button.isChecked());
                button.setText("Good job!");
            }
        });
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void processInput() {
//        if (GameInputs.isKeyJustPressed(GameInputs.Keys.TAB)) {
//            gameStateManager.setGameState(ScreenManager.GameStates.MAIN_GAME);
//        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
        stage.dispose();
        skin.dispose();
    }
}
