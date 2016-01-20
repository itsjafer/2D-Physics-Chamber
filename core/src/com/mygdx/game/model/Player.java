/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.model.Polygon.vectorProject;
import java.util.ArrayList;

/**
 *
 * @author branc2347
 */
public class Player extends Polygon {

    private Vector2 acceleration;
    private float friction = 0f;
    private float restitution = 0f;
    private boolean jumping = false;
    
    float delF;
    
    boolean collided;
    float collisionDepth;
    Vector2 collisionAxis;
    Vector2 movement;
    
    public float HIGHEST = Float.MIN_VALUE;

    public Player(Vector2[] vertices) {
        super(vertices);
        velocity = new Vector2();
        acceleration = new Vector2();
        
        for (Vector2 vertex: vertices)
        {
            if (vertex.y > HIGHEST)
            {
                HIGHEST = vertex.y;
            }
        }
        
        collided = false;
        collisionDepth = 0f;
        collisionAxis = new Vector2();
        movement = new Vector2();
    }

    public void jump() {
        jumping = true;
    }

    public void move(float deltaTime) {
        
//        if (Float.isNaN(velocity.x))
//            velocity.x = 0;
//        if (Float.isNaN(velocity.y))
//            velocity.y = 0;
        System.out.println("=========movement ==========");
        System.out.println("DeltaTime: " + Gdx.graphics.getDeltaTime());
        System.out.println("Initial velocity before accel: " + velocity);
        System.out.println("acceleration: " + acceleration);
//        Vector2 movement = velocity.cpy().add(acceleration.cpy().scl(0.5f));
        movement = (velocity.cpy().scl(deltaTime)).add(acceleration.cpy().scl(0.5f * deltaTime * deltaTime));
        velocity.add(acceleration.cpy().scl(deltaTime));
        System.out.println("final velocity after acceleration :" + velocity);
        System.out.println("MOVEMENT : " + movement);
//         Each vertex is moved by the velocity
        for (Vector2 vertex : vertices) {
            vertex.add(movement);
        }
        updateCenter();
        
        System.out.println("--------end--------------");
    }
    
    public void collidePhysics()
    {
        
        System.out.println("--------------START OF COLLISION ----------------------");
        System.out.println("Colliding axis: " + collisionAxis);
        System.out.println("Depth: " + collisionDepth);
        System.out.println("Initial velocity: " + velocity);
        Vector2 displacement = getNormal(collisionAxis).nor().scl(-collisionDepth);
        System.out.println("initial displacement: " + displacement);
        // complex stuff
        float displacementMag = displacement.cpy().dot(displacement)*movement.len()/(displacement.cpy().dot(movement));
        System.out.println("Displacement mag: " + displacementMag);
        displacement = movement.cpy().nor().scl(displacementMag);
        
        System.out.println("Displacement: " + displacement);
        bump(displacement);
        
//        float componentSpeed = 0f;
//        /// PARALLEL
//        Vector2 parallelComponent = vectorProject(velocity, collisionAxis);
//        System.out.println("initial parallel: " + parallelComponent);
//        System.out.println("accel: " + vectorProject(acceleration, collisionAxis));
//        System.out.println("parallel displacement: " + vectorProject(displacement, collisionAxis));
//        componentSpeed = (float)Math.sqrt(parallelComponent.len()*parallelComponent.len()-2*vectorProject(acceleration, collisionAxis).len()*vectorProject(displacement, collisionAxis).len());
//        parallelComponent.nor().scl(componentSpeed);
//        System.out.println("final parallel: " + parallelComponent);
//        
//        /// NORMAL
//        Vector2 normalComponent = vectorProject(velocity, getNormal(collisionAxis));
//        System.out.println("initial normal: " + normalComponent);
//        System.out.println("accel: " + vectorProject(acceleration, getNormal(collisionAxis)));
//        System.out.println("normal displacement: " + vectorProject(displacement, getNormal(collisionAxis)));
//        componentSpeed = (float)Math.sqrt(normalComponent.len()*normalComponent.len()-2*vectorProject(acceleration, getNormal(collisionAxis)).len()*vectorProject(displacement, getNormal(collisionAxis)).len());
//        normalComponent.nor().scl(componentSpeed);
//        System.out.println("final normal: " + normalComponent);
//        
//        velocity = parallelComponent.add(normalComponent);
//        
//        System.out.println("=============== END ======================");
//        
//        System.out.println("Final velocity: " + velocity);
    }
    /**
     * Resets the player's position to the initial creation position. Also
     * resets momentum
     */
    public void goHome() {
        bump(startPos.cpy().sub(center));
        velocity.set(new Vector2(0, 0));
    }

    

    public void update() {
        acceleration.set(Vector2.Zero);
        jumping = false;
    }

    public void applyAcceleration(Vector2 acceleration) {
        this.acceleration.add(acceleration);
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.add(velocity);
    }
    
    public void updateCollisionStatus(ArrayList<Polygon> polygons)
    {
         // The player's normals
        Vector2[] normals1 = getNormals();
        // The other polygon's normals
        Vector2[] normals2;
        // The player's projection
        Vector2 projection1;
        // The other polygon's projection
        Vector2 projection2;

        float collisionDepthLocal = Float.MAX_VALUE;
        Vector2 collisionAxisLocal = new Vector2();

        // Iterate through all the polygons and check for a collision on a 1 to 1 basis
        for (Polygon otherPoly : polygons) {
            collided = true;
            // Check all of the player's normals
            for (Vector2 normal : normals1) {
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);

                // If one normal doesn't contain a collision, then no collision occurs at all
                if (projection1.x >= projection2.y || projection1.y <= projection2.x) {
                    collided = false;
                    break;
                    // Execution below this point indicates an intersection has occured
                }
                // the intersection depth for the current intersection
                float intersection;

                // Check the two possible intersection lengths and pick the smaller one
                if (Math.abs(projection2.y - projection1.x) < Math.abs(projection2.x - projection1.y)) {
                    intersection = projection2.y - projection1.x;
                } else {
                    intersection = projection2.x - projection1.y;
                }

                // If the current intersection depth is smaller than the overall intersection depth, udpate the overall intersection depth and the overall intersection axis
                if (Math.abs(intersection) < Math.abs(collisionDepthLocal)) {
                    collisionDepthLocal = intersection;
                    collisionAxisLocal = getNormal(normal);
                }
            }

            // Check all of the other polygon's normals
            normals2 = otherPoly.getNormals();
            for (Vector2 normal : normals2) {
                projection1 = this.projectPolygon(normal);
                projection2 = otherPoly.projectPolygon(normal);

                // If one normal doesn't contain a collision, then no collision occurs at all
                if (projection1.x >= projection2.y || projection1.y <= projection2.x) {
                    collided = false;
                    break;
                    // Execution below this point indicates an intersection has occured
                }
                // the intersection depth for the current intersection
                float intersection;

                // Check the two possible intersection lengths and pick the smaller one
                if (Math.abs(projection2.y - projection1.x) < Math.abs(projection2.x - projection1.y)) {
                    intersection = projection2.y - projection1.x;
                } else {
                    intersection = projection2.x - projection1.y;
                }

                // If the current intersection depth is smaller than the overall intersection depth, udpate the overall intersection depth and the overall intersection axis
                if (Math.abs(intersection) < Math.abs(collisionDepthLocal)) {
                    collisionDepthLocal = intersection;
                    collisionAxisLocal = getNormal(normal);
                }
            }
        }
        System.out.println("COLL DEPTH CALCL THING: " + collisionDepthLocal);
        if (collided)
        {
            this.collisionDepth = collisionDepthLocal;
            this.collisionAxis = collisionAxisLocal;
            return;
        }
//        if (collisionDepth < Float.MAX_VALUE && collisionAxis.len() > 0)
//        {
//            collided = true;
//        }
    }

    public void collideWithPolygons(ArrayList<Polygon> polygons) {
        updateCollisionStatus(polygons);
        if (collided)
        {
            while (collided)
            {
                collidePhysics();
                updateCollisionStatus(polygons);
            }
//            Vector2 parallelComponent = vectorProject(velocity, collisionAxis);
//            parallelComponent.scl(1f-friction);
//            Vector2 normalComponent = vectorProject(velocity, getNormal(collisionAxis));
//            parallelComponent.scl(-restitution);
//            
//            velocity = parallelComponent.add(normalComponent);
        }
    }
}
