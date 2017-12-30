package io.github.brettfx.bfxsnake.Sprites;

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
    private static final float FACTOR = 1.0f;

    private Rectangle m_food;
    private Rectangle m_bounds;
    private float m_minHeight;
    private float m_maxHeight;
    private float m_minWidth;
    private float m_maxWidth;

    public Pickup(SnakePart part){
        float width = part.getWidth();
        float height = part.getHeight();

        m_maxHeight = part.getMaxHeight();
        m_maxWidth = part.getMaxWidth();
        m_minHeight = part.getMinHeight();
        m_minWidth = part.getMinWidth();

        float randX = (float)Math.random() * m_maxWidth + m_minWidth;
        float randY = (float)Math.random() * m_maxHeight + m_minHeight;

        //Attempt to align with snake
        randX -= (part.getX() - part.getX());
        randY -= (part.getY() - part.getY());

        m_food = new Rectangle(randX, randY, width * FACTOR, height * FACTOR);
        m_bounds = new Rectangle(randX, randY, width * FACTOR, height * FACTOR);
    }

    /**
     * Collect the pickup when the snake has collided with it.
     * */
    public void collect(SnakePart part){
        float randX = (float)Math.random() * m_maxWidth + m_minWidth;
        float randY = (float)Math.random() * m_maxHeight + m_minHeight;

        //Attempt to align with snake
        randX -= (part.getX() - part.getX());
        randY -= (part.getY() - part.getY());

        m_food.setX(randX);
        m_food.setY(randY);
        m_bounds.setX(randX);
        m_bounds.setY(randY);
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
