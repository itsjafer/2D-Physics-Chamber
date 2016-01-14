/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * The source of the input for the game
 *
 * @author kobed6328
 */
public class InputProcessor extends InputAdapter {

    @Override
    public boolean keyDown(int keyCode) {

        // If the user has pressed a key recognized by the game, the key's flag is updated
        switch (keyCode) {
            case Input.Keys.UP:
                GameInputs.setKey(GameInputs.Keys.UP, true);
                break;
            case Input.Keys.ENTER:
                GameInputs.setKey(GameInputs.Keys.ENTER, true);
                break;
            case Input.Keys.ESCAPE:
                GameInputs.setKey(GameInputs.Keys.ESCAPE, true);
                break;
            case Input.Keys.P:
                GameInputs.setKey(GameInputs.Keys.P, true);
                break;
            case Input.Keys.TAB:
                GameInputs.setKey(GameInputs.Keys.TAB, true);
                break;
            case Input.Keys.W:
                GameInputs.setKey(GameInputs.Keys.W, true);
                break;
            case Input.Keys.A:
                GameInputs.setKey(GameInputs.Keys.A, true);
                break;
            case Input.Keys.S:
                GameInputs.setKey(GameInputs.Keys.S, true);
                break;
            case Input.Keys.D:
                GameInputs.setKey(GameInputs.Keys.D, true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {

        // If the user has pressed a key recognized by the game, the key's flag is updated
        switch (keyCode) {
            case Input.Keys.UP:
                GameInputs.setKey(GameInputs.Keys.UP, false);
                break;
            case Input.Keys.ENTER:
                GameInputs.setKey(GameInputs.Keys.ENTER, false);
                break;
            case Input.Keys.ESCAPE:
                GameInputs.setKey(GameInputs.Keys.ESCAPE, false);
                break;
            case Input.Keys.P:
                GameInputs.setKey(GameInputs.Keys.P, false);
                break;
            case Input.Keys.TAB:
                GameInputs.setKey(GameInputs.Keys.TAB, false);
                break;
            case Input.Keys.W:
                GameInputs.setKey(GameInputs.Keys.W, false);
                break;
            case Input.Keys.A:
                GameInputs.setKey(GameInputs.Keys.A, false);
                break;
            case Input.Keys.S:
                GameInputs.setKey(GameInputs.Keys.S, false);
                break;
            case Input.Keys.D:
                GameInputs.setKey(GameInputs.Keys.D, false);
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int button) {
        switch (button) {
            case 0:
                GameInputs.setMouseButton(GameInputs.MouseButtons.LEFT, true);
                break;
            case 1:
                GameInputs.setMouseButton(GameInputs.MouseButtons.RIGHT, true);
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int button) {
        switch (button) {
            case 0:
                GameInputs.setMouseButton(GameInputs.MouseButtons.LEFT, false);
                break;
            case 1:
                GameInputs.setMouseButton(GameInputs.MouseButtons.RIGHT, false);
                break;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
