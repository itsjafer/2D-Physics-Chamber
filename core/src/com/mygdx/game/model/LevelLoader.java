/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author haidj9901
 */
public class LevelLoader {

    GameWorld world;
    String[] slotNames;

    public LevelLoader() 
    {
       world = MyGdxGame.WORLD;
       slotNames = new String[3];
    }

    /**
     * Saves the position of the polygons and the player
     * @param index 
     * @param slotName
     */
    public void saveLevel(int index, String slotName) {
        try {
            //loading up the text file with the infomration
            BufferedWriter out = new BufferedWriter(new FileWriter("level" + index + ".txt"));
            out.write(slotName);
            //saving the gravity
            out.write("\nGravity:");
            out.write("\n" + world.getGravity().x);
            out.write("\n" + world.getGravity().y + "\n");

            //Writing player information to file
            if (world.getPlayer() != null) {
                out.write("Player: " + "\n");
                out.write(world.getPlayer().getPolygonColour().toString() + "\n");
                Vector2[] playerVertices = world.getPlayer().getVertices();
                for (int x = 0; x < playerVertices.length; x++) {
                    out.write(playerVertices[x].x + "\n");
                    out.write(playerVertices[x].y + "\n");
                }
            }

            //Writing polygon information to file
            if (!world.getPolygons().isEmpty()) {
                for (Polygon polygon : world.getPolygons()) {
                    out.write("\n" + "Polygon ");
                    out.write("\n" + polygon.getPolygonColour().toString());
                    for (Vector2 vertice : polygon.getVertices()) {
                        out.write("\n" + vertice.x);
                        out.write("\n" + vertice.y);
                    }
                }
                out.write("\nend");
            } else {
                out.write("\nnull");
            }
            out.close();
        } catch (IOException e) {
        }

    }

    /**
     * Loads the level based on the information in levels.txt
     */
    public void loadLevel(int index) {
        //delete any existing polygons from the level
        world.getPolygons().clear();

        //loading the file to be read from
        FileReader file = null;
        try {
            file = new FileReader("level" + index + ".txt");
        } catch (Exception e) {
            System.out.println(e);
        }
        Scanner input = new Scanner(file); //create a scanner out of the file that's been loaded in

        //setting the Gravity
        slotNames[index] = input.nextLine();
        input.nextLine();
        world.setGravity(new Vector2(Float.parseFloat(input.nextLine()), Float.parseFloat(input.nextLine())));

        String nextWord = ""; //temporary variable that stores the next string in the file

        //creating the player
        if (input.nextLine().contains("Player")) {
            Color playerColor = Color.valueOf(input.nextLine());
            ArrayList<Vector2> playerVertices = new ArrayList();
            nextWord = input.next();
            while (!nextWord.contains("Polygon") && !nextWord.contains("null")) {
                Vector2 tempVertice = new Vector2(Float.parseFloat(nextWord), input.nextFloat());
                playerVertices.add(tempVertice);
                nextWord = input.next();
            }
            world.createPlayer(playerVertices, playerColor);
            if (input.hasNext()) {
                input.nextLine();
            }
        } else {
            nextWord = input.nextLine();
            world.deletePlayer();
        }
        System.out.println(nextWord);
        //creating the polygons
        if (nextWord.contains("Polygon")) {
            while (true) {
                Color polygonColor = Color.valueOf(input.nextLine());

                ArrayList<Vector2> polygonVertices = new ArrayList();
                nextWord = input.next();
                while (!nextWord.contains("Polygon") && !nextWord.contains("end")) {
                    Vector2 tempVertice = new Vector2(Float.parseFloat(nextWord), input.nextFloat());
                    polygonVertices.add(tempVertice);
                    nextWord = input.next();
                }
                world.createPolygon(polygonVertices, polygonColor);
                if (nextWord.contains("end")) {
                    break;
                }
                input.nextLine();
            }
        }
    }
    
    public String getSlotName(int index)
    {
        return slotNames[index];
    }
}
