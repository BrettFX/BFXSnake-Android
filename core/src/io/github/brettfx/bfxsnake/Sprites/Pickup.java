package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author brett
 * @since 12/25/2017
 *
 * Pick ups are what the snake is supposed to eat to grow
 * Each time a pick up is eaten by the snake, the snake will grow in size.
 * Once the snake has eaten all the pickups a special prize will be won.
 *
 * Java Random Utilization Reference: https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
 */
public class Pickup {
    public static boolean DEBUG_MODE = false;

    //Control the size of the pickup. The greater the number the larger and vice versa
    private static final float PICKUP_SIZE_FACTOR = 1.0f;

    //Control the number of random steps to be generated in accordance with pickup spawn
    //relative to snake head location
    private static final int NUM_RAND_STEPS = 100;

    private Rectangle m_food;
    private Rectangle m_bounds;

    private float m_xStart;
    private float m_yStart;

    public Pickup(SnakePart part){
        float width = part.getWidth();
        float height = part.getHeight();
        m_xStart = part.getXStart();
        m_yStart = part.getYStart();

        m_food = new Rectangle(m_xStart, m_yStart, width * PICKUP_SIZE_FACTOR, height * PICKUP_SIZE_FACTOR);
        m_bounds = new Rectangle(m_xStart, m_yStart, width * PICKUP_SIZE_FACTOR, height * PICKUP_SIZE_FACTOR);

        spawn(part);
    }

    /**
     * Collect the pickup when the snake has collided with it.
     * */
    public void collect(SnakePart part){
        spawn(part);
    }

    /**
     * Spawn a pickup based on a random path with respect to the current location of the
     * snake's head
     *
     * @param part the head of the snake
     * */
    private void spawn(SnakePart part){
        Snake.Directions directions[] = Snake.Directions.values();

        //Don't include NONE as a possible direction
        Snake.Directions randDirection = directions[(int)(Math.random() * (directions.length - 1))];

        Snake.Directions prevDirection = Snake.Directions.NONE;

        float x = m_xStart;
        float y = m_yStart;

        //Traverse random path relative to current location of snake head
        for(int i = 0; i < NUM_RAND_STEPS; i++){
            switch (randDirection){
                case UP:
                    if(y >= Gdx.graphics.getHeight() - part.getHeight()){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    if(isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    y = m_food.getY() + m_food.getHeight();
                    break;

                case DOWN:
                    if(y <= 0){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    if(isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    y = m_food.getY() - m_food.getHeight();
                    break;

                case LEFT:
                    if(x <= 0){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    if(isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    x = m_food.getX() - m_food.getWidth();
                    break;

                case RIGHT:
                    if(x >= Gdx.graphics.getWidth() - part.getWidth()){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    if(isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    x = m_food.getX() + m_food.getWidth();
                    break;

                default:
                    break;
            }

            //Set intermediate location of pickup and adjust bounds accordingly
            if(!isOpposite(prevDirection, randDirection)){
                m_food.setX(x);
                m_food.setY(y);
                m_bounds.setX(x);
                m_bounds.setY(y);

                prevDirection = randDirection;
                randDirection = directions[(int)(Math.random() * directions.length)];
            }else{
                i = i <= 0 ? -1 : i - 1;
            }
        }
    }

    /**
     * Determine if the direction the user is attempting to go in is
     * the opposite direction that they are currently going (not permitted)
     *
     * @param currentDirection the current direction to compare to
     * @param nextDirection the direction to compare with the current direction
     * */
    private boolean isOpposite(Snake.Directions currentDirection, Snake.Directions nextDirection){
        switch (nextDirection){
            case UP:
                return currentDirection == Snake.Directions.DOWN;

            case DOWN:
                return currentDirection == Snake.Directions.UP;

            case LEFT:
                return currentDirection == Snake.Directions.RIGHT;

            case RIGHT:
                return currentDirection == Snake.Directions.LEFT;

            default:
                return false;
        }
    }

    /**
     * Determine if the snake has collided with the pickup
     * */
    public boolean shouldCollect(SnakePart part){
        return part.getBounds().overlaps(m_bounds);
    }

    public float getX(){
        return m_bounds.getX();
    }

    public float getY(){
        return m_bounds.getY();
    }

    public float getWidth(){
        return m_bounds.getWidth();
    }

    public float getHeight(){
        return m_bounds.getHeight();
    }
}
