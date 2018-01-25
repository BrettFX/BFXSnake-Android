package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author brett
 * @since 12/24/2017
 */

public abstract class State {
    protected GameStateManager m_gsm;
    protected OrthographicCamera m_cam;

    protected State(GameStateManager gsm){
        m_gsm = gsm;
        m_cam = new OrthographicCamera();
        m_cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
