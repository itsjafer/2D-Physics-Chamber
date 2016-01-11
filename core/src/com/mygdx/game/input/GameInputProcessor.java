/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;


/**
 * The source of the input for the game
 * @author kobed6328
 */
public class GameInputProcessor extends InputAdapter{

    @Override
    public boolean keyDown(int keyCode) {
        
        // If the user has pressed a key recognized by the game, the key's flag is updated
        switch (keyCode)
        {
            case Input.Keys.UP:
                GameInputs.setKey(GameInputs.Keys.UP, true);
                break;
            case Input.Keys.ENTER:
                GameInputs.setKey(GameInputs.Keys.ENTER, true);
                break;
            case Input.Keys.ESCAPE:
                GameInputs.setKey(GameInputs.Keys.ESCAPE, true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {
        
        // If the user has pressed a key recognized by the game, the key's flag is updated
        switch (keyCode)
        {
            case Input.Keys.UP:
                GameInputs.setKey(GameInputs.Keys.UP, false);
                break;
            case Input.Keys.ENTER:
                GameInputs.setKey(GameInputs.Keys.ENTER, false);
                break;
            case Input.Keys.ESCAPE:
                GameInputs.setKey(GameInputs.Keys.ESCAPE, false);
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
        switch (button)
        {
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
        switch (button)
        {
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
