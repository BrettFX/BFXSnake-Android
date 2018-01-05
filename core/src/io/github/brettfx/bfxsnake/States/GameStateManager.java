package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

import io.github.brettfx.bfxsnake.BFXSnake;

/**
 * @author Brett Allen
 * @since 12/24/2017
 */

public class GameStateManager {

    //A stack of states for more efficient memory utilization
    private Stack<State> m_states;

    private static Preferences m_preferences = Gdx.app.getPreferences(BFXSnake.PREFS_NAME);

    private int m_difficulty;

    private int m_snakeColorIndex;
    private Color m_snakeColor;

    private int m_pickupColorIndex;
    private Color m_pickupColor;

    public GameStateManager(){
        m_states = new Stack<State>();

        m_difficulty = m_preferences.getInteger("Difficulty", 0);

        m_snakeColorIndex = m_preferences.getInteger("SnakeColor", 0);
        m_snakeColor = new Color(BFXSnake.COLORS[m_snakeColorIndex]);

        m_pickupColorIndex = m_preferences.getInteger("PickupColor", 1);
        m_pickupColor = new Color(BFXSnake.COLORS[m_pickupColorIndex]);
    }

    public void setDifficulty(int difficulty){
        m_difficulty = difficulty;
    }

    public void saveDifficulty(){
        m_preferences.putInteger("Difficulty", m_difficulty);
        m_preferences.flush();
    }

    public int getDifficulty(){
        return m_difficulty;
    }

    public void setSnakeColor(int i){
        m_snakeColorIndex = i;
        m_snakeColor.set(BFXSnake.COLORS[m_snakeColorIndex]);
    }

    public void saveSnakeColor(){
        m_preferences.putInteger("SnakeColor", m_snakeColorIndex);
        m_preferences.flush();
    }

    public int getSnakeColorIndex(){
        return m_snakeColorIndex;
    }

    public Color getSnakeColor(){
        return m_snakeColor;
    }

    public void setPickupColor(int i){
        m_pickupColorIndex = i;
        m_pickupColor.set(BFXSnake.COLORS[m_pickupColorIndex]);
    }

    public void savePickupColor(){
        m_preferences.putInteger("PickupColor", m_pickupColorIndex);
        m_preferences.flush();
    }

    public int getPickupColorIndex(){
        return m_pickupColorIndex;
    }

    public void push(State state){
        m_states.push(state);
    }

    public void pop(){
        State removedState = m_states.pop();
        removedState.dispose();
    }

    public void set(State state){
        State removedState = m_states.pop();
        removedState.dispose();
        m_states.push(state);
    }

    public Pixmap getPixmapRoundedRectangle(int width, int height, int radius, Color color) {

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        // Pink rectangle
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight()-2*radius);

        // Green rectangle
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2*radius, pixmap.getHeight());


        // Bottom-left circle
        pixmap.fillCircle(radius, radius, radius);

        // Top-left circle
        pixmap.fillCircle(radius, pixmap.getHeight()-radius, radius);

        // Bottom-right circle
        pixmap.fillCircle(pixmap.getWidth()-radius, radius, radius);

        // Top-right circle
        pixmap.fillCircle(pixmap.getWidth()-radius, pixmap.getHeight()-radius, radius);
        return pixmap;
    }

    public void update(float dt){
        m_states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        m_states.peek().render(sb);
    }

    public void dispose(){
        for(State state : m_states){
            state.dispose();
        }
    }
}
