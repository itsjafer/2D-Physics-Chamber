/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.gamestate.MyScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.model.GameWorld;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Dmitry
 */
public class GameScreen extends MyScreen {

    OrthographicCamera camera;
    Viewport viewport;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    GameWorld world;
    ArrayList<Vector2> potentialPolygon;
    ArrayList<GridPoint2> gridLayout;
    float gridSize;
    // Whether the line being drawn is to be straightened
    boolean straight;
    // The angle multiple to which to snap
    final float SNAP_ANGLE = (float) Math.toRadians(45);
    // The position at which the next point should be added
    Vector2 mouseDrawPos;
    boolean rectangleMode = false;
    boolean snapToGrid;

    /**
     * Creates a UI object
     *
     * @param gameStateManager
     */
    public GameScreen(ScreenManager gameStateManager) {
        super(gameStateManager);
    }

    /**
     * Draws the game
     *
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {
        // updates the camera to the world space
        camera.update();
        // loads the batch and sets it to follow the camera's projection matrix
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        if (snapToGrid) {
            for (GridPoint2 point : gridLayout) {
                shapeRenderer.circle(point.x, point.y, 1);
            }
        }
        if (!potentialPolygon.isEmpty()) {
            if (rectangleMode) {
                drawRectangle();
            } else {
                drawPotentialPolygon();
            }
        }

        for (Polygon polygon : world.getPolygons()) {
            if (polygon.containsPoint(GameInputs.getMousePosition())) {
                System.out.println("Mouse is in a polygon");
                movePolygon(polygon);
                shapeRenderer.setColor(Color.GOLD);
            }
            Vector2[] shapeVertices = polygon.getVertices();
            for (int i = 0; i < shapeVertices.length; i++) {
                shapeRenderer.line(shapeVertices[i], shapeVertices[i + 1 == shapeVertices.length ? 0 : i + 1]);
            }
            shapeRenderer.setColor(Color.WHITE);
        }
        if (world.getPlayer() != null) {
            Player player = world.getPlayer();
            if (player.containsPoint(GameInputs.getMousePosition())) {
                shapeRenderer.setColor(Color.GOLD);
            }
            Vector2[] playerVertices = player.getVertices();
            for (int i = 0; i < playerVertices.length; i++) {
                shapeRenderer.line(playerVertices[i], playerVertices[i + 1 == playerVertices.length ? 0 : i + 1]);
            }
            shapeRenderer.setColor(Color.WHITE);

//            shapeRenderer.line(0, world.getPlayer().HIGHEST, MyGdxGame.WIDTH, world.getPlayer().HIGHEST);
        }
        shapeRenderer.end();
    }

    @Override
    public void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        world = new GameWorld();
        potentialPolygon = new ArrayList();
        gridLayout = new ArrayList();
        gridSize = 20;
        //spawn the grid
        for (int i = 0; i <= MyGdxGame.HEIGHT; i += gridSize) {
            for (int j = 0; j <= MyGdxGame.WIDTH; j += gridSize) {
                gridLayout.add(new GridPoint2(j, i));
            }
        }
        snapToGrid = false;
        straight = false;
        mouseDrawPos = new Vector2();
    }

    @Override
    public void update(float deltaTime) {
        processInput();
        //check to make sure the correct processor is in use
        if (Gdx.input.getInputProcessor() != MyGdxGame.gameInput) {
            Gdx.input.setInputProcessor(MyGdxGame.gameInput);
        }
        mouseDrawPos = GameInputs.getMousePosition();
        if (straight && !potentialPolygon.isEmpty()) {
            straightenMouse(potentialPolygon.get(potentialPolygon.size() - 1), mouseDrawPos);
        } else if (snapToGrid) {
            snapMouseToGrid(mouseDrawPos);
        }

        world.update(deltaTime);
    }

    @Override
    public void processInput() {

        if (world.getPlayer() != null) {
            if (GameInputs.isKeyDown(GameInputs.Keys.W)) {
                world.getPlayer().applyAcceleration(new Vector2(0, 1000));
            }
            if (GameInputs.isKeyDown(GameInputs.Keys.S)) {
                world.getPlayer().applyAcceleration(new Vector2(0, -1000));
            }
            if (GameInputs.isKeyDown(GameInputs.Keys.A)) {
                world.getPlayer().applyAcceleration(new Vector2(-1000, 0));
            }
            if (GameInputs.isKeyDown(GameInputs.Keys.D)) {
                world.getPlayer().applyAcceleration(new Vector2(1000, 0));
            }
        }
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.CTRL)) {
            rectangleMode = true;
        }
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.UP)) {
        }
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ESCAPE)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_MENU);
        }
        if (GameInputs.isKeyDown(GameInputs.Keys.TAB)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.GAME_MENU);
        }
        if (GameInputs.isMouseButtonJustPressed(GameInputs.MouseButtons.LEFT)) {

            if (rectangleMode && potentialPolygon.size() == 4) {
                rectangleMode = false;
            }
            // Loop preventing the user from adding dupliate points to the potential polygon
            // the click is assumed to be valid until a matching coordinate is found in the existing potential polygon
            boolean validPos = true;
            for (Vector2 vertex : potentialPolygon) {
                if (vertex.x == mouseDrawPos.x && vertex.y == mouseDrawPos.y) {
                    validPos = false;
                }
            }
            if (validPos) {
                // if the mouse click is added, simply add a new point to the potential polygon at the valid mouse position
                potentialPolygon.add(mouseDrawPos.cpy());
            }
        }
        // straight should only be true while the SHIFT key is being HELD DOWN
        straight = GameInputs.isKeyDown(GameInputs.Keys.SHIFT);

//            Vector2 newPoint = GameInputs.getMousePosition();
//
//            if (potentialPolygon.size() > 2 && !(new Polygon(potentialPolygon.toArray(new Vector2[potentialPolygon.size()])).containsPoint(newPoint))) {
//                if (GameInputs.isKeyDown(GameInputs.Keys.SHIFT)) {
//                    if (potentialPolygon.get(0).y != newPoint.y && !potentialPolygon.isEmpty()) {
//                        newPoint.y = potentialPolygon.get(0).y;
//                    }
//                }
//                potentialPolygon.add(newPoint);
//                world.setPotentialPolygon(potentialPolygon);
//            } else if (potentialPolygon.size() <= 2) {
//                if (GameInputs.isKeyDown(GameInputs.Keys.SHIFT)) {
//                    if (potentialPolygon.get(0).y != newPoint.y && !potentialPolygon.isEmpty()) {
//                        newPoint.y = potentialPolygon.get(0).y;
//                    }
//                }
//                potentialPolygon.add(newPoint);
//                world.setPotentialPolygon(potentialPolygon);
//            }
        if (GameInputs.isMouseButtonJustPressed(GameInputs.MouseButtons.RIGHT)) {
            if (!potentialPolygon.isEmpty()) {
                potentialPolygon.remove(potentialPolygon.size() - 1);
            }
        }

        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ENTER)) {
            if (potentialPolygon.size() > 2) {
                world.createPolygon(potentialPolygon);
                potentialPolygon.clear();
                System.out.println("Polygon made.");
            }
        }

        if (GameInputs.isKeyJustPressed(GameInputs.Keys.P)) {
            if (world.getPlayer() == null && potentialPolygon.size() > 2) {
                world.createPlayer(potentialPolygon);
                potentialPolygon.clear();
                System.out.println("Player made.");
            }
        }
    }

    public void movePolygon(Polygon movedPoly) {
    }

    /**
     * Snaps the mouse position drawing to the grid lines when grid lines are
     * activated
     *
     * @param curMousePos - position of current mouse
     */
    public void snapMouseToGrid(Vector2 curMousePos) {

        float xPos = gridSize * Math.round(curMousePos.x / gridSize);
        float yPos = gridSize * Math.round(curMousePos.y / gridSize);

        mouseDrawPos.set(xPos, yPos - 1);
    }

    /**
     * Rounds a specified angle up or down to the nearest specified multiple
     *
     * @param angle the angle to be adjusted
     * @param multiple the multiple to which the angle should be added
     * @return the angle rounded up or down to the nearest multiple
     */
    private float adjustAngle(float angle, float multiple) {
        return multiple * Math.round(angle / multiple);
    }

    /**
     * Reset player position and momentum
     */
    public void resetPlayer() {
        if (world.getPlayer() != null) {
            world.getPlayer().goHome();
        }
    }

    /**
     * Draws the potential polygon. It must be ensured that the potential
     * polygon size is greater than 0
     */
    public void drawPotentialPolygon() {

        // First loop through all the points except the last one (we don't want the polygon to wrap around
        for (int i = 0; i < potentialPolygon.size(); i++) {
            // Draw a dot at every point
            shapeRenderer.circle(potentialPolygon.get(i).x, potentialPolygon.get(i).y, 1);
            // Draw the outline of the polygon in red if it's valid (has at least 3 vertices
            if (potentialPolygon.size() >= 3) {
                shapeRenderer.setColor(Color.RED);
            }
            shapeRenderer.line(potentialPolygon.get(i), potentialPolygon.get(i + 1 == potentialPolygon.size() ? 0 : i + 1));

            // reset the color to white for the next loop of drawing points
            shapeRenderer.setColor((Color.WHITE));
        }

        // draw a line from the first point to the mouse (to complete the white outline
        shapeRenderer.line(potentialPolygon.get(0), mouseDrawPos);
        if (potentialPolygon.size() >= 2) // only draw a line from the last added polygon to the mouse if there's at least 2 points.. otherwise, it's just a waste of a line because the polygon is still a line if this condition is not met
        {
            shapeRenderer.line(potentialPolygon.get(potentialPolygon.size() - 1), mouseDrawPos);
        }
    }

    /**
     * Reset the level by deleting all polygons and sending the player to the
     * start position
     */
    public void resetLevel() {
        potentialPolygon.clear();
        world.getPolygons().clear();
        world.deletePlayer();
    }

    /**
     * Draws a based based on two points
     */
    public void drawRectangle() {
        System.out.println(potentialPolygon.size());
        //store the original point to begin the rectangle from
        Vector2 mouseClick = potentialPolygon.get(0);
        shapeRenderer.line(potentialPolygon.get(0), mouseDrawPos);

        //recreate the polygon using only the first vertice
        potentialPolygon.clear();
        potentialPolygon.add(mouseClick);

        //create the vertical and horizontal components of the rectangle
        Vector2 vertical = new Vector2(potentialPolygon.get(0).x, mouseDrawPos.y);
        Vector2 horizontal = new Vector2(mouseDrawPos.x, potentialPolygon.get(0).y);

        //add rectangle vertices to potentialPolygon 
        potentialPolygon.add(horizontal);
        potentialPolygon.add(mouseDrawPos);
        potentialPolygon.add(vertical);

        //draw the rectangle
        for (int i = 0; i < potentialPolygon.size(); i++) {
            // Draw a dot at every point
            shapeRenderer.circle(potentialPolygon.get(i).x, potentialPolygon.get(i).y, 1);
            // Draw the outline of the polygon in red if it's valid (has at least 3 vertices

            shapeRenderer.setColor(Color.BLUE);

            shapeRenderer.line(potentialPolygon.get(i), potentialPolygon.get(i + 1 == potentialPolygon.size() ? 0 : i + 1));

            // reset the color to white for the next loop of drawing points
            shapeRenderer.setColor((Color.WHITE));
        }

    }

    /**
     * Straightens the mouse for the snap to angle option
     *
     * @param point the point around which the mouse position should be adjusted
     * @param curMousePos the mouse's unadjusted position
     */
    public void straightenMouse(Vector2 point, Vector2 curMousePos) {
        // Gets the angle between the point and the mouse
        float angle = getAngle(point, curMousePos);
        // Adjusts the angle to the global snap angle
        angle = adjustAngle(angle, SNAP_ANGLE);
        // Take the "radius" between the two points
        Vector2 diff = curMousePos.cpy().sub(point);

        // Apply the new angle transformations on the mouse point
        float newX = point.x + diff.len() * (MathUtils.cos(angle));
        float newY = point.y + diff.len() * (MathUtils.sin(angle));
        // The mouse drawing position is now updated with the adjusted position
        mouseDrawPos.set(newX, newY);
    }

    /**
     * Finds the angle between two points relative to the x-axis
     *
     * @param origin the point around which the angle is to be measured
     * @param pos2 the point whose angle is to be measured relative to the
     * origin
     * @return the angle measured from pos2 to the origin point, relative to the
     * x-axis (horizontal)
     */
    public float getAngle(Vector2 origin, Vector2 pos2) {
        // Using the tangent in a right triangle:
        float xVal = pos2.x - origin.x;
        float yVal = pos2.y - origin.y;

        float theta = MathUtils.atan2(yVal, xVal);

        return theta;
    }

    @Override
    public void show() {
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

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        // test
        viewport.apply(true);
    }
}
