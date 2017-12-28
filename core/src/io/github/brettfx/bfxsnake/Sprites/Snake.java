package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * @author brett
 * @since 12/25/2017
 */

public class Snake {
    public static boolean DEBUG_MODE = false;

    //Increasing the value will slow the speed, i.e., the smaller the number the faster the snake will go
    private static final int INIT_MOVEMENT_SPEED = 10; //Default: 25

    //Delay between ticks to update snake
    private int m_delay;

    private Vector3 m_position;
    private Vector3 m_velocity;
    private Rectangle m_bounds;

    //The snake (comprised of snake parts)
    private Array<SnakePart> m_snakeParts;

    //To render individual snake parts
    private ShapeRenderer m_shapeRenderer;

    public boolean m_colliding;
    private boolean m_paused;

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
        m_paused = false;
        m_delay = 0;
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

            //Starts the cascading process
            m_snakeParts.get(0).setDirection(m_currentDirection);
        }
    }

    /**
     * If a valid direction is specified then change the direction that the snake is going in
     *
     * */
    private void move(){
        float x;
        float y;

        //Don't update movement if paused
        if(m_paused || m_colliding){
            return;
        }

        for(int i = 0; i < m_snakeParts.size; i++){
            SnakePart part = m_snakeParts.get(i);
            x = part.getX();
            y = part.getY();

            switch (part.getDirection()){
                case UP:
                    if(y >= Gdx.graphics.getHeight() - part.getHeight()){
                        m_colliding = true;
                        return;
                    }

                    y = part.getY() + part.getHeight();
                    break;

                case DOWN:
                    if(y <= 0){
                        m_colliding = true;
                        return;
                    }

                    y = part.getY() - part.getHeight();
                    break;

                case LEFT:
                    if(x <= 0){
                        m_colliding = true;
                        return;
                    }

                    x = part.getX() - part.getWidth();
                    break;

                case RIGHT:
                    if(x >= Gdx.graphics.getWidth() - part.getWidth()){
                        m_colliding = true;
                        return;
                    }

                    x = part.getX() + part.getWidth();
                    break;

                default:
                    break;
            }

            m_colliding = i != 0 && y == m_snakeParts.get(0).getY() && x == m_snakeParts.get(0).getX();

            if(m_colliding){
                return;
            }

            //Traverse entire snake and update the position of each part accordingly
            m_snakeParts.get(i).setX(x);
            m_snakeParts.get(i).setY(y);
        }
    }

    /**
     * Used when the direction of the snake has been changed. The new direction that the snake is
     * going in will be cascaded through the snake until each snake part is going in the same direction
     * as the head of the snake.
     * */
    private void cascade(){
        //Only cascade if not paused
        if(m_paused || m_colliding){
            return;
        }

        //Start from the end to ensure that only one is updated at a time
        for(int i = m_snakeParts.size - 1; i > 0; i--){
            SnakePart previousPart = m_snakeParts.get(i - 1);
            m_snakeParts.get(i).setDirection(previousPart.getDirection());
        }
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
     * Pause the snake
     * */
    public void pause(){
        m_paused = true;
    }

    /**
     * Resume the snake from a paused state
     * */
    public void resume(){
        m_paused = false;
    }

    /**
     * Determine if the snake is paused
     * */
    public boolean isPaused(){
        return m_paused;
    }

    /**
     * Updates location of snake
     * */
    public void update(float dt){
        if(DEBUG_MODE && m_delay > INIT_MOVEMENT_SPEED){
            System.out.println("Tick time: " + dt +  "; delay count: " + m_delay);
        }

        //Update snake based on delay value
        if(m_delay > INIT_MOVEMENT_SPEED){
            move();

            //Cascade new direction (if any)
            //Only required when there is more than one snake part
            if(m_snakeParts.size > 1){
                cascade();
            }

            m_delay = 0;
        }

        m_delay++;
    }

    public void dispose(){
        m_shapeRenderer.dispose();
    }
}
