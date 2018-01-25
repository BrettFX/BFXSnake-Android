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

import io.github.brettfx.bfxsnake.BFXSnake;

import static io.github.brettfx.bfxsnake.BFXSnake.DEF_FONT_SIZE;
import static io.github.brettfx.bfxsnake.BFXSnake.SETTINGS_FONT_SIZE;

/**
 * @author brett
 * @since 1/3/2018
 */
public class SettingsState extends State{

    //Change color of snake
    private TextButton m_btnSnakeColor;

    //Change color of pickup
    private TextButton m_btnPickupColor;

    //Toggle the controller as enabled or disabled
    private TextButton m_btnControllerState;

    private Stage m_settingsStage;

    private BitmapFont m_settingsFont;
    private BitmapFont m_headingFont;

    protected SettingsState(GameStateManager gsm) {
        super(gsm);

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_settingsStage = new Stage(viewport);

        Gdx.input.setInputProcessor(m_settingsStage);

        m_settingsFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_settingsFont.getData().setScale(SETTINGS_FONT_SIZE, SETTINGS_FONT_SIZE);

        m_gsm.setTextButtonFont(m_settingsFont);

        m_btnSnakeColor = new TextButton("SNAKE COLOR", m_gsm.getButtonStyle());
        m_btnSnakeColor.pad(BFXSnake.BUTTON_PADDING);
        m_btnSnakeColor.setColor(BFXSnake.BUTTON_COLOR);
        m_btnSnakeColor.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.setSnakeColor((m_gsm.getSnakeColorIndex() + 1) % BFXSnake.COLORS.length);
            }
        });

        m_btnPickupColor = new TextButton("PICKUP COLOR", m_gsm.getButtonStyle());
        m_btnPickupColor.pad(BFXSnake.BUTTON_PADDING);
        m_btnPickupColor.setColor(BFXSnake.BUTTON_COLOR);
        m_btnPickupColor.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.setPickupColor((m_gsm.getPickupColorIndex() + 1) % BFXSnake.COLORS.length);
            }
        });

        m_btnControllerState = new TextButton("CONTROLLER: " + m_gsm.getControllerState(), m_gsm.getButtonStyle());
        m_btnControllerState.pad(BFXSnake.BUTTON_PADDING);
        m_btnControllerState.setColor(BFXSnake.BUTTON_COLOR);
        m_btnControllerState.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.toggleControllerState();
            }
        });

        TextButton m_btnBack = new TextButton("BACK", m_gsm.getButtonStyle());
        m_btnBack.pad(BFXSnake.BUTTON_PADDING);
        m_btnBack.setColor(BFXSnake.BUTTON_COLOR);
        m_btnBack.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        TextButton m_btnRestoreDefVals = new TextButton("RESTORE DEFAULTS", m_gsm.getButtonStyle());
        m_btnRestoreDefVals.pad(BFXSnake.BUTTON_PADDING);
        m_btnRestoreDefVals.setColor(BFXSnake.BUTTON_COLOR);
        m_btnRestoreDefVals.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();

                m_gsm.setSnakeColor(0);
                m_gsm.setSnakeColor(m_gsm.getSnakeColorIndex());

                m_gsm.setPickupColor(1);
                m_gsm.setPickupColor(m_gsm.getPickupColorIndex());

                if(!m_gsm.isControllerOn()){
                    m_gsm.toggleControllerState();
                }
            }
        });

        m_headingFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_headingFont.getData().setScale(DEF_FONT_SIZE, DEF_FONT_SIZE);

        Label.LabelStyle headingLabelStyle = new Label.LabelStyle(m_headingFont, m_headingFont.getColor());
        Label titleLabel = new Label("SETTINGS", headingLabelStyle);

        Table titleTable = new Table();

        titleTable.top().padTop(titleLabel.getHeight());
        titleTable.setFillParent(true);

        titleTable.add(titleLabel);

        m_settingsStage.addActor(titleTable);

        Table selectionTable = new Table();

        selectionTable.top().padTop(Gdx.graphics.getHeight() / 6 + titleLabel.getHeight());
        selectionTable.setFillParent(true);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        //For the button widths
        Label largeLabel = new Label(BFXSnake.LARGE_LABEL_TEXT, headingLabelStyle);

        selectionTable.add(m_btnSnakeColor).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnPickupColor).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnControllerState).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnRestoreDefVals).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        selectionTable.add(m_btnBack).width(largeLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        selectionTable.row().padTop(padding);

        m_settingsStage.addActor(selectionTable);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        m_btnControllerState.setText("CONTROLLER: " + m_gsm.getControllerState());
        m_btnSnakeColor.getLabel().setColor(m_gsm.getSnakeColor());
        m_btnPickupColor.getLabel().setColor(m_gsm.getPickupColor());
    }

    @Override
    public void render(SpriteBatch sb) {
        m_settingsStage.draw();
    }

    @Override
    public void dispose() {
        m_settingsFont.dispose();
        m_headingFont.dispose();
        m_settingsStage.dispose();
    }
}
