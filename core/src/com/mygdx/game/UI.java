/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 *
 * @author Dmitry
 */
public class UI {
    
    SpriteBatch batch;
    OrthographicCamera camera;
    Viewport viewport;
    
    public UI(float width, float height)
    {
        create(width, height);
    }
    
    public void create(float width, float height)
    {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        
        camera.position.x = Gdx.graphics.getWidth()/2;
        camera.position.y = Gdx.graphics.getHeight()/2;
        
    }
    
    public void render()
    {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.end();
////        shapeRenderer.setProjectionMatrix(camera.combined);
//        
//        shapeRenderer.begin(ShapeType.Line);
//        
//        ArrayList<Polygon> collidedPolygons = world.collidePolygons();
//        
//        for (Polygon polygon: world.getPolygons())
//        {
//            shapeRenderer.setColor(Color.WHITE);
//            if (collidedPolygons.contains(polygon))
//                if (polygon == world.collidePoint(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())))
//                {
//                    shapeRenderer.setColor(Color.YELLOW);
//                }
//                else
//                    shapeRenderer.setColor(Color.RED);
//            else
//                if (polygon == world.collidePoint(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())))
//                    shapeRenderer.setColor(Color.GREEN);
//            shapeVertices = new float[polygon.getVertices().length*2];
//            for (int i = 0; i < shapeVertices.length-1; i += 2)
//            {
//                shapeVertices[i] = polygon.getVertices()[i/2].x;
//                shapeVertices[i+1] = polygon.getVertices()[i/2].y;
//            }
//            shapeRenderer.polygon(shapeVertices);
//        }
//        
//        shapeRenderer.setColor(Color.WHITE);
//        for (int i = 0; i < world.potentialPolygon.size(); i ++)
//        {
//            shapeRenderer.point(world.potentialPolygon.get(i).x, world.potentialPolygon.get(i).y, 0);
//            shapeRenderer.line(world.potentialPolygon.get(i), world.potentialPolygon.get(i+1==world.potentialPolygon.size() ? 0: i+1));
//            
//        }
//        
//        shapeRenderer.end();
        
    }

    void resize(int width, int height) {
        viewport.update(width, height);
    }
    
}
