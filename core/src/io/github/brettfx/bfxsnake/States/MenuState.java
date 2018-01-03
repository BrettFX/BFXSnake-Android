package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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

import static io.github.brettfx.bfxsnake.BFXSnake.FONT_SIZE;

/**
 * @author brett
 * @since 12/24/2017
 */
public class MenuState extends State {

    private static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD", "PRO"};

    private Stage m_menuStage;
    private BitmapFont m_menuFont;
    private BitmapFont m_titleFont;
    private GameStateManager m_gsm;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        m_gsm = gsm;

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_menuStage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_menuStage);

        m_menuFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_menuFont.getData().setScale(FONT_SIZE, FONT_SIZE);

        m_titleFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_titleFont.getData().setScale(FONT_SIZE, FONT_SIZE);

        Label playLabel =  new Label("PLAY", new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));
        playLabel.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_gsm.push(new PlayState(m_gsm));
                dispose();
            }
        });

        final Label highScoreLabel =  new Label("HIGH SCORE: " + String.format(Locale.getDefault(), "%02d", Score.getHighScore()),
                new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));

        final Label difficultyLabel =  new Label("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()],
                new Label.LabelStyle(m_menuFont, m_menuFont.getColor()));

        difficultyLabel.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_gsm.setDifficulty((m_gsm.getDifficulty() + 1) % DIFFICULTIES.length);

                System.out.println("Current difficulties: " + DIFFICULTIES[m_gsm.getDifficulty()] +
                        " (" + m_gsm.getDifficulty() + ")");

                difficultyLabel.setText("DIFFICULTY: " + DIFFICULTIES[m_gsm.getDifficulty()]);
            }
        });

        Label titleLabel = new Label(BFXSnake.TITLE, new Label.LabelStyle(m_titleFont, m_titleFont.getColor()));

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(m_menuFont,
                titleLabel.getText() + "\n" +
                        playLabel.getText() + "\n" +
                        highScoreLabel.getText() + "\n" +
                        difficultyLabel.getText());

        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_menuStage.addActor(titleTable);

        Table selectionTable = new Table();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 2);
        selectionTable.setFillParent(true);

        selectionTable.add(playLabel);
        selectionTable.row().padTop(5f);

        selectionTable.add(highScoreLabel);
        selectionTable.row().padTop(5f);

        selectionTable.add(difficultyLabel);

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
        m_menuStage.draw();
    }

    @Override
    public void dispose() {
        m_menuStage.dispose();
        m_menuFont.dispose();
        m_titleFont.dispose();
    }
}
