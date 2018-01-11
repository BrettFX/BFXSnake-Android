package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

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
    private static final int NUM_RAND_STEPS = 50;

    private Rectangle m_food;
    private Rectangle m_bounds;

    private Snake m_snake;

    public Pickup(SnakePart part, Snake snake){
        float x = part.getXStart();
        float y = part.getYStart();
        float width = part.getWidth();
        float height = part.getHeight();

        m_snake = snake;

        m_food = new Rectangle(x, y, width * PICKUP_SIZE_FACTOR, height * PICKUP_SIZE_FACTOR);
        m_bounds = new Rectangle(x, y, width * PICKUP_SIZE_FACTOR, height * PICKUP_SIZE_FACTOR);

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

        //Starting x and y coordinates are based on the current location of the part
        float x = part.getX();
        float y = part.getY();

        boolean outOfBounds;

        //Traverse random path relative to current location of snake head
        for(int i = 0; i < NUM_RAND_STEPS; i++){
            switch (randDirection){
                case UP:
                    outOfBounds = y >= m_snake.getMaxHeight();

                    if(outOfBounds || isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length - 1)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    y = m_food.getY() + m_food.getHeight();
                    break;

                case DOWN:
                    outOfBounds = y <= m_snake.getMinHeight();

                    if(outOfBounds || isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length - 1)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    y = m_food.getY() - m_food.getHeight();
                    break;

                case LEFT:
                    outOfBounds = x <= m_snake.getMinWidth();

                    if(outOfBounds || isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length - 1)];
                        i = i <= 0 ? -1 : i - 1;
                        continue;
                    }

                    x = m_food.getX() - m_food.getWidth();
                    break;

                case RIGHT:
                    outOfBounds = x >= m_snake.getMaxWidth();

                    if(outOfBounds || isOpposite(prevDirection, randDirection)){
                        randDirection = directions[(int)(Math.random() * directions.length - 1)];
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
                randDirection = directions[(int)(Math.random() * directions.length - 1)];
            }else{
                i = i <= 0 ? -1 : i - 1;
            }
        }
    }

    /**
     * Ensures that the currently spawned pickup is not overlapped by any part of the
     * snake.
     *
     * If there is one part that violates this constraint, a new pickup will be spawned.
     *
     * @param snakeParts the entire snake
     * */
    public void validateSpawn(Array<SnakePart> snakeParts){
        for(int i = 0; i < snakeParts.size; i++){
            if(snakeParts.get(i).getBounds().overlaps(m_food)){
                spawn(snakeParts.get(0));
                i = 0;
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
