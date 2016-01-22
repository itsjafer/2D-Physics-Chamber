/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author Dmitry, Jafer, Caius
 */
public class GameWorld {

    private ArrayList<Polygon> polygons;
    private Player player;
    private Vector2 gravity;
    private Vector2 horizontalMovementAxis;
    static public Vector2 verticalMovementAxis;
    private Polygon finish;
    
    public boolean playerJump = false;
    
    public GameWorld() {
        polygons = new ArrayList();
        setGravity(new Vector2(0, -30));
    }

    public void update(float deltaTime) {

//        if (player != null && GameInputs.isKeyDown(GameInputs.Keys.UP)) {
//        if (player != null && GameInputs.isKeyJustPressed(GameInputs.Keys.UP)) {
        if (player != null) {
            player.applyAcceleration(gravity);
            if (playerJump)
            {
                System.out.println("hHI");
                jumpPlayer();
                playerJump = false;
            }
            player.move(deltaTime);
            if (!polygons.isEmpty()) {
                player.collideWithPolygons(polygons);
            }
            player.update();
        }
    }

    public void movePlayerRight() {
        if (Polygon.scalarProject(player.getVelocity(), horizontalMovementAxis) >= 70)
            return;
        Vector2 accel = horizontalMovementAxis.cpy().scl(70).sub(Polygon.vectorProject(player.getVelocity(), horizontalMovementAxis)).scl(1f/Gdx.graphics.getDeltaTime());
        player.applyAcceleration(accel);
    }

    public void movePlayerLeft() {
        if (Polygon.scalarProject(player.getVelocity(), horizontalMovementAxis) <= -70)
            return;
        Vector2 accel = horizontalMovementAxis.cpy().scl(-70).sub(Polygon.vectorProject(player.getVelocity(), horizontalMovementAxis)).scl(1f/Gdx.graphics.getDeltaTime());
        player.applyAcceleration(accel);
    }

    public void jumpPlayer() {
        if (player.onGround()) {
            Vector2 vi = verticalMovementAxis.cpy().scl((float)Math.sqrt(-2*Polygon.scalarProject(gravity, verticalMovementAxis)*verticalMovementAxis.cpy().scl(100).len()));
            Vector2 accel = vi.sub(Polygon.vectorProject(player.getVelocity(), verticalMovementAxis)).scl(1f/Gdx.graphics.getDeltaTime());
            player.applyAcceleration(accel);
//            player.move(Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Creates a polygon based on the vectors passed in
     *
     * @param polygon list of vertices of the polygon
     */
    public void createPolygon(ArrayList<Vector2> polygon, Color colour) {
        polygons.add(new Polygon(polygon.toArray(new Vector2[polygon.size()]), colour));
    }

    /**
     * Updates the gravity and horizontal/vertical direction vectors
     * @param gravity the vector
     */
    public void setGravity(Vector2 gravity) {
        // update gravity
        this.gravity = gravity;
        // perpendicular to gravity
        horizontalMovementAxis = Polygon.getNormal(gravity).nor();
        // parallel but opposite in dir to gravity
        verticalMovementAxis = gravity.cpy().nor().scl(-1);
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

    public void createFinish(ArrayList<Vector2> vertices, Color colour) {
        finish = new Polygon(vertices.toArray(new Vector2[vertices.size()]), colour);
    }
}
