package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Components.Score;

import static io.github.brettfx.bfxsnake.BFXSnake.DEF_FONT_SIZE;

/**
 * @author brett
 * @since 12/24/2017
 */
public class MenuState extends State {
    public static boolean DEBUG_MODE = false;

    private static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD", "PRO", "IMPOSSIBLE"};

    private Stage m_menuStage;
    private BitmapFont m_menuFont;
    private BitmapFont m_titleFont;

    private TextButton m_btnDifficulty;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        m_gsm.setSnakeColor();
        m_gsm.setPickupColor();
        m_gsm.setControllerState();
        m_gsm.flush();
        Score.flush();

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_menuStage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_menuStage);

        m_menuFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_menuFont.getData().setScale(DEF_FONT_SIZE, DEF_FONT_SIZE);

        m_gsm.setTextButtonFont(m_menuFont);

        TextButton btnPlay = new TextButton("PLAY", m_gsm.getButtonStyle());
        btnPlay.pad(BFXSnake.BUTTON_PADDING); //Adds padding around text to give feal of authentic button
        btnPlay.setColor(BFXSnake.BUTTON_COLOR);
        btnPlay.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.set(new PlayState(m_gsm));
            }
        });

        TextButton btnSettings = new TextButton("SETTINGS", m_gsm.getButtonStyle());
        btnSettings.pad(BFXSnake.BUTTON_PADDING);
        btnSettings.setColor(BFXSnake.BUTTON_COLOR);
        btnSettings.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                //Go to settings state
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.set(new SettingsState(m_gsm));
            }
        });

        TextButton btnHighScore = new TextButton("HIGH SCORE: " + String.format(Locale.getDefault(), "%02d",
                Score.getHighScore()), m_gsm.getButtonStyle());
        btnHighScore.pad(BFXSnake.BUTTON_PADDING);
        btnHighScore.setColor(BFXSnake.BUTTON_COLOR);
        btnHighScore.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();

                //Go to leaderboard state
                m_gsm.set(new LearderboardState(m_gsm));
            }
        });

        m_btnDifficulty = new TextButton("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()], m_gsm.getButtonStyle());
        m_btnDifficulty.pad(BFXSnake.BUTTON_PADDING);
        m_btnDifficulty.setColor(BFXSnake.BUTTON_COLOR);
        m_btnDifficulty.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();

                m_gsm.setDifficulty((m_gsm.getDifficulty() + 1) % DIFFICULTIES.length);

                System.out.println("Current difficulties: " + DIFFICULTIES[m_gsm.getDifficulty()] +
                        " (" + m_gsm.getDifficulty() + ")");

                m_btnDifficulty.setText("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()]);
            }
        });

        m_titleFont = new BitmapFont(Gdx.files.internal(BFXSnake.TITLE_FONT));
        m_titleFont.getData().setScale(BFXSnake.DEF_FONT_SIZE, BFXSnake.DEF_FONT_SIZE);

        Label titleLabel = new Label(BFXSnake.TITLE, new Label.LabelStyle(m_titleFont, m_titleFont.getColor()));
        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_menuStage.addActor(titleTable);

        Table selectionTable = new Table();
        selectionTable.center();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 4);
        selectionTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        //For the button widths
        Label largeLabel = new Label(BFXSnake.LARGE_LABEL_TEXT, new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));

        selectionTable.add(btnPlay).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(btnSettings).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(btnHighScore).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnDifficulty).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);

        m_menuStage.addActor(selectionTable);

        if(DEBUG_MODE){
            m_menuStage.setDebugAll(true);
        }
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        m_menuStage.draw();
    }

    @Override
    public void dispose() {
        m_menuStage.dispose();
        m_menuFont.dispose();
        m_titleFont.dispose();
    }
}
