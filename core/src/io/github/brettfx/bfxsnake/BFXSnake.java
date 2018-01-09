/*
 * Reference: https://www.youtube.com/playlist?list=PLZm85UZQLd2TPXpUJfDEdWTSgszionbJy
 * */

package io.github.brettfx.bfxsnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.brettfx.bfxsnake.Sprites.Pickup;
import io.github.brettfx.bfxsnake.Sprites.Snake;
import io.github.brettfx.bfxsnake.States.GameStateManager;
import io.github.brettfx.bfxsnake.States.MenuState;
import io.github.brettfx.bfxsnake.States.PlayState;

public class BFXSnake extends ApplicationAdapter {
	public static final String PREFS_NAME = "BFXSnake_Preferences";
	public static final String TITLE = "BFX SNAKE";

	public static final int SCALE_FACTOR = 45;

	public static final String SCORE_FONT = "fonts/score.fnt";
	public static final String MENU_FONT = "fonts/menu.fnt";

	public static final float FONT_SIZE = 5.0f; //1.4f

	public static SpriteBatch m_batch;
	private GameStateManager m_gameStateManager;

	public static final Color BUTTON_COLOR = Color.GRAY;

	public static final Color COLORS[] = {
		Color.GREEN, Color.RED, Color.CHARTREUSE, Color.CORAL, Color.CYAN,
		Color.FIREBRICK, Color.FOREST, Color.GOLD, Color.GOLDENROD, Color.GRAY,
		Color.BLUE, Color.LIME, Color.MAGENTA, Color.MAROON, Color.NAVY,
		Color.OLIVE, Color.ORANGE, Color.PINK, Color.PURPLE, Color.BROWN,
		Color.ROYAL, Color.SALMON, Color.SCARLET, Color.SKY, Color.SLATE,
		Color.TAN, Color.TEAL, Color.VIOLET, Color.WHITE, Color.YELLOW
	};
	
	@Override
	public void create () {
		Snake.DEBUG_MODE = false;
		PlayState.DEBUG_MODE = true;
		Pickup.DEBUG_MODE = false;

		m_batch = new SpriteBatch();
		m_gameStateManager = new GameStateManager();
		m_gameStateManager.push(new MenuState(m_gameStateManager));
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
