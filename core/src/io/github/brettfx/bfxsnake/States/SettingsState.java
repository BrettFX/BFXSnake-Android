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

    //To restore everything back to default
    private Label m_restoreDefaults;

    //To go back to the previous state (menu)
    private Label m_backLabel;

    private Stage m_settingsStage;

    private BitmapFont m_settingsFont;

    private Texture m_snakeColorTexture;
    private Texture m_pickupColorTexture;
    private Texture m_controllerStateTexture;
    private Texture m_restoreDefValTexture;
    private Texture m_backLabelTexture;

    protected SettingsState(GameStateManager gsm) {
        super(gsm);

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
                m_gsm.setSnakeColor((m_gsm.getSnakeColorIndex() + 1) % BFXSnake.COLORS.length);
            }
        });

        m_pickupColorLabel = new Label("PICKUP COLOR", settingsLabelStyle);
        m_pickupColorLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.setPickupColor((m_gsm.getPickupColorIndex() + 1) % BFXSnake.COLORS.length);
            }
        });

        m_controllerState = new Label("CONTROLLER: " + m_gsm.getControllerState(), settingsLabelStyle);
        m_controllerState.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.toggleControllerState();
            }
        });

        m_backLabel = new Label("BACK", settingsLabelStyle);
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

        m_restoreDefaults = new Label("RESTORE DEFAULTS", settingsLabelStyle);
        m_restoreDefaults.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.setSnakeColor(0);
                m_gsm.setSnakeColor(m_gsm.getSnakeColorIndex());

                m_gsm.setPickupColor(1);
                m_gsm.setPickupColor(m_gsm.getPickupColorIndex());

                if(!m_gsm.isControllerOn()){
                    m_gsm.toggleControllerState();
                }
            }
        });

        Label titleLabel = new Label("SETTINGS", settingsLabelStyle);

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(m_settingsFont,
                titleLabel.getText() + "\n" +
                   m_snakeColorLabel.getText() + "\n" +
                   m_pickupColorLabel.getText() + "\n" +
                   m_controllerState.getText() + "\n" +
                   m_restoreDefaults.getText() + "\n" +
                   m_backLabel.getText());

        Table titleTable = new Table();

        titleTable.top();
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_settingsStage.addActor(titleTable);

        Table selectionTable = new Table();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 6);
        selectionTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        selectionTable.add(m_snakeColorLabel);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_pickupColorLabel);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_controllerState);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_restoreDefaults);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_backLabel);
        selectionTable.row().padTop(padding);

        m_settingsStage.addActor(selectionTable);

        //For button texture overlays
        int width = (int)(m_restoreDefaults.getWidth() + (m_backLabel.getWidth() / 2));
        int height = (int)m_restoreDefaults.getHeight();

        m_snakeColorTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                m_gsm.getSnakeColor()));
        m_pickupColorTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                m_gsm.getPickupColor()));
        m_controllerStateTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                BUTTON_COLOR));
        m_restoreDefValTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                BUTTON_COLOR));
        m_backLabelTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                BUTTON_COLOR));
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        m_controllerState.setText("CONTROLLER: " + m_gsm.getControllerState());
    }

    @Override
    public void render(SpriteBatch sb) {
        float x = m_restoreDefaults.getX() - (m_backLabel.getWidth() / 4);

        sb.begin();

        sb.draw(m_snakeColorTexture, x, m_snakeColorLabel.getY()); //Render snake color button
        sb.draw(m_pickupColorTexture, x, m_pickupColorLabel.getY()); //Render pickup color button
        sb.draw(m_controllerStateTexture, x, m_controllerState.getY()); //Render controller state button
        sb.draw(m_restoreDefValTexture, x, m_restoreDefaults.getY()); //Render restore default values button
        sb.draw(m_backLabelTexture, x, m_backLabel.getY()); //Render back button

        sb.end();

        m_settingsStage.draw();
    }

    @Override
    public void dispose() {
        m_settingsFont.dispose();
        m_settingsStage.dispose();
        m_snakeColorTexture.dispose();
        m_pickupColorTexture.dispose();
        m_controllerStateTexture.dispose();
        m_restoreDefValTexture.dispose();
        m_backLabelTexture.dispose();
    }
}
