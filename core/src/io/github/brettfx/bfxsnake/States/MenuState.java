package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

    public static boolean DEBUG_MODE = false;

    private static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD", "PRO", "IMPOSSIBLE"};

    private Stage m_menuStage;
    private BitmapFont m_menuFont;

    private Label m_playLabel;
    private Label m_settingsLabel;
    private Label m_highScoreLabel;
    private Label m_difficultyLabel;

    private Texture m_playLabelTexture;
    private Texture m_settingsTexture;
    private Texture m_highScoreTexture;
    private Texture m_difficultyTexture;

    private float m_x;

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

        Label.LabelStyle menuLabelStyle = new Label.LabelStyle(m_menuFont, m_menuFont.getColor());

        m_playLabel =  new Label("PLAY", menuLabelStyle);
        m_playLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.set(new PlayState(m_gsm));
            }
        });

        m_settingsLabel = new Label("SETTINGS", menuLabelStyle);
        m_settingsLabel.addListener(new InputListener(){
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

        m_highScoreLabel =  new Label("HIGH SCORE: " + String.format(Locale.getDefault(), "%02d",
                Score.getHighScore()), menuLabelStyle);
        m_highScoreLabel.addListener(new InputListener(){
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

        m_difficultyLabel =  new Label("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()], menuLabelStyle);
        m_difficultyLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.setDifficulty((m_gsm.getDifficulty() + 1) % DIFFICULTIES.length);

                System.out.println("Current difficulties: " + DIFFICULTIES[m_gsm.getDifficulty()] +
                        " (" + m_gsm.getDifficulty() + ")");

                m_difficultyLabel.setText("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()]);
            }
        });

        Label titleLabel = new Label(BFXSnake.TITLE, new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));
        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_menuStage.addActor(titleTable);

        Table selectionTable = new Table();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 4);
        selectionTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        selectionTable.add(m_playLabel).expandX();
        selectionTable.row().padTop(padding);

        selectionTable.add(m_settingsLabel).expandX();
        selectionTable.row().padTop(padding);

        selectionTable.add(m_highScoreLabel).expandX();
        selectionTable.row().padTop(padding);

        selectionTable.add(m_difficultyLabel).expandX();

        m_menuStage.addActor(selectionTable);

        //For the button texture overlays
        Label temp = new Label("HIGHSCORE: IMPOSSIBLE", new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));
        int width = (int)(temp.getWidth() + (m_playLabel.getWidth() / 2));
        int height = (int)temp.getHeight();
        m_x = Gdx.graphics.getWidth() / 6;

        m_playLabelTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        m_settingsTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        m_highScoreTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        m_difficultyTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));

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
        sb.begin();

        sb.draw(m_playLabelTexture, m_x, m_playLabel.getY());
        sb.draw(m_settingsTexture, m_x, m_settingsLabel.getY());
        sb.draw(m_highScoreTexture, m_x, m_highScoreLabel.getY());
        sb.draw(m_difficultyTexture, m_x, m_difficultyLabel.getY());

        sb.end();

        m_menuStage.draw();
    }

    @Override
    public void dispose() {
        m_menuStage.dispose();
        m_menuFont.dispose();
        m_playLabelTexture.dispose();
        m_settingsTexture.dispose();
        m_highScoreTexture.dispose();
        m_difficultyTexture.dispose();
    }
}
