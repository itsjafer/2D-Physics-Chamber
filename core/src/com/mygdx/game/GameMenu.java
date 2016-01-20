/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.gamestate.MyScreen;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.input.GameInputs;

/**
 *
 * @author branc2347
 */
public class GameMenu extends MyScreen {

    Skin skin, skinCanvas, skinColourSample;
    Stage stage, stage2;
    GameScreen background;
    InputMultiplexer im, im2, lastUsedMultiplexer;
    TextButton resetPlayer, resetLevel, colours, backToMenu, sampleColourButton, backToGame;
    Slider gravitySliderY, gravitySliderX;
    Label labelSliderY, labelSliderX;
    CheckBox snapToGrid;
    Color drawColour;
    ImageButton colourPalette;
    Pixmap canvas;
    boolean choosingColour;

    public GameMenu(ScreenManager gameStateManager, GameScreen background) {
        super(gameStateManager);
        this.background = background;
    }

    @Override
    public void init() {

        //for all the buttons/sliders
        stage = new Stage();
        //for the colour palette 
        stage2 = new Stage();
        //Input multiplexer, giving priority to stage over gameinput
        im = new InputMultiplexer(this.stage, MyGdxGame.gameInput);
        //same as above, but giving priority to stage 2, which is colour choosing
        im2 = new InputMultiplexer(this.stage2, MyGdxGame.gameInput);
        //in order to know which input multiplexer was used last (when switching screens)
        lastUsedMultiplexer = im;
        // set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(im);

        //when true, it renders stage 2, when false it renders stage 1
        choosingColour = false;

        //initialize skin by imlpementing the json file that implements the atlas
        // the json file has the buttonstyle,etc already coded into it, only need to call the name to use it
        skin = new Skin(Gdx.files.internal("ui-data/uiskin.json"));
        //skin for the colour palette
        skinCanvas = new Skin(new TextureAtlas(Gdx.files.internal("ui-data/canvas.pack")));
        //skin for the COLOUR SAMPLE
        skinColourSample = new Skin();
        canvas = new Pixmap(Gdx.files.internal("ui-data/canvas.png"));
        // Create a table1 that fills the screen, the buttons, etc go into this table1
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();

        //table 1 LAYOUT
        stage.addActor(table1);
        table1.setFillParent(true);
        table1.align(Align.topLeft);

        //create the buttons for table 1
        resetPlayer = new TextButton("Reset Player", skin, "default");
        resetLevel = new TextButton("Reset Shapes", skin, "default");
        colours = new TextButton("Colours:", skin, "default");
        backToGame = new TextButton("Return to Game", skin);
        snapToGrid = new CheckBox("Snap to grid", skin);

        //add objects to table1
        table1.add(resetPlayer).pad(4, 4, 4, 4);
        table1.add(resetLevel).pad(4, 0, 4, 4);
        table1.add(colours).pad(4, 0, 4, 4);
        table1.add(backToGame);
        table1.row();
        table1.add(snapToGrid);

        //table 2 LAYOUT
        stage.addActor(table2);
        table2.setFillParent(true);
        table2.align(Align.bottomLeft);

        //create the objects for table 2
        gravitySliderY = new Slider(-100, 100, 10, false, skin);
        gravitySliderX = new Slider(-100, 100, 10, false, skin);
        labelSliderY = new Label("Gravity y-dir: " + gravitySliderY.getVisualValue(), skin);
        labelSliderX = new Label("Gravity x-dir: " + gravitySliderX.getVisualValue(), skin);

        //add objects to table 2
        table2.add(gravitySliderY).pad(4, 4, 4, 4);
        table2.add(gravitySliderX).pad(4, 0, 4, 4);
        table2.row();
        table2.add(labelSliderY).pad(0, 4, 4, 4);
        table2.add(labelSliderX).pad(0, 0, 4, 4);

        //format table 3 LAYOUT
        stage2.addActor(table3);
        table3.setFillParent(true);
        table3.align(Align.center);

        //table 3 buttons
        colourPalette = new ImageButton(skinCanvas.getDrawable("canvas"));
        backToMenu = new TextButton("Return to Game Menu", skin);
// SAMPLE FOR THE COLOUR. SO THE USER SEES IT BEFORE SWITCHING WINDOWS
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(40, 20, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skinColourSample.add("white", new Texture(pixmap));

//		// Store the default libgdx font under the name "default".
        skinColourSample.add("default", new BitmapFont());
        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle2 = new TextButtonStyle();
        textButtonStyle2.up = skinColourSample.newDrawable("white");
        textButtonStyle2.font = skinColourSample.getFont("default");
        skinColourSample.add("default", textButtonStyle2);

        sampleColourButton = new TextButton("", skinColourSample);
        // add the buttons to table 3
        table3.add(backToMenu).pad(4, 4, 4, 4);
        table3.row();
        table3.add(sampleColourButton).pad(0, 4, 4, 4);
        table3.row();
        table3.add(colourPalette);

        //add the button/slider inputs
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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        background.render(deltaTime);
        if (choosingColour) {
            stage2.act(deltaTime);
            stage2.draw();
        } else {
            stage.act(deltaTime);
            stage.draw();
        }
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

    //to avoid the clutter of code
    public void addInputs() {
        resetPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //if the player exists, reset the location of the player
                background.resetPlayer();
                background.update(Gdx.graphics.getDeltaTime());
            }
        });
        resetLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //if the player exists, reset the location of the player
                background.resetLevel();
            }
        });
        gravitySliderY.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                System.out.println(gravitySliderY.getVisualValue());
                background.world.setGravity(new Vector2(background.world.getGravity().x, gravitySliderY.getVisualValue()));
                labelSliderY.setText("Gravity y-dir: " + gravitySliderY.getVisualValue());
            }
        });
        gravitySliderX.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                System.out.println(gravitySliderX.getVisualValue());
                background.world.setGravity(new Vector2(gravitySliderX.getVisualValue(), background.world.getGravity().y));
                labelSliderX.setText("Gravity x-dir: " + gravitySliderX.getVisualValue());
            }
        });
        snapToGrid.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                if (snapToGrid.isChecked()) {
                    background.gridMode = true;
                } else if (!snapToGrid.isChecked()) {
                    background.gridMode = false;
                }
            }
        });
        colourPalette.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setColour((int) x, (int) y);
            }
        });
        colours.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                choosingColour = true;
                Gdx.input.setInputProcessor(im2);
                lastUsedMultiplexer = im2;
            }
        });
        backToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                choosingColour = false;
                Gdx.input.setInputProcessor(im);
                lastUsedMultiplexer = im;
            }
        });
        backToGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
            }
        });
    }

    public void setColour(int xPos, int yPos) {
        drawColour = new Color(canvas.getPixel(xPos, (canvas.getHeight() - yPos)));
        sampleColourButton.setColor(drawColour);
        background.drawColour = drawColour;
    }

    @Override
    public void processInput() {
//        if (!GameInputs.isKeyDown(GameInputs.Keys.TAB)) {
//            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
//        }
    }
}
