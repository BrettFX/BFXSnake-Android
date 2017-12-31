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
    public static boolean DEBUG_MODE = false;

    private Rectangle m_food;
    private Rectangle m_bounds;
    private Random m_rand;

    private int m_minHeight;
    private int m_maxHeight;
    private int m_minWidth;
    private int m_maxWidth;

    public Pickup(SnakePart part){
        int width = part.getWidth();
        int height = part.getHeight();

        m_maxHeight = Gdx.graphics.getHeight() - height;
        m_maxWidth = Gdx.graphics.getWidth() - width;
        m_minHeight = height;
        m_minWidth = width;

        m_rand = new Random();

        m_food = new Rectangle(0, 0, width, height);
        m_bounds = new Rectangle(0, 0, width, height);

        int randX = m_rand.nextInt(m_maxWidth) + m_minWidth;
        int randY = m_rand.nextInt(m_maxHeight) + m_minHeight;

        align(part, randX, randY);
    }

    /**
     * Process the location of the randomly spawned pickup by means of
     * ensuring it will be in alignment with the snake's head
     *
     * @param part the current snake head
     * */
    private void align(SnakePart part, int randX, int randY){
//        while(true){
//            if(randX == 0){
//                randX = m_rand.nextInt(m_maxWidth) + m_minWidth;
//                continue;
//            }
//
//            if((part.getX() % randX) != 0){
//                randX = m_rand.nextInt(m_maxWidth) + m_minWidth;
//                continue;
//            }
//
//            if(randY == 0){
//                randY = m_rand.nextInt(m_maxHeight) + m_minHeight;
//                continue;
//            }
//
//            if((part.getY() % randY) != 0){
//                randY = m_rand.nextInt(m_maxHeight) + m_minHeight;
//                continue;
//            }
//
//            break;
//        }

        boolean xGreater = randX > part.getX();
        boolean yGreater = randY > part.getY();

        int xOffset = randX % part.getWidth();
        int yOffset = randY % part.getHeight();

        if(DEBUG_MODE){
            System.out.println("====PART DETAILS====");
            System.out.println("x: " + part.getX() + ", y: " + part.getY());
            System.out.println("x location is even? " + (part.getY() % 2 == 0) + "\n");

            System.out.println("====PICKUP DETAILS====");
            System.out.println("x: " + randX + ", y: " + randY + "\n");

            System.out.println("====OFFSET DETAILS====");
            System.out.println("x-offset: " + xOffset + ", y-offset: " + yOffset);
        }

        //Determine if greater or not to prevent going out of bounds
        randX = xGreater ? randX - (part.getWidth() - xOffset) : randX + (part.getWidth() - xOffset);
        randY = yGreater ? randY - (part.getHeight() - yOffset) : randY + (part.getHeight() - yOffset);

        //Test 2
        xOffset = randX % part.getWidth();
        yOffset = randY % part.getHeight();

        if(DEBUG_MODE){
            System.out.println("====NEW PART DETAILS====");
            System.out.println("x: " + part.getX() + ", y: " + part.getY());
            System.out.println("x location is even? " + (part.getY() % 2 == 0) + "\n");

            System.out.println("====NEW PICKUP DETAILS====");
            System.out.println("x: " + randX + ", y: " + randY + "\n");

            System.out.println("====NEW OFFSET DETAILS====");
            System.out.println("x-offset: " + xOffset + ", y-offset: " + yOffset);
        }

        m_food.setX(randX);
        m_food.setY(randY);
        m_bounds.setX(randX);
        m_bounds.setY(randY);
    }

    /**
     * Collect the pickup when the snake has collided with it.
     * */
    public void collect(SnakePart currentHeadPart){
        int randX = m_rand.nextInt(m_maxWidth) + m_minWidth;
        int randY = m_rand.nextInt(m_maxHeight) + m_minHeight;

        align(currentHeadPart, randX, randY);
    }

    /**
     * Determine if the snake has collided with the pickup
     * */
    public boolean shouldCollect(SnakePart part){
        return part.getBounds().overlaps(m_bounds);
    }

    public int getX(){
        return (int)m_bounds.getX();
    }

    public int getY(){
        return (int)m_bounds.getY();
    }

    public int getWidth(){
        return (int)m_bounds.getWidth();
    }

    public int getHeight(){
        return (int)m_bounds.getHeight();
    }
}
