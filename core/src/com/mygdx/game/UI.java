/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Dmitry
 */
public class UI {
    
    SpriteBatch batch;
//    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    Texture img;
    
    float[] shapeVertices;
    
    BitmapFont font;
    
    public UI()
    {
        create();
    }
    
    public void create()
    {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
//        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    public void render(World world)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        camera.update();
//        batch.setProjectionMatrix(camera.combined);
//        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.begin(ShapeType.Line);
        
        ArrayList<Polygon> collidedPolygons = world.collidePolygons();
        
        for (Polygon polygon: world.getPolygons())
        {
            shapeRenderer.setColor(Color.WHITE);
            if (collidedPolygons.contains(polygon))
                if (polygon == world.collidePoint(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())))
                {
                    shapeRenderer.setColor(Color.YELLOW);
                }
                else
                    shapeRenderer.setColor(Color.RED);
            else
                if (polygon == world.collidePoint(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())))
                    shapeRenderer.setColor(Color.GREEN);
            shapeVertices = new float[polygon.getVertices().length*2];
            for (int i = 0; i < shapeVertices.length-1; i += 2)
            {
                shapeVertices[i] = polygon.getVertices()[i/2].x;
                shapeVertices[i+1] = polygon.getVertices()[i/2].y;
            }
            shapeRenderer.polygon(shapeVertices);
        }
        
        shapeRenderer.setColor(Color.WHITE);
        for (int i = 0; i < world.potentialPolygon.size(); i ++)
        {
            shapeRenderer.point(world.potentialPolygon.get(i).x, world.potentialPolygon.get(i).y, 0);
            shapeRenderer.line(world.potentialPolygon.get(i), world.potentialPolygon.get(i+1==world.potentialPolygon.size() ? 0: i+1));
            
        }
        
        shapeRenderer.end();
        
        batch.begin();
//        for (Polygon polygon: world.getPolygons())
//        {
////            for (Vector2 vertex: polygon.getVertices())
////            {
////                font.draw(batch, vertex.toString(), vertex.x, vertex.y);
////            }
//            font.draw(batch, "" + polygon.getFriction(), polygon.getVertices()[0].x, polygon.getVertices()[0].y);
//        }
        batch.end();
        
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//            camera.position.x -= 20;
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//            camera.position.x += 20;
//        if (Gdx.input.isKeyPressed(Input.Keys.UP))
//            camera.position.y += 20;
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
//            camera.position.y -= 20;
    }
    
}
