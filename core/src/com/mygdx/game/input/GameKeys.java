/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.input;

import java.util.HashMap;

/**
 * The collection of all the valid keys in the game and their states
 * @author Dmitry
 */
public class GameKeys {
    
    // The valid keys
    public static enum Keys{
        UP, ENTER, ESCAPE;
    }
    
    // boolean values corresponding to each key's pressed state
    private static HashMap<Keys, Boolean> keysDown;
    // boolean values corresponding to each key's long-pressed state
    private static HashMap<Keys, Boolean> keysHeldDown;
    
    /**
     * Initializes the key storage
     */
    static {
        
        // set each key state to false
        keysDown = new HashMap();
        for (Keys key: Keys.values())
        {
            keysDown.put(key, false);
        }
        // keysHeldDown should start as the same as the normal keysDown
        keysHeldDown = (HashMap<Keys, Boolean>)keysDown.clone();
    }
    
    /**
     * Updates each key's long-held down state
     */
    public static void update()
    {
        for (Keys key: Keys.values())
        {
            // If the key's value is true, it has now been held down for two cycles, therefore it can be considered to be held down
            keysHeldDown.put(key, keysDown.get(key));
        }
    }
    
    /**
     * Returns whether or not a key has just entered the pressed state
     * @param key the key to be checked
     * @return a boolean value corresponding to the key's just-pressed state
     */
    public static boolean isKeyJustPressed(Keys key)
    {
        // The key should be currently pressed (as the pressed state is updated before the long-pressed state)
        // The key's long-pressed state should not be true
        return keysDown.get(key) && !keysHeldDown.get(key);
    }
    /**
     * Returns whether or not a key is being pressed
     * @param key the key to be checked.
     * @return a boolean value corresponding to the key's pressed state
     */
    public static boolean isKeyDown(Keys key)
    {
        // It doesn't matter if the key has been held down for a long time, so just return the key's pressed state
        return keysDown.get(key);
    }
    
    /**
     * Updates a key's pressed state.
     * @param key the key to be updated.
     * @param state the key's new state.
     */
    public static void setKey(Keys key, boolean state)
    {
        // Only update the key's pressed state. The long-pressed state is updated next cycle
        keysDown.put(key, state);
    }
}
