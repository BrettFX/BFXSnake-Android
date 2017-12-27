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

    public enum Directions {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    //Keep track of the snake's current direction
    private Directions currentDirection = Directions.NONE;

    /**
     * Constructor
     * */
    public Snake(){
        //Create the initial snake
        m_snakeParts = new Array<SnakePart>();
        m_snakeParts.add(new SnakePart());

        m_position = new Vector3(m_snakeParts.get(0).getX(), m_snakeParts.get(0).getY(), 0);
        m_velocity = new Vector3(0, 0, 0);

        m_shapeRenderer = new ShapeRenderer();

        m_colliding = false;
    }

    /**
     * Determine if the direction the user is attempting to go in is
     * the opposite direction that they are currently going (not permitted)
     *
     * @param direction the direction to compare with the current direction
     * */
    private boolean isOpposite(Directions direction){
        switch (direction){
            case UP:
                return currentDirection == Directions.DOWN;

            case DOWN:
                return currentDirection == Directions.UP;

            case LEFT:
                return currentDirection == Directions.RIGHT;

            case RIGHT:
                return currentDirection == Directions.LEFT;

            default:
                return false;
        }
    }

    public ShapeRenderer getShapeRenderer(){
        return m_shapeRenderer;
    }

    /**
     * If a valid direction is specified then change the direction that the snake is going in
     *
     * @param direction the direction to make the snake go in
     * */
    public void move(Directions direction){
        //Can't go in the opposite direction
        if(!isOpposite(direction)){
            float newX = m_snakeParts.get(0).getX();
            float newY = m_snakeParts.get(0).getY();

            //Move in the specified direction
            currentDirection = direction;

            //TODO adapt to full snake (i.e., traverse entire snake and update loc of each part)
            switch (currentDirection){
                case UP:
                    newY = m_snakeParts.get(0).getY() + m_snakeParts.get(0).getHeight();
                    break;

                case DOWN:
                    newY = m_snakeParts.get(0).getY() - m_snakeParts.get(0).getHeight();
                    break;

                case LEFT:
                    newX = m_snakeParts.get(0).getX() - m_snakeParts.get(0).getWidth();
                    break;

                case RIGHT:
                    newX = m_snakeParts.get(0).getX() + m_snakeParts.get(0).getWidth();
                    break;

                default:
                   break;
            }

            //Traverse entire snake and update the position of each part accordingly
            m_snakeParts.get(0).setX(newX);
            m_snakeParts.get(0).setY(newY);
        }
    }

    /**
     * Grow the snake by appending a snake part
     * */
    public void grow(){

    }

    public Array<SnakePart> getSnake(){
        return m_snakeParts;
    }

    /**
     * Updates location of snake
     * */
    public void update(float dt){

    }

    public void dispose(){
        m_shapeRenderer.dispose();
    }

}
