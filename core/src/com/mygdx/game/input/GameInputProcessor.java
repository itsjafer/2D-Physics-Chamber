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
                GameKeys.setKey(GameKeys.Keys.UP, true);
                break;
            case Input.Keys.ENTER:
                GameKeys.setKey(GameKeys.Keys.ENTER, true);
                break;
            case Input.Keys.ESCAPE:
                GameKeys.setKey(GameKeys.Keys.ESCAPE, true);
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
                GameKeys.setKey(GameKeys.Keys.UP, false);
                break;
            case Input.Keys.ENTER:
                GameKeys.setKey(GameKeys.Keys.ENTER, false);
                break;
            case Input.Keys.ESCAPE:
                GameKeys.setKey(GameKeys.Keys.ESCAPE, false);
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
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
