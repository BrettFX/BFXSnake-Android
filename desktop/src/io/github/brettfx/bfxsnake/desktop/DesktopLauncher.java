package io.github.brettfx.bfxsnake.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.brettfx.bfxsnake.BFXSnake;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BFXSnake.DESKTOP_WIDTH;
		config.height = BFXSnake.DESKTOP_HEIGHT;
		config.title = BFXSnake.TITLE;
		new LwjglApplication(new BFXSnake(), config);
	}
}
