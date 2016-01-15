/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import java.util.HashMap;

/**
 * The collection of all the valid keys in the game and their states
 *
 * @author Dmitry
 */
public class GameInputs {

    // The valid keys
    public static enum Keys {

        UP, ENTER, ESCAPE, P, TAB, W, A, S, D, CTRL, SHIFT;
    }

    public static enum MouseButtons {

        LEFT, RIGHT;
    }
    private static Vector2 mousePosition;
    // boolean values corresponding to each key's pressed state
    private static HashMap<Keys, Boolean> keysDown;
    // boolean values corresponding to each key's long-pressed state
    private static HashMap<Keys, Boolean> keysHeldDown;
    // boolean values corresponding to each mouse button's pressed state
    private static HashMap<MouseButtons, Boolean> mouseButtonsDown;
    // boolean values corresponding to each mouse button's long-pressed state
    private static HashMap<MouseButtons, Boolean> mouseButtonsHeldDown;

    /**
     * Initializes the key and mouse storage
     */
    static {
        mousePosition = new Vector2();
        // set each key state to false
        keysDown = new HashMap();
        for (Keys key : Keys.values()) {
            keysDown.put(key, false);
        }
        // keysHeldDown should start as the same as the normal keysDown
        keysHeldDown = (HashMap<Keys, Boolean>) keysDown.clone();

        // set each mouse button to false
        mouseButtonsDown = new HashMap();
        for (MouseButtons mouseButton : MouseButtons.values()) {
            mouseButtonsDown.put(mouseButton, false);
        }
        // mouseButtonsDown should start as the same as the normal mouseButtonsDown
        mouseButtonsHeldDown = (HashMap<MouseButtons, Boolean>) mouseButtonsDown.clone();
    }

    /**
     * Updates each key's and mouse button's long-held down state
     */
    public static void update() {
        for (Keys key : Keys.values()) {
            // If the key's value is true, it has now been held down for two cycles, therefore it can be considered to be held down
            keysHeldDown.put(key, keysDown.get(key));
        }
        for (MouseButtons mouseButton : MouseButtons.values()) {
            // If the mouse button's value is true, it has now been held down for two cycles, therefore it can be considered to be held down
            mouseButtonsHeldDown.put(mouseButton, mouseButtonsDown.get(mouseButton));
        }

        mousePosition.set(Gdx.input.getX(), MyGdxGame.HEIGHT - Gdx.input.getY());
    }

    /**
     * Returns whether or not a key has just entered the pressed state
     *
     * @param key the key to be checked
     * @return a boolean value corresponding to the key's just-pressed state
     */
    public static boolean isKeyJustPressed(Keys key) {
        // The key should be currently pressed (as the pressed state is updated before the long-pressed state)
        // The key's long-pressed state should not be true
        return keysDown.get(key) && !keysHeldDown.get(key);
    }

    /**
     * Returns whether or not a key is being pressed
     *
     * @param key the key to be checked.
     * @return a boolean value corresponding to the key's pressed state
     */
    public static boolean isKeyDown(Keys key) {
        // It doesn't matter if the key has been held down for a long time, so just return the key's pressed state
        return keysDown.get(key);
    }

    /**
     * Updates a key's pressed state.
     *
     * @param key the key to be updated.
     * @param state the key's new state.
     */
    public static void setKey(Keys key, boolean state) {
        // Only update the key's pressed state. The long-pressed state is updated next cycle
        keysDown.put(key, state);
    }

    /**
     * Returns whether or not a mouse button has just entered the pressed state
     *
     * @param button the mouse button to be checked
     * @return a boolean value corresponding to the mouse button's just-pressed
     * state
     */
    public static boolean isMouseButtonJustPressed(MouseButtons button) {
        // The mouse button should be currently pressed (as the pressed state is updated before the long-pressed state)
        // The button's long-pressed state should not be true
        return mouseButtonsDown.get(button) && !mouseButtonsHeldDown.get(button);
    }

    /**
     * Returns whether or not a mouse button is being pressed
     *
     * @param button the mouse button to be checked.
     * @return a boolean value corresponding to the mouse button's pressed state
     */
    public static boolean isMouseButtonDown(MouseButtons button) {
        // It doesn't matter if the button has been held down for a long time, so just return the button's pressed state
        return mouseButtonsDown.get(button);
    }

    /**
     * Updates a mouse button's pressed state.
     *
     * @param button the mouse button to be updated.
     * @param state the mouse button's new state.
     */
    public static void setMouseButton(MouseButtons button, boolean state) {
        // Only update the button's pressed state. The long-pressed state is updated next cycle
        mouseButtonsDown.put(button, state);
    }

    public static Vector2 getMousePosition() {
        return mousePosition;
    }
}
