package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Scenes.Controller;
import io.github.brettfx.bfxsnake.Sprites.Pickup;
import io.github.brettfx.bfxsnake.Sprites.Snake;
import io.github.brettfx.bfxsnake.Sprites.SnakePart;

/**
 * @author brett
 * @since 12/24/2017
 */
public class PlayState extends State {

    public static boolean DEBUG_MODE = false;

    private Snake m_snake;

    private Controller m_controller;

    private boolean m_gameOver;

    private Stage m_stage;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        m_controller = new Controller(true);

        m_snake = new Snake(m_controller);

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_stage = new Stage(viewport);

        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        bitmapFont.getData().setScale(BFXSnake.FONT_SIZE, BFXSnake.FONT_SIZE);

        //Setting up the font of the text to be displayed on the gameover screen
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());

        Table table = new Table();
        table.center();

        //Table will take up entire stage
        table.setFillParent(true);

        //Create game over label
        Label gameOverLabel = new Label("GAME OVER", font);
        table.add(gameOverLabel).expandX();

        //Create a play again label
        Label playAgainLabel = new Label("Tap to Play Again", font);
        table.row();

        //Add a bit of padding to top of play again label
        table.add(playAgainLabel).expandX().padTop(10f);

        m_stage.addActor(table);

        m_gameOver = false;
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
            }else if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
                m_snake.pause();
            }else if(m_gameOver && Gdx.input.justTouched()){
                m_gsm.set(new PlayState(m_gsm));
            }

            //Debugging tools
            if(DEBUG_MODE){
                if(Gdx.input.isKeyJustPressed(Input.Keys.A)) { //Allow debugger to grow snake at will
                    m_snake.grow();
                }else if(Gdx.input.isKeyJustPressed(Input.Keys.C)){ //Toggle controller
                    m_controller.toggleUse();
                }
            }

        }else if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            m_snake.resume();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        m_snake.update(dt);
        m_gameOver = m_snake.isColliding();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(m_cam.combined);
        sb.begin();

        ShapeRenderer sr = m_snake.getShapeRenderer();
        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Filled);

        //Render controller's overlay
        if(m_controller.isUsingController()){
            sr.setColor(Color.GRAY);
            sr.rect(0, 0, m_controller.getWidth(), Gdx.graphics.getHeight());
        }

        //Render pickup
        sr.setColor(Color.RED);
        Pickup pickup = m_snake.getPickup();
        sr.rect(pickup.getX(), pickup.getY(), pickup.getWidth(), pickup.getHeight());

        //Render snake
        sr.setColor(Color.GREEN);

        for(SnakePart part : m_snake.getSnake()){
            sr.rect(part.getX(), part.getY(), part.getWidth(), part.getHeight());
        }

        sr.end();
        sb.end();

        if(m_controller.isUsingController()){
            m_controller.draw();
        }

        //Determine if game over and show game over if it is game over
        if(m_gameOver){
            m_stage.draw();
        }
    }

    @Override
    public void dispose() {
        m_snake.dispose();
        m_controller.dispose();
        m_stage.dispose();
    }
}
