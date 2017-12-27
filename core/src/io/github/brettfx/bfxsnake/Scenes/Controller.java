package io.github.brettfx.bfxsnake.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.brettfx.bfxsnake.BFXSnake;

/**
 * @author brett
 * @since 12/26/2017
 */

public class Controller {
    private static final String UP = "directional_pad/up_arrow_transparent.png";
    private static final String DOWN = "directional_pad/down_arrow_transparent.png";
    private static final String LEFT = "directional_pad/left_arrow_transparent.png";
    private static final String RIGHT = "directional_pad/right_arrow_transparent.png";

    private static final float PADDING_TOP = 5f;
    private static final float PADDING_LEFT = 5f;
    private static final float PADDING_BOTTOM = 5f;
    private static final float PADDING_RIGHT = 5f;

    private static final float ARROW_WIDTH = 150f; //40
    private static final float ARROW_HEIGHT = 150f; //40

    private Viewport m_viewport;
    private Stage m_stage;

    private boolean m_upPressed,
            m_downPressed,
            m_leftPressed,
            m_rightPressed;

    public Controller(){
        OrthographicCamera cam = new OrthographicCamera();
        m_viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        m_stage = new Stage(m_viewport, BFXSnake.m_batch);

        /*m_stage.addListener(new InputListener()
        {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.UP:
                    case Input.Keys.W:
                        m_upPressed = true;
                        break;

                    case Input.Keys.DOWN:
                    case Input.Keys.S:
                        m_downPressed = true;
                        break;

                    case Input.Keys.LEFT:
                    case Input.Keys.A:
                        m_leftPressed = true;
                        break;

                    case Input.Keys.RIGHT:
                    case Input.Keys.D:
                        m_rightPressed = true;
                        break;
                }

                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.UP:
                    case Input.Keys.W:
                        m_upPressed = false;
                        break;

                    case Input.Keys.DOWN:
                    case Input.Keys.S:
                        m_downPressed = false;
                        break;

                    case Input.Keys.LEFT:
                    case Input.Keys.A:
                        m_leftPressed = false;
                        break;

                    case Input.Keys.RIGHT:
                    case Input.Keys.D:
                        m_rightPressed = false;
                        break;
                }

                return true;
            }
        });*/

        Gdx.input.setInputProcessor(m_stage);

        //Create a table that will hold the controller arrows (3x3 matrix)
        Table table = new Table();
        table.left().bottom();

        //Create up_arrow image
        Image upImg = new Image(new Texture(UP));
        upImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);


        //Add input listener to up_arrow image to act as a button
        upImg.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                m_upPressed = true;

                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_upPressed = false;
            }
        });

        //Create down_arrow image
        Image downImg = new Image(new Texture(DOWN));
        downImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to down_arrow image to act as a button
        downImg.addListener(new InputListener()
        {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                m_downPressed = true;

                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_downPressed = false;
            }
        });

        //Create left_arrow image
        Image leftImg = new Image(new Texture(LEFT));
        leftImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to left_arrow image to act as a button
        leftImg.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                m_leftPressed = true;

                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_leftPressed = false;
            }
        });

        //Create right_arrow image
        Image rightImg = new Image(new Texture(RIGHT));
        rightImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to right_arrow image to act as a button
        rightImg.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                m_rightPressed = true;

                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_rightPressed = false;
            }
        });

        //Populate the 3x3 table with arrow images to simulate directional pad controller
        table.add();
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        table.add();

        table.row().pad(PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT);

        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());

        table.row().padBottom(PADDING_BOTTOM);

        table.add();
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        table.add();

        table.pack();

        //Add the table to the m_stage
        m_stage.addActor(table);
    }

    public void draw()
    {
        m_stage.draw();
    }

    public boolean isDownPressed()
    {
        return m_downPressed;
    }

    public boolean isLeftPressed()
    {
        return m_leftPressed;
    }

    public boolean isUpPressed()
    {
        return m_upPressed;
    }

    public boolean isRightPressed()
    {
        return m_rightPressed;
    }

    public void resize(int width, int height)
    {
        m_viewport.update(width, height);
    }

    public void dispose()
    {
        m_stage.dispose();
    }
}
