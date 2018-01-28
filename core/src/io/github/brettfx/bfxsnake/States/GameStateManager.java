package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Stack;

import io.github.brettfx.bfxsnake.BFXSnake;

import static io.github.brettfx.bfxsnake.BFXSnake.DEF_FONT_SIZE;

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

    private boolean m_controllerOn;
    private boolean m_musicOn;

    private TextureAtlas m_buttonAtlas;
    private Skin m_buttonSkin;

    private TextButton.TextButtonStyle m_textButtonStyle;

    private AssetManager m_assetManager;

    private Music m_themeMusic;
    private Music m_menuMusic;

    public GameStateManager(){
        m_states = new Stack<State>();

        m_difficulty = m_preferences.getInteger("Difficulty", 0);

        m_snakeColorIndex = m_preferences.getInteger("SnakeColor", 0);
        m_snakeColor = new Color(BFXSnake.COLORS[m_snakeColorIndex]);

        m_pickupColorIndex = m_preferences.getInteger("PickupColor", 1);
        m_pickupColor = new Color(BFXSnake.COLORS[m_pickupColorIndex]);

        m_controllerOn = m_preferences.getBoolean("ControllerState", true);
        m_musicOn = m_preferences.getBoolean("MusicState", true);

        m_buttonAtlas = new TextureAtlas(Gdx.files.internal(BFXSnake.BUTTON_PACK));
        m_buttonSkin = new Skin(m_buttonAtlas);

        BitmapFont menuFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        menuFont.getData().setScale(DEF_FONT_SIZE, DEF_FONT_SIZE);

        m_textButtonStyle = new TextButton.TextButtonStyle();
        m_textButtonStyle.up = m_buttonSkin.getDrawable("button");
        m_textButtonStyle.down = m_buttonSkin.getDrawable("button-down");
        m_textButtonStyle.pressedOffsetX = 1; //Moves text on button
        m_textButtonStyle.pressedOffsetY = -1; //Moves text on button

        //Instantiate asset manager to load in sounds and music
        m_assetManager = new AssetManager();
        m_assetManager.load(BFXSnake.THEME_MUSIC, Music.class);
        m_assetManager.load(BFXSnake.MENU_MUSIC, Music.class);
        m_assetManager.load(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class);
        m_assetManager.load(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class);
        m_assetManager.load(BFXSnake.HIGHSCORE_SOUND, Sound.class);
        m_assetManager.load(BFXSnake.PICKUP_SOUND, Sound.class);
        m_assetManager.load(BFXSnake.GAMEOVER_SOUND, Sound.class);
        m_assetManager.load(BFXSnake.WINNING_SOUND, Sound.class);
        m_assetManager.finishLoading();

        m_themeMusic = m_assetManager.get(BFXSnake.THEME_MUSIC, Music.class);
        m_themeMusic.setLooping(true);
        m_themeMusic.setVolume(BFXSnake.DEF_MUSIC_VOL);

        m_menuMusic = m_assetManager.get(BFXSnake.MENU_MUSIC, Music.class);
        m_menuMusic.setLooping(true);
        m_menuMusic.setVolume(BFXSnake.DEF_MUSIC_VOL);
    }

    public void stopSounds(){
        m_assetManager.get(BFXSnake.GAMEOVER_SOUND, Sound.class).stop();
        m_assetManager.get(BFXSnake.WINNING_SOUND, Sound.class).stop();
        m_assetManager.get(BFXSnake.HIGHSCORE_SOUND, Sound.class).stop();
        m_assetManager.get(BFXSnake.PICKUP_SOUND, Sound.class).stop();
    }

    public AssetManager getAssets(){
        return m_assetManager;
    }

    public Music getThemeMusic(){
        return m_themeMusic;
    }

    public Music getMenuMusic(){
        return m_menuMusic;
    }

    public void setTextButtonFont(BitmapFont bitmapFont){
        m_textButtonStyle.font = bitmapFont;
    }

    public TextButton.TextButtonStyle getButtonStyle(){
        return m_textButtonStyle;
    }

    public void setDifficulty(int difficulty){
        m_difficulty = difficulty;
    }

    public void setDifficulty(){
        m_preferences.putInteger("Difficulty", m_difficulty);
    }

    public int getDifficulty(){
        return m_difficulty;
    }

    public void setSnakeColor(int i){
        m_snakeColorIndex = i;
        m_snakeColor.set(BFXSnake.COLORS[m_snakeColorIndex]);
    }

    public void setSnakeColor(){
        m_preferences.putInteger("SnakeColor", m_snakeColorIndex);
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

    public void setPickupColor(){
        m_preferences.putInteger("PickupColor", m_pickupColorIndex);
    }

    public int getPickupColorIndex(){
        return m_pickupColorIndex;
    }

    public Color getPickupColor(){
        return m_pickupColor;
    }

    public void toggleControllerState(){
        m_controllerOn = !m_controllerOn;
    }

    public void setControllerState(){
        m_preferences.putBoolean("ControllerState", m_controllerOn);
    }

    public boolean isControllerOn(){
        return m_controllerOn;
    }

    public String getControllerState(){
        return m_controllerOn ? "ON" : "OFF";
    }

    public void toggleMusicState(){
        m_musicOn = !m_musicOn;
    }

    public void setMusicState(){
        m_preferences.putBoolean("MusicState", m_musicOn);
    }

    public boolean isMusicOn(){
        return m_musicOn;
    }

    public String getMusicState(){
        return m_musicOn ? "ON" : "OFF";
    }

    public void flush(){
        m_preferences.flush();
    }

    public void push(State state){
        m_states.push(state);
    }

    @SuppressWarnings("unused")
    public void pop(){
        State removedState = m_states.pop();
        removedState.dispose();
    }

    public void set(State state){
        State removedState = m_states.pop();
        removedState.dispose();
        m_states.push(state);
    }

    /*Not used but kept for future reference*/
    @SuppressWarnings("unused")
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

        m_buttonAtlas.dispose();
        m_buttonSkin.dispose();
        m_assetManager.dispose();
    }
}
