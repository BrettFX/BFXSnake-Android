package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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

    public GameStateManager(){
        m_states = new Stack<State>();
        m_difficulty = m_preferences.getInteger("Difficulty", 0);
    }

    public void setDifficulty(int difficulty){
        m_difficulty = difficulty;
        m_preferences.putInteger("Difficulty", m_difficulty);
    }

    public int getDifficulty(){
        return m_difficulty;
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
