/*
 * Reference: https://www.youtube.com/playlist?list=PLZm85UZQLd2TPXpUJfDEdWTSgszionbJy
 * */

package io.github.brettfx.bfxsnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BFXSnake extends ApplicationAdapter {
	public static final int SCALE_FACTOR = 45;

	private ShapeRenderer m_shapeRenderer;
	private float m_snakePartSize;

	public static SpriteBatch m_batch;

	
	@Override
	public void create () {
		m_shapeRenderer = new ShapeRenderer();
		m_snakePartSize = Gdx.graphics.getWidth() / SCALE_FACTOR;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		m_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		m_shapeRenderer.setColor(0, 1, 0, 1);
		m_shapeRenderer.rect(400, 400, m_snakePartSize, m_snakePartSize);
		m_shapeRenderer.rect(400 + m_snakePartSize, 400, m_snakePartSize, m_snakePartSize);
		m_shapeRenderer.rect(400 + (m_snakePartSize * 2), 400, m_snakePartSize, m_snakePartSize);
		m_shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		m_shapeRenderer.dispose();
	}
}
