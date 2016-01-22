/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.MyGdxGame;

/**
 * The source of the input for the game
 *
 * @author kobed6328
 */
public class InputProcessor extends InputAdapter {

    /**
     * Returns the corresponding key enum from GameInputs.Keys
     * @param keyCode the numerical value of the key from InptutAdapter
     * @return the corresponding key enum from GameInputs.Keys if it is found there; otherwise, return null
     */
    private GameInputs.Keys getKey(int keyCode)
    {
        GameInputs.Keys key = null;
        switch (keyCode) {
            case Input.Keys.ENTER:
                key = GameInputs.Keys.ENTER;
                break;
            case Input.Keys.ESCAPE:
                key = GameInputs.Keys.ESCAPE;
                break;
            case Input.Keys.P:
                key = GameInputs.Keys.P;
                break;
            case Input.Keys.TAB:
                key = GameInputs.Keys.TAB;
                break;
            case Input.Keys.W:
                key = GameInputs.Keys.W;
                break;
            case Input.Keys.A:
                key = GameInputs.Keys.A;
                break;
            case Input.Keys.S:
                key = GameInputs.Keys.S;
                break;
            case Input.Keys.D:
                key = GameInputs.Keys.D;
                break;
            case Input.Keys.CONTROL_RIGHT:
            case Input.Keys.CONTROL_LEFT:
                key = GameInputs.Keys.CTRL;
                break;
            case Input.Keys.SHIFT_RIGHT:
            case Input.Keys.SHIFT_LEFT:
                key = GameInputs.Keys.SHIFT;
                break;
        }
        
        return key;
    }
    
    /**
     * Returns the corresponding mouse button enum from GameInputs.MouseButtons
     * @param button the numerical value of the mouse button from InptutAdapter
     * @return the corresponding mouse button enum from GameInputs.MouseButtons if it is found there; otherwise, return null
     */
    private GameInputs.MouseButtons getButton(int button)
    {
        GameInputs.MouseButtons bttn = null;
        switch (button) {
            case 0:
                bttn = GameInputs.MouseButtons.LEFT;
                break;
            case 1:
                bttn = GameInputs.MouseButtons.RIGHT;
                break;
        }
        return bttn;
    }
    
    @Override
    public boolean keyDown(int keyCode) {
        // If the user has pressed a key recognized by the game, the key's flag is updated
        GameInputs.Keys key = getKey(keyCode);
        if (key != null)
        {
            GameInputs.setKey(key, true);
        }
        
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {

        // If the user has pressed a key recognized by the game, the key's flag is updated
        GameInputs.Keys key = getKey(keyCode);
        if (key != null)
        {
            GameInputs.setKey(key, false);
        }
        
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int i2, int button) {
        GameInputs.MouseButtons bttn = getButton(button);
        if (bttn != null)
        {
            GameInputs.setMouseButton(bttn, true);
        }
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int i2, int button) {
        GameInputs.MouseButtons bttn = getButton(button);
        if (bttn != null)
        {
            GameInputs.setMouseButton(bttn, false);
        }
        return true;
    }
    
    @Override
    public boolean touchDragged(int x, int y, int i2) {
        // don't need to set mouse bttn down because that's already handled in touchDown
        GameInputs.setMousePosition(x, MyGdxGame.HEIGHT-y);
        GameInputs.moveMouse();
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        GameInputs.setMousePosition(x, MyGdxGame.HEIGHT-y);
        GameInputs.moveMouse();
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
