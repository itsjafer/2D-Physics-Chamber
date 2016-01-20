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
 * @author DmitryJaferCaius
 */
public class GameScreen extends MyScreen {

    //initializing essential variables
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    protected GameWorld world;
    //initializing storage variables used to change appearance of the game
    private ArrayList<Vector2> potentialPolygon;
    private ArrayList<GridPoint2> gridLayout;
    private ArrayList<Polygon> lastPolygonMoved;
    private float gridSize;
    protected Color drawColour;
    private boolean finishMode;
    private final float SNAP_ANGLE = (float) Math.toRadians(45); // The angle multiple to which to snap
    //drawing variables
    private boolean validPos;
    private boolean clickedInsidePolygon;
    private Vector2 oldMousePos;
    private boolean straight; // Whether the line being drawn is to be straightened
    private Vector2 mouseDrawPos; // The position at which the next point should be added
    private boolean rectangleMode;
    protected boolean gridMode;

    /**
     * Creates a UI object
     *
     * @param gameStateManager
     */
    public GameScreen(ScreenManager gameStateManager) {
        super(gameStateManager);
    }

    @Override
    public void init() {
        //initializing all variables
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        world = new GameWorld();
        potentialPolygon = new ArrayList();
        gridLayout = new ArrayList();
        lastPolygonMoved = new ArrayList();
        gridSize = 20;
        //spawn the grid
        for (int i = 0; i <= MyGdxGame.HEIGHT; i += gridSize) {
            for (int j = 0; j <= MyGdxGame.WIDTH; j += gridSize) {
                gridLayout.add(new GridPoint2(j, i));
            }
        }
        drawColour = Color.NAVY;
        gridMode = false;
        straight = false;
        clickedInsidePolygon = false;
        oldMousePos = null;
        mouseDrawPos = new Vector2();
        rectangleMode = false;
        finishMode = false;
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

        //shapeRenderer initialization
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();

        //draws the grid points
        if (gridMode) {
            shapeRenderer.setColor(Color.WHITE);
            for (GridPoint2 point : gridLayout) {
                shapeRenderer.circle(point.x, point.y, 1);
            }
        }

        //draw any polygon in progress
        if (!potentialPolygon.isEmpty()) {
            if (rectangleMode) {
                drawRectangle();
            } else {
                drawPotentialPolygon();
            }
        }
        validPos = true;


        for (Polygon polygon : world.getPolygons()) {
            //Checking and verifying polygon dragging
            if (!lastPolygonMoved.contains(polygon)) {
                lastPolygonMoved.add(0, polygon);
            }

            //set polygon colour
            shapeRenderer.setColor(polygon.getPolygonColour());

            //checking if the mouse is on the polygon
            if (polygon.containsPoint(GameInputs.getMousePosition())) {
                validPos = false;
                shapeRenderer.setColor(Color.GOLD);
            }

            //drawing each polygon
            Vector2[] shapeVertices = polygon.getVertices();
            for (int i = 0; i < shapeVertices.length; i++) {
                shapeRenderer.line(shapeVertices[i], shapeVertices[i + 1 == shapeVertices.length ? 0 : i + 1]);
            }
        }
        //dealing with the player
        if (world.getPlayer() != null) {
            //initialize the player
            Player player = world.getPlayer();
            shapeRenderer.setColor(player.getPolygonColour());

            //change the colour based on if the mouse is in the player
            if (player.containsPoint(GameInputs.getMousePosition())) {
                shapeRenderer.setColor(Color.GOLD);
            }

            //render the player
            Vector2[] playerVertices = player.getVertices();
            for (int i = 0; i < playerVertices.length; i++) {
                shapeRenderer.line(playerVertices[i], playerVertices[i + 1 == playerVertices.length ? 0 : i + 1]);
            }
            shapeRenderer.setColor(Color.WHITE);
        }
        shapeRenderer.end();
    }

    @Override
    public void update(float deltaTime) {
        //check for any inputs
        processInput();

        //check to make sure the correct processor is in use
        if (Gdx.input.getInputProcessor() != MyGdxGame.gameInput) {
            Gdx.input.setInputProcessor(MyGdxGame.gameInput);
        }

        //update the mouse position
        mouseDrawPos = GameInputs.getMousePosition();

        //check if there are any special conditions for drawing a line
        if (straight && !potentialPolygon.isEmpty()) {
            straightenMouse(potentialPolygon.get(potentialPolygon.size() - 1), mouseDrawPos);
        } else if (gridMode) {
            snapMouseToGrid(mouseDrawPos);
        }

        world.update(deltaTime);
    }

    @Override
    /**
     * Processes any inputs taken by the game
     */
    public void processInput() {

        //player movement
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

        //holding down ctrl results in rectangle
        if (GameInputs.isKeyDown(GameInputs.Keys.CTRL)) {
            rectangleMode = true;
        }

        //pressing esc takes you to the main menu
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ESCAPE)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_MENU);
        }

        //holding tab will take you to the game menu
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.TAB)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.GAME_MENU);
        }

        //click the left mouse button
        if (GameInputs.isMouseButtonJustPressed(GameInputs.MouseButtons.LEFT)) {

            // Loop preventing the user from adding dupliate points to the potential polygon
            // the click is assumed to be valid until a matching coordinate is found in the existing potential polygon
            if (rectangleMode && potentialPolygon.size() == 4) {
                rectangleMode = false;
            }

            //loop through each vertex to check if it already exists
            for (Vector2 vertex : potentialPolygon) {
                if (vertex.x == mouseDrawPos.x && vertex.y == mouseDrawPos.y) {
                    validPos = false;
                }
            }

            //if the click is valid based on the previous test
            if (validPos) {
                // if the mouse click is added, simply add a new point to the potential polygon at the valid mouse position
                potentialPolygon.add(mouseDrawPos.cpy());

                //remove the point if it results in a concave polygon
                if (!isConvex(potentialPolygon)) {
                    potentialPolygon.remove(potentialPolygon.size() - 1);
                }
            } else {
                clickedInsidePolygon = true;
                oldMousePos = mouseDrawPos;
                for (Polygon polygon : lastPolygonMoved) {
                    if (polygon.containsPoint(mouseDrawPos)) {
                        lastPolygonMoved.remove(polygon);
                        lastPolygonMoved.add(0, polygon);
                        break;
                    }
                }
            }

        }
        //check if the left mouse button has been dragged
        if (GameInputs.isMouseDragged(GameInputs.MouseButtons.LEFT)) {
            if (clickedInsidePolygon && !lastPolygonMoved.isEmpty()) {
                Vector2 displacement = mouseDrawPos.cpy().sub(oldMousePos);
                lastPolygonMoved.get(0).bump(displacement);
                oldMousePos = mouseDrawPos;
            }
        }
        if (GameInputs.isMouseButtonJustReleased(GameInputs.MouseButtons.LEFT)) {
            clickedInsidePolygon = false;
        }

        // straight should only be true while the SHIFT key is being HELD DOWN
        straight = GameInputs.isKeyDown(GameInputs.Keys.SHIFT);

        //right-clicking removes the last vertice of the potential polygon
        if (GameInputs.isMouseButtonJustPressed(GameInputs.MouseButtons.RIGHT)) {
            if (!potentialPolygon.isEmpty()) {
                potentialPolygon.remove(potentialPolygon.size() - 1);
            }
        }

        //pressing enter instantiates a polygon
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ENTER)) {
            //assuming a valid polygon can be made
            if (potentialPolygon.size() > 2) {
                world.createPolygon(potentialPolygon, drawColour);
                lastPolygonMoved.add(0, world.getPolygons().get(world.getPolygons().size() - 1));
                potentialPolygon.clear();
            }
        }

        //pressing P results in the creation of a player
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.P)) {
            if (world.getPlayer() == null && potentialPolygon.size() > 2) {
                world.createPlayer(potentialPolygon, drawColour);
                potentialPolygon.clear();
            }
        }
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
            world.getPlayer().reset();
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
                shapeRenderer.setColor(drawColour);
            }
            shapeRenderer.line(potentialPolygon.get(i), potentialPolygon.get(i + 1 == potentialPolygon.size() ? 0 : i + 1));

            // reset the color to white for the next loop of drawing points
            shapeRenderer.setColor(drawColour);
        }
        potentialPolygon.add(mouseDrawPos.cpy());
        if (!isConvex(potentialPolygon)) {
            shapeRenderer.setColor(Color.RED);
        } else {
            shapeRenderer.setColor(Color.GREEN);
        }
        potentialPolygon.remove(potentialPolygon.size() - 1);
        potentialPolygon.trimToSize();
        // draw a line from the first point to the mouse (to complete the white outline
        shapeRenderer.line(potentialPolygon.get(0), mouseDrawPos);
        if (potentialPolygon.size() >= 2) // only draw a line from the last added polygon to the mouse if there's at least 2 points.. otherwise, it's just a waste of a line because the polygon is still a line if this condition is not met
        {
            shapeRenderer.line(potentialPolygon.get(potentialPolygon.size() - 1), mouseDrawPos);
        }

    }

    /**
     * Checks the concavity of a polygon
     *
     * @param potentialConvexPolygon an arraylist of vertices within the polygon
     * @return true if the polygon is convex
     */
    public boolean isConvex(ArrayList<Vector2> potentialConvexPolygon) {
        //the polygon cannot be concave with less than 3 vertices
        if (potentialConvexPolygon.size() < 3) {
            return true;
        }
        boolean sign = false;
        int n = potentialConvexPolygon.size();
        for (int i = 0; i < potentialConvexPolygon.size(); i++) {

            //use the gift-wrapping algorithm to determine if a polygon is complex
            double dx1 = potentialConvexPolygon.get((i + 2) % n).x - potentialConvexPolygon.get((i + 1) % n).x;
            double dy1 = potentialConvexPolygon.get((i + 2) % n).y - potentialConvexPolygon.get((i + 1) % n).y;
            double dx2 = potentialConvexPolygon.get(i).x - potentialConvexPolygon.get((i + 1) % n).x;
            double dy2 = potentialConvexPolygon.get(i).y - potentialConvexPolygon.get((i + 1) % n).y;

            //calculate the z-value of the crossproduct
            double zCrossProduct = dx1 * dy2 - dy1 * dx2;

            //return concavity based on the sign of the cross product
            if (zCrossProduct == 0) {
                return false;
            }
            if (i == 0) {
                sign = zCrossProduct > 0;
            } else {
                if (sign != (zCrossProduct > 0)) {
                    return false;
                }
            }
        }
        return true;
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
            // Draw the outline of the polygon in game colour if it's valid (has at least 3 vertices

            shapeRenderer.setColor(drawColour);

            shapeRenderer.line(potentialPolygon.get(i), potentialPolygon.get(i + 1 == potentialPolygon.size() ? 0 : i + 1));

            // reset the color to white for the next loop of drawing points
            shapeRenderer.setColor(drawColour);
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
