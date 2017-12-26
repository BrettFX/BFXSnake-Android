package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import io.github.brettfx.bfxsnake.BFXSnake;

/**
 * @author brett
 * @since 12/25/2017
 *
 * A snake is comprised of snake parts
 */
public class SnakePart {
    private Rectangle m_part;

    //Promotes versatility by enabling the data type of m_part to change to Texture or Image
    //Boundary of each snake part to detect collisions
    private Rectangle m_bounds;

    private float m_xLoc;
    private float m_yLoc;
    private float m_width;
    private float m_height;

    /**
     * Default constructor to be used at initial state of game
     * Makes it so the snake will spawn in the center of the screen
     * Dimensions of snake part depend on the width and height of screen and the
     * scale factor set in the BGXSnake class
     * */
    public SnakePart(){
        m_width = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;
        m_height = Gdx.graphics.getHeight() / BFXSnake.SCALE_FACTOR;
        m_xLoc = m_width / 2;
        m_yLoc = m_height / 2;
        m_part = new Rectangle(m_xLoc, m_yLoc, m_width, m_height);
        m_bounds = new Rectangle(m_xLoc, m_yLoc, m_width, m_height);

    }

    public SnakePart(float x, float y, float width, float height){
        m_part = new Rectangle(x, y, width, height);
        m_bounds = new Rectangle(x, y, width, height);
        m_xLoc = x;
        m_yLoc = y;
        m_width = width;
        m_height = height;
    }

    public Rectangle getPart(){
        return m_part;
    }

    public float getX() {
        return m_xLoc;
    }

    public void setX(float x){
        m_xLoc = x;
    }

    public float getY() {
        return m_yLoc;
    }

    public void setY(float y){
        m_yLoc = y;
    }

    public float getWidth() {
        return m_width;
    }

    public float getHeight() {
        return m_height;
    }
}
