package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Components.Score;

import static io.github.brettfx.bfxsnake.BFXSnake.BUTTON_COLOR;
import static io.github.brettfx.bfxsnake.BFXSnake.FONT_SIZE;

/**
 * @author brett
 * @since 1/9/2018
 */
public class LearderboardState extends State {

    private GameStateManager m_gsm;

    private Label m_resetLabel;
    private Texture m_resetTexture;

    private Label m_backLabel;
    private Texture m_backTexture;

    private Stage m_stage;

    protected LearderboardState(GameStateManager gsm) {
        super(gsm);
        m_gsm = gsm;

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_stage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_stage);

        BitmapFont leaderboardFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        leaderboardFont.getData().setScale(FONT_SIZE, FONT_SIZE);

        Label.LabelStyle leaderboardLabelStyle = new Label.LabelStyle(leaderboardFont, leaderboardFont.getColor());

        m_resetLabel = new Label("RESET HIGHSCORE", leaderboardLabelStyle);
        m_resetLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Score.resetHighScore();
                m_resetLabel.setText("HIGHSCORE IS NOW " + Score.getHighScore());
            }
        });

        m_backLabel = new Label("BACK TO MENU", leaderboardLabelStyle);
        m_backLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        Label titleLabel = new Label("LEADERBOARD", leaderboardLabelStyle);

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(leaderboardFont,
                titleLabel.getText() + "\n" +
                        m_resetLabel.getText() + "\n" +
                        m_backLabel.getText());

        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_stage.addActor(titleTable);

        Table leaderboardTable = new Table();
        leaderboardTable.top().padTop(Gdx.graphics.getHeight() / 4);;
        leaderboardTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        leaderboardTable.add(m_resetLabel);
        leaderboardTable.row().padTop(padding);

        leaderboardTable.add(m_backLabel);

        m_stage.addActor(leaderboardTable);

        int width = (int)(m_resetLabel.getWidth() + (m_backLabel.getWidth() / 2));
        int height = (int)m_resetLabel.getHeight();

        m_resetTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, Color.RED));
        m_backTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        float x = m_resetLabel.getX() - (m_backLabel.getWidth() / 4);

        sb.begin();

        sb.draw(m_resetTexture, x, m_resetLabel.getY());
        sb.draw(m_backTexture, x, m_backLabel.getY());

        sb.end();

        m_stage.draw();
    }

    @Override
    public void dispose() {
        m_stage.dispose();
        m_resetTexture.dispose();
        m_backTexture.dispose();
    }
}
