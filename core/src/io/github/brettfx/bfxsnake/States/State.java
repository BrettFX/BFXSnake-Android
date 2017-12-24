package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * @author brett
 * @since 12/24/2017
 */

public abstract class State {
    public static final int VIEWPORT_WIDTH = 240;
    public static final int VIEWPORT_HEIGHT = 400;

    protected GameStateManager m_gsm;
    protected OrthographicCamera m_cam;
    protected Vector3 m_mouse;

    protected State(GameStateManager gsm){
        m_gsm = gsm;
        m_cam = new OrthographicCamera();
        m_cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
}
