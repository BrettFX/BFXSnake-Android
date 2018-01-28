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

	public static final String LARGE_LABEL_TEXT = "  HIGHSCORE: IMPOSSIBLE  ";
	public static final String SMALL_LABEL_TEXT = "    BACK TO MENU    ";

	public static final float DEF_BUTTON_WIDTH_SCALE = 1.0625f;

	//Increasing this will make the snake smaller and decrease padding between buttons
	public static final int SCALE_FACTOR = 45;

	public static final Color BUTTON_COLOR = Color.GRAY;
	public static final float BUTTON_PADDING = 20f;

	public static final String SCORE_FONT = "fonts/font-export.fnt";
	public static final String MENU_FONT = "fonts/font-export.fnt";
	public static final String TITLE_FONT = "fonts/font-title-export.fnt";

	public static final String BUTTON_PACK = "main_button/glassy-ui.atlas";

	public static final String THEME_MUSIC = "audio/music/BFXSnake.mp3";
	public static final String MENU_MUSIC = "audio/music/BFXSnake_Menu.mp3";

	public static final String BUTTON_CLICK_DOWN_SOUND = "audio/sounds/ButtonPressDown.wav";
	public static final String BUTTON_CLICK_UP_SOUND = "audio/sounds/ButtonPressUp.wav";
	public static final String HIGHSCORE_SOUND = "audio/sounds/Highscore.wav";
	public static final String PICKUP_SOUND = "audio/sounds/Pickup.wav";
	public static final String GAMEOVER_SOUND = "audio/sounds/Gameover.wav";

	public static final float DEF_FONT_SIZE = 5.0f; //1.4f
	public static final float SETTINGS_FONT_SIZE = 5.0f; //2.5f
	public static final float OPACITY = 0.75f;

	public static final float DEF_MUSIC_VOL = 1.0f;
	public static final float PAUSE_MUSIC_VOL = 0.2f;

	private SpriteBatch m_batch;
	private GameStateManager m_gameStateManager;

	public static final Color SCORE_COLOR = Color.WHITE;
	public static final Color NOTIFICATION_COLOR = Color.GREEN;

	public static final Color COLORS[] = {
		Color.GREEN, Color.RED, Color.ROYAL, Color.CORAL, Color.CYAN,
		Color.FIREBRICK, Color.FOREST, Color.GOLD, Color.GOLDENROD, Color.DARK_GRAY,
		Color.BLUE, Color.LIME, Color.MAGENTA, Color.MAROON, Color.NAVY,
		Color.OLIVE, Color.ORANGE, Color.PINK, Color.PURPLE, Color.BROWN,
			Color.CHARTREUSE, Color.SALMON, Color.SCARLET, Color.SKY, Color.SLATE,
		Color.TAN, Color.TEAL, Color.VIOLET, Color.WHITE, Color.YELLOW
	};
	
	@Override
	public void create () {
		Snake.DEBUG_MODE = false;
		MenuState.DEBUG_MODE = false;
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
