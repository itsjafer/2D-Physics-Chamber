/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameWorld {

    private ArrayList<Polygon> polygons;
    private Player player;
    private Vector2 gravity;
    private Polygon finish;

    public GameWorld() {
        polygons = new ArrayList();
        gravity = new Vector2(0, -30f);
    }

    public void update(float deltaTime) {

//        if (player != null && GameInputs.isKeyDown(GameInputs.Keys.UP)) {
//        if (player != null && GameInputs.isKeyJustPressed(GameInputs.Keys.UP)) {
        if (player != null) {
            player.applyAcceleration(gravity);
            player.move(deltaTime);
            if (!polygons.isEmpty()) {
                Vector2 collidingAxis = new Vector2();
                Vector2 prevCollidingAxis = null;;
                do {
                    prevCollidingAxis = collidingAxis.cpy();
                    collidingAxis = player.collideWithPolygons(polygons, prevCollidingAxis);
                } while (collidingAxis != null && collidingAxis.x != prevCollidingAxis.x && collidingAxis.y != prevCollidingAxis.y);
//                player.collideWithPolygons(polygons);
            }
            player.update();
        }
    }

    /**
     * Creates a polygon based on the vectors passed in
     *
     * @param polygon arraylist of vertices of the polygon
     */
    public void createPolygon(ArrayList<Vector2> polygon, Color colour) {
        polygons.add(new Polygon(polygon.toArray(new Vector2[polygon.size()]), colour));
    }

    /**
     * Sets the gravity of the world, including direction and magnitude
     *
     * @param gravity - direction and magnitude
     */
    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    /**
     * Get the gravity of the world, including direction and magnitude
     *
     * @return gravity - direction and magnitude
     */
    public Vector2 getGravity() {
        return this.gravity;
    }

    /**
     * Getter method for the polygons arraylist
     *
     * @return
     */
    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    /**
     * Creates the player based on the vectors passed in
     *
     * @param playerPolygon arraylist of vertices
     */
    public void createPlayer(ArrayList<Vector2> playerPolygon, Color colour) {
        player = new Player(playerPolygon.toArray(new Vector2[playerPolygon.size()]), colour);
    }

    /**
     * Getter method for the player polygon
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public void deletePlayer() {
        player = null;
    }

    /**
     * Saves the position of the polygons and the player
     */
    public void saveLevel() {
        try {
            //loading up the text file with the infomration
            BufferedWriter out = new BufferedWriter(new FileWriter("levels.txt"));

            
            //saving the gravity
            out.write("Gravity:");
            out.write("\n" + gravity.x);
            out.write("\n" + gravity.y + "\n");

            //Writing player information to file
            if (getPlayer() != null) {
                out.write("Player: " + "\n");
                Vector2[] playerVertices = player.getVertices();
                for (int x = 0; x < playerVertices.length; x++) {
                    out.write(playerVertices[x].x + "\n");
                    out.write(playerVertices[x].y + "\n");
                }
            }

            //Writing polygon information to file
            if (!polygons.isEmpty()) {
                for (Polygon polygon : polygons) {
                    out.write("\n" + "Polygon ");
                    out.write("\n" + polygon.getPolygonColour());
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
    public void loadLevel() {
        //delete any existing polygons from the level
        polygons.clear();

        //loading the file to be read from
        FileReader file = null;
        try {
            file = new FileReader("levels.txt");
        } catch (Exception e) {
            System.out.println(e);
        }
        Scanner input = new Scanner(file); //create a scanner out of the file that's been loaded in

        //setting the Gravity
        input.nextLine();
        setGravity(new Vector2(Float.parseFloat(input.nextLine()), Float.parseFloat(input.nextLine())));

        //reading the information for a player
        if (input.nextLine().contains("Player")) {
            ArrayList<Vector2> playerVertices = new ArrayList();
            String nextWord = input.next();

            //Add vertices until a Polygon or the end of the file is reached
            while (!nextWord.equals("Polygon") && !nextWord.equals("null")) {
                Vector2 tempVertice = new Vector2(Float.parseFloat(nextWord), Float.parseFloat(input.next()));
                System.out.println(tempVertice);
                playerVertices.add(tempVertice);
                nextWord = input.next();
            }
            //create the player
            createPlayer(playerVertices, Color.WHITE);
        }

        //if there is information about the polygons, use it to create them
        if (input.hasNext()) {
            input.nextLine();
            String nextWord = "";
            //Read information for every polygon within the text file
            do {
                nextWord = input.next();
                ArrayList<Vector2> polygonVertices = new ArrayList();
                while (!nextWord.equals("Polygon") && !nextWord.equals("end")) {
                    Vector2 tempVertice = new Vector2(Float.parseFloat(nextWord), input.nextFloat());
                    polygonVertices.add(tempVertice);
                    if (input.hasNext()) {
                        nextWord = input.next();
                    } else {
                        break;
                    }
                }
                createPolygon(polygonVertices, Color.WHITE);
            } while (nextWord.equals("Polygon"));
        }
    }

    public void createFinish(ArrayList<Vector2> vertices, Color colour) {
        finish = new Polygon(vertices.toArray(new Vector2[vertices.size()]), colour);
    }
}
