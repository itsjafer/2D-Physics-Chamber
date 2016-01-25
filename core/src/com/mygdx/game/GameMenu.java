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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.gamescreen.MyScreen;
import com.mygdx.game.gamescreen.ScreenManager;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.model.GameWorld;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameMenu extends MyScreen {

    //essentials for Scene2d UI
    Skin defaultSkin, skinCanvas, skinColourSample;
    Stage stageMenu, stageColour;
    TextButton resetPlayer, resetLevel, colours, backToMenu, sampleColourButton, backToGame;
    Slider gravitySliderY, gravitySliderX, restitutionSlider, frictionSlider;
    Label labelGravityY, labelGravityX, labelRestitution, labelFriction;
    CheckBox snapToGrid, playerRotate;
    Color drawColour;
    ImageButton colourPalette;
    Pixmap canvas;
    //input and drawing
    InputMultiplexer im, im2, lastUsedMultiplexer;
    GameWorld world;
    GameScreen background;
    boolean choosingColour;

    /**
     * Creates instance of game menu, UI
     *
     * @param gameStateManager
     * @param background background to be rendered while in game menu (the game
     * itself)
     */
    public GameMenu(ScreenManager gameStateManager, GameScreen background) {
        super(gameStateManager);
        this.background = background;
    }

    @Override
    public void init() {

        //access to global world, used to set various global settings
        world = MyGdxGame.WORLD;

        //for all the tables that implement buttons/sliders. Everything runs
        //on the stageMenu
        stageMenu = new Stage();

        //stage for the colour palette
        stageColour = new Stage();

        //Input multiplexer, giving priority to stageMenu over gameinput
        im = new InputMultiplexer(this.stageMenu, MyGdxGame.gameInput);

        //same as above, but giving priority to stageMenu 2, which is colour choosing
        im2 = new InputMultiplexer(this.stageColour, MyGdxGame.gameInput);

        //in order to know which input multiplexer was used last (when switching screens)
        lastUsedMultiplexer = im;

        // set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(lastUsedMultiplexer);

        //when true, it renders stageColour, when false it renders stageMenu
        choosingColour = false;

        //initialize defaultSkin by imlpementing the json file that implements the atlas
        // the json file has the buttonstyle,etc already coded into it, only need to call the name to use it
        defaultSkin = new Skin(Gdx.files.internal("ui-data/uiskin.json"));

        //skin for the colour palette, that uses the atlas related to the canvas picture
        skinCanvas = new Skin(new TextureAtlas(Gdx.files.internal("ui-data/canvas.pack")));

        //skin for the colour sample button
        skinColourSample = new Skin();

        //create the canvas using a pixmap, in order to be able to get each pixel from the picture
        canvas = new Pixmap(Gdx.files.internal("ui-data/canvas.png"));

        // Create the tables:
        Table tableButtons = new Table(); //table for game menu buttons
        Table tableSliders = new Table(); //table for game menu sliders
        Table tableColour = new Table(); //table for choosing colour canvas

        //tableButtons LAYOUT. set table to fill stage, align table top left
        stageMenu.addActor(tableButtons);
        tableButtons.setFillParent(true);
        tableButtons.align(Align.topLeft);

        //create the buttons for tableButtons
        resetPlayer = new TextButton("Reset Player", defaultSkin, "default");
        resetLevel = new TextButton("Delete World", defaultSkin, "default");
        colours = new TextButton("Colours", defaultSkin, "default");
        backToGame = new TextButton("Return to Game", defaultSkin);
        snapToGrid = new CheckBox("Snap to grid", defaultSkin);
        playerRotate = new CheckBox("Player rotation", defaultSkin);

        //add objects to tableButtons
        tableButtons.add(resetPlayer).pad(4, 4, 4, 4);
        tableButtons.add(resetLevel).pad(4, 4, 4, 4);
        tableButtons.add(colours).pad(4, 4, 4, 8);
        tableButtons.add(backToGame).pad(4, 4, 4, 4);
        tableButtons.row();
        tableButtons.add(snapToGrid).pad(4, 4, 4, 4);
        tableButtons.add(playerRotate).pad(4, 0, 4, 4);

        //tableSliders layout, aligned to bottom left
        stageMenu.addActor(tableSliders);
        tableSliders.setFillParent(true);
        tableSliders.align(Align.bottomLeft);

        //create the objects for tableSliders
        gravitySliderY = new Slider(-1000f, 1000f, 20f, false, defaultSkin);
        gravitySliderX = new Slider(-1000f, 1000f, 20f, false, defaultSkin);
        restitutionSlider = new Slider(0, 1, 0.1f, false, defaultSkin);
        frictionSlider = new Slider(0, 1, 0.1f, false, defaultSkin);
        labelGravityY = new Label("Gravity y-dir: " + world.getGravity().y, defaultSkin);
        labelGravityX = new Label("Gravity x-dir: " + world.getGravity().x, defaultSkin);
        labelFriction = new Label("Friction: " + world.getFriction(), defaultSkin);
        labelRestitution = new Label("Restitution: " + world.getRestitution(), defaultSkin);

        //add objects to tableSliders
        tableSliders.add(gravitySliderY).pad(4, 4, 4, 4);
        tableSliders.add(gravitySliderX).pad(4, 0, 4, 4);
        tableSliders.add(restitutionSlider).pad(4, 0, 4, 4);
        tableSliders.add(frictionSlider).pad(4, 0, 4, 4);
        tableSliders.row();
        tableSliders.add(labelGravityY).pad(0, 4, 4, 4);
        tableSliders.add(labelGravityX).pad(0, 0, 4, 4);
        tableSliders.add(labelRestitution).pad(0, 0, 4, 4);
        tableSliders.add(labelFriction).pad(0, 0, 4, 4);

        //format tableColour layout, aligned to center
        stageColour.addActor(tableColour);
        tableColour.setFillParent(true);
        tableColour.align(Align.center);

        //tableColour buttons
        colourPalette = new ImageButton(skinCanvas.getDrawable("canvas"));
        backToMenu = new TextButton("Return to Game Menu", defaultSkin);

        // SAMPLE FOR THE COLOUR. SO THE USER SEES IT BEFORE SWITCHING WINDOWS
        // Generate a 40x20 white texture and store it in the defaultSkin named "white".
        Pixmap pixmap = new Pixmap(40, 20, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skinColourSample.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skinColourSample.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle2 = new TextButtonStyle();
        textButtonStyle2.up = skinColourSample.newDrawable("white");
        textButtonStyle2.font = skinColourSample.getFont("default");
        skinColourSample.add("default", textButtonStyle2);

        sampleColourButton = new TextButton("", skinColourSample);

        // add the buttons to tableColour
        tableColour.add(backToMenu).pad(4, 4, 4, 4);
        tableColour.row();
        tableColour.add(sampleColourButton).pad(0, 4, 4, 4);
        tableColour.row();
        tableColour.add(colourPalette);

        //add the inputs that need their listeners overwritten
        //(each button/slider has a default listener, but we sometimes want another instead)
        addInputs();
    }

    @Override
    public void update(float deltaTime) {

        //set the input processor to the last used processor if the screen is set to GameMenu
        if (Gdx.input.getInputProcessor() != lastUsedMultiplexer) {
            Gdx.input.setInputProcessor(lastUsedMultiplexer);
        }

        //this is in case the user loads another level, updates values on the sliders based on that
        updateValues();

        //process all the inputs
        processInput();

    }

    @Override
    public void resize(int width, int height) {
        stageMenu.getViewport().update(width, height, true);
    }

    /**
     * update gravity, friction and restitution based on the world values. This
     * is so that when the user loads, the values are updated
     */
    public void updateValues() {
        if (gravitySliderX.getValue() != world.getGravity().x) {
            gravitySliderX.setValue(MyGdxGame.WORLD.getGravity().x);
        }
        if (gravitySliderY.getValue() != world.getGravity().y) {
            gravitySliderY.setValue(MyGdxGame.WORLD.getGravity().y);
        }
        if (restitutionSlider.getValue() != world.getRestitution()) {
            restitutionSlider.setValue(world.getRestitution());
        }
        if (frictionSlider.getValue() != world.getFriction()) {
            frictionSlider.setValue(world.getFriction());
        }
    }

    @Override
    public void show() {
    }

    /**
     * Draw the in-game menu
     *
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {
        background.render(deltaTime);

        //specify which stage to render based on whether the user wants to 
        //choose colours
        if (choosingColour) {
            stageColour.act(deltaTime);
            stageColour.draw();
        } else {
            stageMenu.act(deltaTime);
            stageMenu.draw();
        }
    }

    /**
     * Process the user's inputs
     */
    @Override
    public void processInput() {
        //switch between gameScreen and gameMenu easily using TAB
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.TAB)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
        }

        //NOTE:
        // For all the buttons that needed to be clicked, button.isChecked()
        // had to be used, because button.isPressed was acting weird.
        // therefore, we had to manually uncheck the button to make it like a click...
        //reset player, then update the background to show that
        if (resetPlayer.isChecked()) {
            //if the player exists, reset the location of the player
            resetPlayer.setChecked(false);
            background.resetPlayer();
            background.update(Gdx.graphics.getDeltaTime());
        }

        //reset the level
        if (resetLevel.isChecked()) {
            resetLevel.setChecked(false);
            background.resetLevel();
        }

        //enable or disable snap to grid mode
        if (snapToGrid.isChecked()) {
            background.setGridMode(true);
        } else if (!snapToGrid.isChecked()){
            background.setGridMode(false);
        }

        //enable or disable player rotation
        if (playerRotate.isChecked()) {
            world.setRotatePlayer(true);
        } else if (!snapToGrid.isChecked()) {
            world.setRotatePlayer(false);
        }

        //go to colour stage, choose colour
        if (colours.isChecked()) {
            colours.setChecked(false);
            choosingColour = true;
            //switches game inputs to new stage
            Gdx.input.setInputProcessor(im2);
            lastUsedMultiplexer = im2;
        }

        //return back to game menu
        if (backToMenu.isChecked()) {
            backToMenu.setChecked(false);
            choosingColour = false;
            //switch inputs again
            Gdx.input.setInputProcessor(im);
            lastUsedMultiplexer = im;
        }

        //return back to game, similar to TAB
        if (backToGame.isChecked()) {
            backToGame.setChecked(false);
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
        }
    }

    public void addInputs() {

        //add a new listener to the slider. Update world setting based on what the user selects for sliders
        gravitySliderY.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                background.world.setGravity(new Vector2(background.world.getGravity().x, gravitySliderY.getVisualValue()));
                labelGravityY.setText("Gravity y-dir: " + gravitySliderY.getVisualValue());
            }
        });

        //add a new listener to the slider. Update world setting based on what the user selects for sliders
        gravitySliderX.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                background.world.setGravity(new Vector2(gravitySliderX.getVisualValue(), background.world.getGravity().y));
                labelGravityX.setText("Gravity x-dir: " + gravitySliderX.getVisualValue());
            }
        });

        //add a new listener to the colour palette. Update the colour used to draw based on user selection
        colourPalette.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setColour((int) x, (int) y);
            }
        });

        //add a new listener to the slider. Update world setting based on what the user selects for sliders
        restitutionSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                if (world.getPlayer() != null) {
                    world.setRestitution(restitutionSlider.getValue());
                    labelRestitution.setText("Restitution: " + restitutionSlider.getValue());
                } else {
                    labelRestitution.setText("Create player first.");
                }
            }
        });

        //add a new listener to the slider. Update world setting based on what the user selects for sliders
        frictionSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                if (world.getPlayer() != null) {
                    world.setFriction(frictionSlider.getVisualValue());
                    labelFriction.setText("Friction: " + frictionSlider.getValue());
                } else {
                    labelFriction.setText("Create player first.");
                }
            }
        });
    }

    /**
     * Set the colour based on the position of the mouse on the colour palette
     * uses the get pixel method, which returns rgb8888 values for a new colour
     *
     * @param xPos mouse x position (when dragging)
     * @param yPos mouse y position (when dragging)
     */
    public void setColour(int xPos, int yPos) {
        drawColour = new Color(canvas.getPixel(xPos, (canvas.getHeight() - yPos)));
        sampleColourButton.setColor(drawColour);
        background.drawColour = drawColour; // set the drawing colour in gamescreen
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
