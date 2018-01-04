package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
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

import static io.github.brettfx.bfxsnake.BFXSnake.BUTTON_COLOR;
import static io.github.brettfx.bfxsnake.BFXSnake.FONT_SIZE;

/**
 * @author brett
 * @since 1/3/2018
 */
public class SettingsState extends State{

    //Change color of snake
    private Label m_snakeColorLabel;

    //Change color of pickup
    private Label m_pickupColorLabel;

    //Toggle the controller as enabled or disabled
    private Label m_controllerState;

    private Stage m_settingsStage;

    private BitmapFont m_settingsFont;

    private GameStateManager m_gsm;

    protected SettingsState(GameStateManager gsm) {
        super(gsm);

        m_gsm = gsm;

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_settingsStage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_settingsStage);

        m_settingsFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_settingsFont.getData().setScale(FONT_SIZE, FONT_SIZE);

        Label.LabelStyle settingsLabelStyle = new Label.LabelStyle(m_settingsFont, m_settingsFont.getColor());

        m_snakeColorLabel =  new Label("SNAKE COLOR", settingsLabelStyle);
        m_snakeColorLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                //TODO change snake color
                m_gsm.setSnakeColor((m_gsm.getSnakeColorIndex() + 1) % BFXSnake.COLORS.length);
            }
        });

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(m_settingsFont, m_snakeColorLabel.getText());

        Table selectionTable = new Table();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 4);
        selectionTable.setFillParent(true);

        selectionTable.add(m_snakeColorLabel);
        selectionTable.row().padTop(5f);

        m_settingsStage.addActor(selectionTable);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        int width = (int)m_snakeColorLabel.getWidth();
        int height = (int)m_snakeColorLabel.getHeight();
        float x = m_snakeColorLabel.getX() - (m_snakeColorLabel.getWidth() / 4);

        sb.begin();

        Texture playLabelTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2, BUTTON_COLOR));
        sb.draw(playLabelTexture, x, m_snakeColorLabel.getY());

        sb.end();

        m_settingsStage.draw();
    }

    @Override
    public void dispose() {
        m_settingsStage.dispose();
    }
}
