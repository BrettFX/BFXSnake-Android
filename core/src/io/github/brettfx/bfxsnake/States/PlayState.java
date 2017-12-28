package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.brettfx.bfxsnake.Scenes.Controller;
import io.github.brettfx.bfxsnake.Sprites.Snake;
import io.github.brettfx.bfxsnake.Sprites.SnakePart;

/**
 * @author brett
 * @since 12/24/2017
 */
public class PlayState extends State {

    private static final boolean DEBUG_MODE = false;

    private Snake m_snake;

    private Controller m_controller;

    private boolean m_gameOver;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        m_snake = new Snake();
        m_controller = new Controller();
    }

    @Override
    public void handleInput() {
        if(!m_snake.isPaused()){
            if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || (m_controller.isDownPressed() && Gdx.input.justTouched())){
                m_snake.setDirection(Snake.Directions.DOWN);

            }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || (m_controller.isUpPressed() && Gdx.input.justTouched())){
                m_snake.setDirection(Snake.Directions.UP);

            }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || (m_controller.isLeftPressed() && Gdx.input.justTouched())){
                m_snake.setDirection(Snake.Directions.LEFT);

            }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || (m_controller.isRightPressed() && Gdx.input.justTouched())){
                m_snake.setDirection(Snake.Directions.RIGHT);
            }else if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
                m_snake.grow();
            }else if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
                m_snake.pause();
            }
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            m_snake.resume();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        m_snake.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(m_cam.combined);
        sb.begin();

        ShapeRenderer sr = m_snake.getShapeRenderer();
        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GREEN);

        for(SnakePart part : m_snake.getSnake()){
            sr.rect(part.getX(), part.getY(), part.getWidth(), part.getHeight());
        }

        sr.end();
        sb.end();

        m_controller.draw();
    }

    @Override
    public void dispose() {
        m_snake.dispose();
        m_controller.dispose();
    }
}
