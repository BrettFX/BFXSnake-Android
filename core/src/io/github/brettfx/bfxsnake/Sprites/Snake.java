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

    private Array<SnakePart> m_snakeParts;

    //To render individual snake parts
    private ShapeRenderer m_shapeRenderer;

    public boolean m_colliding;

    /**
     * Constructor
     *
     * @param xLoc the x-coordinate location to spawn the snake
     * @param yLoc the y-coordinate location to spawn the snake
     * */
    public Snake(int xLoc, int yLoc){
        m_position = new Vector3(xLoc, yLoc, 0);
        m_velocity = new Vector3(0, 0, 0);



        m_colliding = false;
    }

}
