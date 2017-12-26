package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * @author brett
 * @since 12/25/2017
 */

public class Snake {
    public static final boolean DEBUG_MODE = false;

    private static final int INIT_MOVEMENT_SPEED = 100;

    private Vector3 m_position;
    private Vector3 m_velocity;
    private Rectangle m_bounds;

    //The snake (comprised of snake parts)
    private Array<SnakePart> m_snakeParts;

    //To render individual snake parts
    private ShapeRenderer m_shapeRenderer;

    public boolean m_colliding;

    enum Directions {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    //Keep track of the snake's current direction
    private Directions currentDirection = Directions.NONE;

    /**
     * Constructor
     * */
    public Snake(){
        //Create the initial snake
        m_snakeParts.add(new SnakePart());

        m_position = new Vector3(m_snakeParts.get(0).getX(), m_snakeParts.get(0).getY(), 0);
        m_velocity = new Vector3(0, 0, 0);

        m_shapeRenderer = new ShapeRenderer();

        m_colliding = false;
    }

    /**
     * If a valid direction is specified then change the direction that the snake is going in
     *
     * @param direction the direction to make the snake go in
     * */
    public void move(Directions direction){
        if(currentDirection != direction){
            //Move in the specified direction

            currentDirection = direction;
        }
    }

    public void dispose(){
        m_shapeRenderer.dispose();
    }

}
