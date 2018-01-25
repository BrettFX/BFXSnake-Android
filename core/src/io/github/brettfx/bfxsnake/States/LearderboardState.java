package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Components.Score;

import static io.github.brettfx.bfxsnake.BFXSnake.DEF_FONT_SIZE;

/**
 * @author brett
 * @since 1/9/2018
 */
public class LearderboardState extends State {
    private Stage m_stage;

    private BitmapFont m_leaderboardFont;

    protected LearderboardState(final GameStateManager gsm) {
        super(gsm);

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_stage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_stage);

        m_leaderboardFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_leaderboardFont.getData().setScale(DEF_FONT_SIZE, DEF_FONT_SIZE);

        m_gsm.setTextButtonFont(m_leaderboardFont);

        Label.LabelStyle leaderboardLabelStyle = new Label.LabelStyle(m_leaderboardFont, m_leaderboardFont.getColor());

        TextButton m_btnReset = new TextButton("RESET", m_gsm.getButtonStyle());
        m_btnReset.pad(BFXSnake.BUTTON_PADDING);
        m_btnReset.setColor(BFXSnake.BUTTON_COLOR);
        m_btnReset.getLabel().setColor(Color.RED);
        m_btnReset.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                Score.resetHighScore();
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        TextButton m_btnBack = new TextButton("CANCEL", m_gsm.getButtonStyle());
        m_btnBack.pad(BFXSnake.BUTTON_PADDING);
        m_btnBack.setColor(BFXSnake.BUTTON_COLOR);
        m_btnBack.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.getAssets().get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        Label titleLabel = new Label("RESET HIGHSCORE?", leaderboardLabelStyle);

        Table titleTable = new Table();

        titleTable.top().padTop(titleLabel.getHeight());
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_stage.addActor(titleTable);

        Table leaderboardTable = new Table();
        leaderboardTable.top().padTop(Gdx.graphics.getHeight() / 4 + titleLabel.getHeight());
        leaderboardTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        //For the button widths
        Label largeLabel = new Label(BFXSnake.LARGE_LABEL_TEXT, new Label.LabelStyle(m_leaderboardFont, m_leaderboardFont.getColor()));

        leaderboardTable.add(m_btnReset).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        leaderboardTable.row().padTop(padding);

        leaderboardTable.add(m_btnBack).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);

        m_stage.addActor(leaderboardTable);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        m_stage.draw();
    }

    @Override
    public void dispose() {
        m_leaderboardFont.dispose();
        m_stage.dispose();
    }
}
