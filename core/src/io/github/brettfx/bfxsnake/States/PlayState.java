package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.brettfx.bfxsnake.Sprites.Snake;

/**
 * @author brett
 * @since 12/24/2017
 */
public class PlayState extends State {

    private Snake m_snake;

    private boolean m_gameOver;

    protected PlayState(GameStateManager gsm) {
        super(gsm);

        m_snake = new Snake();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

    }
}
