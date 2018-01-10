package io.github.brettfx.bfxsnake.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.States.MenuState;
import io.github.brettfx.bfxsnake.States.PlayState;

/**
 * @author brett
 * @since 12/26/2017
 */

public class Controller {
    private static final String UP = "directional_pad/up_arrow_transparent.png";
    private static final String DOWN = "directional_pad/down_arrow_transparent.png";
    private static final String LEFT = "directional_pad/left_arrow_transparent.png";
    private static final String RIGHT = "directional_pad/right_arrow_transparent.png";
    private static final String PAUSE_BUTTON = "pause_button/pause_play_img_white.png";

    private static final float PADDING_TOP = 20f; //5
    private static final float PADDING_LEFT = 20f; //5
    private static final float PADDING_BOTTOM = 20f; //5
    private static final float PADDING_RIGHT = 20f; //5

    private static final float ARROW_WIDTH = 150f; //40
    private static final float ARROW_HEIGHT = 150f; //40

    private Viewport m_viewport;
    private Stage m_stage;

    private Image m_leftImg;
    private Image m_rightImg;
    private Image m_downImg;
    private Image m_upImg;
    private Image m_pauseButton;

    private Label m_exitLabel;

    private boolean m_usingController;

    private boolean m_upPressed,
            m_downPressed,
            m_leftPressed,
            m_rightPressed,
            m_pausedPressed,
            m_exitPressed;

    public Controller(boolean usingController){
        OrthographicCamera cam = new OrthographicCamera();
        m_viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        m_stage = new Stage(m_viewport, BFXSnake.m_batch);

        //Gdx.input.setInputProcessor(m_stage);

        m_usingController = usingController;

        //Create up_arrow image
        m_upImg = new Image(new Texture(UP));
        m_upImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to up_arrow image to act as a button
        m_upImg.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_upPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_upPressed = false;
            }
        });

        //Create down_arrow image
        m_downImg = new Image(new Texture(DOWN));
        m_downImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to down_arrow image to act as a button
        m_downImg.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_downPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_downPressed = false;
            }
        });

        //Create left_arrow image
        m_leftImg = new Image(new Texture(LEFT));
        m_leftImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to left_arrow image to act as a button
        m_leftImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_leftPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_leftPressed = false;
            }
        });

        //Create right_arrow image
        m_rightImg = new Image(new Texture(RIGHT));
        m_rightImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to right_arrow image to act as a button
        m_rightImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_rightPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_rightPressed = false;
            }
        });

        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        bitmapFont.getData().setScale(BFXSnake.FONT_SIZE, BFXSnake.FONT_SIZE);

        //Setting up the font of the text to be displayed on the gameover screen
        Label.LabelStyle exitLabelStyle = new Label.LabelStyle(bitmapFont, Color.RED);

        m_exitLabel = new Label("EXIT", exitLabelStyle);
        m_exitLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return (m_exitPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_exitPressed = false;
            }
        });

        //Initially the exit label should not be visible
        m_exitLabel.setVisible(false);

        //Create a table for the exit button
        Table exitTable = new Table();
        exitTable.setFillParent(true);
        exitTable.top();
        exitTable.add(m_exitLabel).size(m_exitLabel.getWidth(), m_exitLabel.getHeight());

        m_stage.addActor(exitTable);

        //Create the pause button to be the same size of the arrow buttons
        m_pauseButton = new Image(new Texture(PAUSE_BUTTON));
        m_pauseButton.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        m_pauseButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_pausedPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_pausedPressed = false;
            }
        });

        //Create a table for the pause button
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.top().right();
        pauseTable.add(m_pauseButton).size(m_pauseButton.getWidth(), m_pauseButton.getHeight());

        m_stage.addActor(pauseTable);

        //Create a table that will hold the controller arrows (3x3 matrix) if the controller is enabled
        if(m_usingController){
            Table controlsTable = new Table();
            controlsTable.left().bottom();

            //Populate the 3x3 table with arrow images to simulate directional pad controller
            controlsTable.add();
            controlsTable.add(m_upImg).size(m_upImg.getWidth(), m_upImg.getHeight());
            controlsTable.add();

            controlsTable.row().pad(PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT);

            controlsTable.add(m_leftImg).size(m_leftImg.getWidth(), m_leftImg.getHeight());
            controlsTable.add();
            controlsTable.add(m_rightImg).size(m_rightImg.getWidth(), m_rightImg.getHeight());

            controlsTable.row().padBottom(PADDING_BOTTOM);

            controlsTable.add();
            controlsTable.add(m_downImg).size(m_downImg.getWidth(), m_downImg.getHeight());
            controlsTable.add();

            controlsTable.pack();

            //Add the table to the m_stage
            m_stage.addActor(controlsTable);
        }

        if(PlayState.DEBUG_MODE){
            m_stage.setDebugAll(true);
        }
    }

    public Stage getStage(){
        return m_stage;
    }

    public boolean isUsingController(){
        return m_usingController;
    }

    public void toggleUse(){
        m_usingController = !m_usingController;
    }

    public void draw(){
        m_stage.draw();
    }

    public boolean isDownPressed(){
        return m_downPressed;
    }

    public boolean isLeftPressed(){
        return m_leftPressed;
    }

    public boolean isUpPressed(){
        return m_upPressed;
    }

    public boolean isRightPressed(){
        return m_rightPressed;
    }

    public boolean isPausedPressed(){
        return m_pausedPressed;
    }

    public boolean isExitPressed(){
        return m_exitPressed;
    }

    public void setExitVisibility(boolean b){
        m_exitLabel.setVisible(b);
    }

    public void resize(int width, int height){
        m_viewport.update(width, height);
    }

    public float getWidth(){
        float widths = m_rightImg.getWidth() + m_leftImg.getHeight() + m_upImg.getWidth() + m_downImg.getWidth();
        return widths - (PADDING_RIGHT);
    }

    public void dispose(){
        m_stage.dispose();
    }
}
