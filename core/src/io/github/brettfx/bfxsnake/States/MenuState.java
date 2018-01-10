package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
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

    public static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD", "PRO", "IMPOSSIBLE"};

    private Stage m_menuStage;
    private BitmapFont m_menuFont;
    private BitmapFont m_titleFont;
    private GameStateManager m_gsm;

    private Label m_playLabel;
    private Label m_settingsLabel;
    private Label m_highScoreLabel;
    private Label m_difficultyLabel;

    private Score m_menuScore;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        m_gsm = gsm;

        m_gsm.saveSnakeColor();
        m_gsm.savePickupColor();
        m_gsm.saveControllerState();
        m_gsm.flush();

        m_menuScore = new Score(BFXSnake.SCORE_COLOR);

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_menuStage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_menuStage);

        m_menuFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_menuFont.getData().setScale(FONT_SIZE, FONT_SIZE);

        m_titleFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_titleFont.getData().setScale(FONT_SIZE, FONT_SIZE);

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
                m_menuScore.getHighScore()), menuLabelStyle);
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

        Label titleLabel = new Label(BFXSnake.TITLE, new Label.LabelStyle(m_titleFont, m_titleFont.getColor()));

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(m_menuFont,
                titleLabel.getText() + "\n" +
                        m_playLabel.getText() + "\n" +
                        m_settingsLabel.getText() + "\n" +
                        m_highScoreLabel.getText() + "\n" +
                        m_difficultyLabel.getText());

        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_menuStage.addActor(titleTable);

        Table selectionTable = new Table();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 4);
        selectionTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        selectionTable.add(m_playLabel);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_settingsLabel);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_highScoreLabel);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_difficultyLabel);

        m_menuStage.addActor(selectionTable);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        int width = (int)(m_difficultyLabel.getWidth() + (m_playLabel.getWidth() / 2));
        int height = (int)m_difficultyLabel.getHeight();
        float x = m_difficultyLabel.getX() - (m_playLabel.getWidth() / 4);

        sb.begin();

        Texture playLabelTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        sb.draw(playLabelTexture, x, m_playLabel.getY());

        Texture settingsTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        sb.draw(settingsTexture, x, m_settingsLabel.getY());

        Texture highScoreTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        sb.draw(highScoreTexture, x, m_highScoreLabel.getY());

        Texture difficultyTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        sb.draw(difficultyTexture, x, m_difficultyLabel.getY());

        sb.end();

        playLabelTexture.dispose();
        settingsTexture.dispose();
        highScoreTexture.dispose();
        difficultyTexture.dispose();

        m_menuStage.draw();
    }

    @Override
    public void dispose() {
        m_menuStage.dispose();
        m_menuFont.dispose();
        m_titleFont.dispose();
    }
}
