package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * @author brett
 * @since 12/25/2017
 *
 * Pick ups are what the snake is supposed to eat to grow
 * Each time a pick up is eaten by the snake, the snake will grow in size.
 * Once the snake has eaten all the pickups a special prize will be won.
 */
public class Pickup {
    private Rectangle m_food;
    private Rectangle m_bounds;
    private Random m_rand;
    private float m_minHeight;
    private float m_maxHeight;
    private float m_minWidth;
    private float m_maxWidth;

    public Pickup(SnakePart part){
        float width = part.getWidth();
        float height = part.getHeight();

        m_maxHeight = Gdx.graphics.getHeight() - width;
        m_maxWidth = Gdx.graphics.getWidth() - height;
        m_minHeight = 0;
        m_minWidth = 0;

        m_rand = new Random();

        float randX = m_rand.nextInt((int)m_maxWidth) + m_minWidth;
        float randY = m_rand.nextInt((int)m_maxHeight) + m_minHeight;

        m_food = new Rectangle(randX, randY, width, height);
        m_bounds = new Rectangle(randX, randY, width, height);
    }

    /**
     * Collect the pickup when the snake has collided with it.
     * */
    public void collect(){
        float randX = m_rand.nextInt((int)m_maxWidth) + m_minWidth;
        float randY = m_rand.nextInt((int)m_maxHeight) + m_minHeight;

        m_food.setX(randX);
        m_food.setY(randY);
        m_bounds.setX(randX);
        m_bounds.setY(randY);
    }

    /**
     * Determine if the snake has collided with the pickup
     * */
    public boolean shouldCollect(SnakePart part){
        return m_bounds.getX() == part.getX() && m_bounds.getY() == part.getY();
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
