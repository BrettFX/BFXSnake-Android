/*
 * Reference: https://www.youtube.com/playlist?list=PLZm85UZQLd2TPXpUJfDEdWTSgszionbJy
 * */

package io.github.brettfx.bfxsnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.brettfx.bfxsnake.States.GameStateManager;
import io.github.brettfx.bfxsnake.States.PlayState;

public class BFXSnake extends ApplicationAdapter {
	public static final int SCALE_FACTOR = 45;

	public static SpriteBatch m_batch;
	private GameStateManager m_gameStateManager;
	
	@Override
	public void create () {
		m_batch = new SpriteBatch();
		m_gameStateManager = new GameStateManager();
		m_gameStateManager.push(new PlayState(m_gameStateManager));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		m_gameStateManager.update(Gdx.graphics.getDeltaTime());
		m_gameStateManager.render(m_batch);
	}
	
	@Override
	public void dispose () {
		m_batch.dispose();
		m_gameStateManager.dispose();
	}
}
