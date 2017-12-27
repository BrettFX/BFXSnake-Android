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
    private static final boolean DEBUG_MODE = false;

    //Increasing the value will slow the speed, i.e., the smaller the number the faster the snake will go
    private static final int INIT_MOVEMENT_SPEED = 25;

    //Delay between ticks to update snake
    private int m_delay;

    private int m_currentCascadingIndex;

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
    private Directions m_currentDirection = Directions.RIGHT;

    /**
     * Constructor
     * */
    public Snake(){
        //Create the initial snake
        m_snakeParts = new Array<SnakePart>();
        m_snakeParts.add(new SnakePart());
        m_snakeParts.get(m_snakeParts.size - 1).setDirection(m_currentDirection);

        m_position = new Vector3(m_snakeParts.get(0).getX(), m_snakeParts.get(0).getY(), 0);
        m_velocity = new Vector3(0, 0, 0);

        m_shapeRenderer = new ShapeRenderer();

        m_colliding = false;
        m_delay = 0;
        m_currentCascadingIndex = 0;
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
                return m_currentDirection == Directions.DOWN;

            case DOWN:
                return m_currentDirection == Directions.UP;

            case LEFT:
                return m_currentDirection == Directions.RIGHT;

            case RIGHT:
                return m_currentDirection == Directions.LEFT;

            default:
                return false;
        }
    }

    public ShapeRenderer getShapeRenderer(){
        return m_shapeRenderer;
    }

    //TODO Fix bug that occurs when changing directions and the snake contains more than one part
    public void setDirection(Directions direction){
        if(!isOpposite(direction)){
            m_currentDirection = direction;
        }
    }

    /**
     * If a valid direction is specified then change the direction that the snake is going in
     *
     * */
    private void move(){
        float newX;
        float newY;

        for(int i = 0; i < m_snakeParts.size; i++){
            newX = m_snakeParts.get(i).getX();
            newY = m_snakeParts.get(i).getY();

            switch (m_snakeParts.get(i).getDirection()){
                case UP:
                    newY = m_snakeParts.get(i).getY() + m_snakeParts.get(i).getHeight();
                    break;

                case DOWN:
                    newY = m_snakeParts.get(i).getY() - m_snakeParts.get(i).getHeight();
                    break;

                case LEFT:
                    newX = m_snakeParts.get(i).getX() - m_snakeParts.get(i).getWidth();
                    break;

                case RIGHT:
                    newX = m_snakeParts.get(i).getX() + m_snakeParts.get(i).getWidth();
                    break;

                default:
                    break;
            }

            //Traverse entire snake and update the position of each part accordingly
            m_snakeParts.get(i).setX(newX);
            m_snakeParts.get(i).setY(newY);
        }
    }

    /**
     * Used when the direction of the snake has been changed. The new direction that the snake is
     * going in will be cascaded through the snake until each snake part is going in the same direction
     * as the head of the snake.
     *
     * @param direction the direction to make the current snake part go in based on current
     *                  direction.
     * @param i the current index of the snake part to move to avoid moving all
     *          snake parts simultaneously
     * */
    private void cascade(Directions direction, int i){
        m_snakeParts.get(i).setDirection(direction);
    }

    /**
     * Grow the snake by appending a snake part
     * Depends on the current direction of last snake part
     * */
    public void grow(){
        int tail = m_snakeParts.size - 1;
        SnakePart previousPart = m_snakeParts.get(tail);

        float width = previousPart.getWidth();
        float height = previousPart.getHeight();
        float x = previousPart.getX();
        float y = previousPart.getY();

        switch (previousPart.getDirection()) {
            case UP:
                y = previousPart.getY() - height;
                break;

            case DOWN:
                y = previousPart.getY() + height;
                break;

            case LEFT:
                x = previousPart.getX() + width;
                break;

            case RIGHT:
                x = previousPart.getX() - width;
                break;

            default:
                break;
        }

        m_snakeParts.add(new SnakePart(x, y, width, height));

        //Have the newly inserted snake part follow the previous part
        m_snakeParts.get(m_snakeParts.size - 1).setDirection(previousPart.getDirection());
    }

    public Array<SnakePart> getSnake(){
        return m_snakeParts;
    }

    /**
     * Updates location of snake
     * */
    public void update(float dt){
        if(DEBUG_MODE && m_delay > INIT_MOVEMENT_SPEED){
            System.out.println("Tick time: " + dt +  "; delay count: " + m_delay);
        }

        m_delay++;

        //Update snake based on delay value
        if(m_delay >= INIT_MOVEMENT_SPEED){
            if(m_currentCascadingIndex >= m_snakeParts.size){
                m_currentCascadingIndex = 0;
            }

            //Cascade new direction (if any)
            cascade(m_currentDirection, m_currentCascadingIndex);

            move();
            m_delay = 0;
            m_currentCascadingIndex++;
        }
    }

    public void dispose(){
        m_shapeRenderer.dispose();
    }

}
