package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Components.Score;

import static io.github.brettfx.bfxsnake.BFXSnake.BUTTON_COLOR;
import static io.github.brettfx.bfxsnake.BFXSnake.FONT_SIZE;

/**
 * @author brett
 * @since 12/24/2017
 */
public class MenuState extends State {
    private final float BUTTON_PADDING = 20f;
    private final float BUTTON_WIDTH_SCALE = 1.0625f;

    public static boolean DEBUG_MODE = false;

    private static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD", "PRO", "IMPOSSIBLE"};

    private Stage m_menuStage;
    private BitmapFont m_menuFont;

    private TextButton m_btnPlay;
    private TextButton m_btnSettings;
    private TextButton m_btnHighScore;
    private TextButton m_btnDifficulty;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        m_gsm.saveSnakeColor();
        m_gsm.savePickupColor();
        m_gsm.saveControllerState();
        m_gsm.flush();
        Score.flush();

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_menuStage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_menuStage);

        m_menuFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_menuFont.getData().setScale(FONT_SIZE, FONT_SIZE);

        m_btnPlay = new TextButton("PLAY", m_gsm.getButtonStyle());
        m_btnPlay.pad(BUTTON_PADDING); //Adds padding around text to give feal of authentic button
        m_btnPlay.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.set(new PlayState(m_gsm));
            }
        });

        m_btnSettings = new TextButton("SETTINGS", m_gsm.getButtonStyle());
        m_btnSettings.pad(BUTTON_PADDING);
        m_btnSettings.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                //Go to settings state
                m_gsm.set(new SettingsState(m_gsm));
            }
        });

        m_btnHighScore = new TextButton("HIGH SCORE: " + String.format(Locale.getDefault(), "%02d",
                Score.getHighScore()), m_gsm.getButtonStyle());
        m_btnHighScore.pad(BUTTON_PADDING);
        m_btnHighScore.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //Go to leaderboard state
                m_gsm.set(new LearderboardState(m_gsm));
            }
        });

        m_btnDifficulty = new TextButton("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()], m_gsm.getButtonStyle());
        m_btnDifficulty.pad(BUTTON_PADDING);
        m_btnDifficulty.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.setDifficulty((m_gsm.getDifficulty() + 1) % DIFFICULTIES.length);

                System.out.println("Current difficulties: " + DIFFICULTIES[m_gsm.getDifficulty()] +
                        " (" + m_gsm.getDifficulty() + ")");

                m_btnDifficulty.setText("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()]);
            }
        });

        Label titleLabel = new Label(BFXSnake.TITLE, new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));
        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_menuStage.addActor(titleTable);

        Table selectionTable = new Table();
        selectionTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        selectionTable.center();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 6);
        selectionTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        //For the button widths
        Label largeLabel = new Label("HIGHSCORE: IMPOSSIBLE", new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));

        //selectionTable.add(m_playLabel).expandX();
        selectionTable.add(m_btnPlay).width(largeLabel.getWidth() * BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnSettings).width(largeLabel.getWidth() * BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnHighScore).width(largeLabel.getWidth() * BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnDifficulty).width(largeLabel.getWidth() * BUTTON_WIDTH_SCALE);

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
    }
}
