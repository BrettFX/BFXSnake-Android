package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Components.Score;
import io.github.brettfx.bfxsnake.Scenes.Controller;

/**
 * @author brett
 * @since 12/25/2017
 */
public class Snake {
    public static boolean DEBUG_MODE = false;

    //Increasing the value will slow the speed, i.e., the smaller the number the faster the snake will go
    private static final int INIT_MOVEMENT_SPEED = 25; //Default: 25 (Easy mode)

    //Delay between ticks to update snake
    private int m_delay;

    //The snake (comprised of snake parts)
    private Array<SnakePart> m_snakeParts;

    //To render individual snake parts
    private ShapeRenderer m_shapeRenderer;

    private boolean m_colliding;
    private boolean m_paused;

    private int m_difficultyVal;

    public enum Directions {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    //Keep track of the snake's current direction
    private Directions m_currentDirection = Directions.NONE;

    private Pickup m_pickup;

    private float m_maxHeight;
    private float m_maxWidth;
    private float m_minHeight;
    private float m_minWidth;

    private Score m_score;

    /**
     * Constructor
     * */
    public Snake(Controller controller){
        //Create the initial snake
        m_snakeParts = new Array<SnakePart>();
        m_snakeParts.add(new SnakePart());

        //Make the snake start going in a random direction
        m_currentDirection = getRandomDirection();

        m_snakeParts.get(0).setDirection(m_currentDirection);

        m_difficultyVal = 0;

        SnakePart head = m_snakeParts.get(0);

        m_maxHeight = Gdx.graphics.getHeight() - head.getHeight();
        m_maxWidth = Gdx.graphics.getWidth() - head.getWidth();
        m_minHeight = 0;
        m_minWidth = controller.isUsingController() ? controller.getWidth() + head.getWidth() : 0;

        //Pass head of snake to pickup for reference
        m_pickup = new Pickup(m_snakeParts.get(0), this);

        m_shapeRenderer = new ShapeRenderer();

        m_colliding = false;
        m_paused = false;
        m_delay = 0;

        m_score = new Score(BFXSnake.SCORE_COLOR);
    }

    public Score getScore(){
        return m_score;
    }

    public void setDifficultyVal(int difficultyVal){
        m_difficultyVal = difficultyVal;
    }

    /**
     * Determine if the direction the user is attempting to go in is
     * the opposite direction that they are currently going (not permitted)
     *
     * @param direction the direction to compare with the current direction
     * */
    public boolean isOpposite(Directions direction){
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

    /**
     * Need a parameterless version of setting the direction so that the user
     * cannot change the direction more than once before the snake has actually moved
     * in the direction that it was supposed to be moved in.
     *
     * Sets the direction of the entire snake to the direction of the the head.
     * */
    private void setDirection(){
        m_currentDirection = m_snakeParts.get(0).getDirection();
    }

    public void setDirection(Directions direction){
        if(!isOpposite(direction)){
            //m_currentDirection = direction;

            //Starts the cascading process
            m_snakeParts.get(0).setDirection(direction);
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
            SnakePart head = m_snakeParts.get(0);
            SnakePart part = m_snakeParts.get(i);

            x = part.getX();
            y = part.getY();

            switch (part.getDirection()){
                case UP:
                    if(y >= m_maxHeight){
                        m_colliding = true;
                        return;
                    }

                    y = part.getY() + part.getHeight();
                    break;

                case DOWN:
                    if(y <= m_minHeight){
                        m_colliding = true;
                        return;
                    }

                    y = part.getY() - part.getHeight();
                    break;

                case LEFT:
                    if(x <= m_minWidth){
                        m_colliding = true;
                        return;
                    }

                    x = part.getX() - part.getWidth();
                    break;

                case RIGHT:
                    if(x >= m_maxWidth){
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

            //Collect the pickup if the snake's head has collided with it
            if(m_pickup.shouldCollect(head)){
                m_pickup.collect(head);
                m_pickup.validateSpawn(m_snakeParts);
                grow();
            }

            //Traverse entire snake and update the position of each part accordingly
            m_snakeParts.get(i).setX(x);
            m_snakeParts.get(i).setY(y);
        }
    }

    /**
     * Determine if the snake has collided with anything
     * */
    public boolean isColliding(){
        return m_colliding;
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

        //Increment the current score
        m_score.add();
    }

    public Array<SnakePart> getSnake(){
        return m_snakeParts;
    }

    /**
     * Get the snake's pickup
     * */
    public Pickup getPickup(){
        return m_pickup;
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

    public float getMaxHeight() {
        return m_maxHeight;
    }

    public float getMaxWidth() {
        return m_maxWidth;
    }

    public float getMinHeight() {
        return m_minHeight;
    }

    public float getMinWidth() {
        return m_minWidth;
    }

    public static Directions getRandomDirection(){
        Snake.Directions directions[] = Snake.Directions.values();

        //Don't include NONE as a possible direction
        return directions[(int)(Math.random() * (directions.length - 1))];
    }

    /**
     * Updates location of snake
     * */
    public void update(float dt){
        if(DEBUG_MODE && m_delay > (INIT_MOVEMENT_SPEED - m_difficultyVal)){
            System.out.println("Tick time: " + dt +  "; delay count: " + m_delay);
        }

        //Update snake based on delay value
        if(m_delay > (INIT_MOVEMENT_SPEED - m_difficultyVal)){
            move();

            //Set the direction now that the snake has actually been moved
            setDirection();

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
