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

    private GameStateManager m_gsm;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        m_gsm = gsm;

        m_gsm.saveDifficulty();
        m_gsm.flush();

        m_controller = new Controller(m_gsm.isControllerOn());

        m_snake = new Snake(m_controller);

        //Determine difficulty value
        int difficultyVal = 0;

        //Possible difficulty vlaues: 0, 1, 2, 3, 4
        //Diffculty value will be subtracted from snake's initial movement speed
        switch(m_gsm.getDifficulty()){
            case 0: //EASY
                difficultyVal = 0;
                break;

            case 1: //MEDIUM
                difficultyVal = 8;
                break;

            case 2: //HARD
                difficultyVal = 16;
                break;

            case 3: //PRO
                difficultyVal = 20;
                break;

            case 4: //INSANE
                difficultyVal = 24;
                break;

            default:
                break;
        }

        m_snake.setDifficultyVal(difficultyVal);

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
        if(DEBUG_MODE){
            if(Gdx.input.justTouched()){
                System.out.println("Just touched at: x = " + Gdx.input.getX() + ", y = " + Gdx.input.getY());
                System.out.print("Current snake head location: x = " + m_snake.getSnake().get(0).getX());
                System.out.println(", y = " + m_snake.getSnake().get(0).getY());
                System.out.println();
            }
        }

        if(!m_snake.isPaused()){
            if(m_controller.isUsingController()){
                if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN ) || Gdx.input.isKeyJustPressed(Input.Keys.S)
                        || (m_controller.isDownPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.DOWN);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)
                        || (m_controller.isUpPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.UP);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)
                        || (m_controller.isLeftPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.LEFT);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)
                        || (m_controller.isRightPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.RIGHT);
                    
                }else if((m_controller.isPausedPressed() && Gdx.input.justTouched()) || Gdx.input.isKeyJustPressed(Input.Keys.P)){
                    m_snake.pause();
                }else if(m_gameOver && Gdx.input.justTouched()){
                    m_gsm.set(new PlayState(m_gsm));
                }
            }else{ //Handle case when controller is disabled
                if(Gdx.input.justTouched()){
                    if(m_gameOver){
                        m_gsm.set(new PlayState(m_gsm));
                        return;
                    }

                    if(m_controller.isPausedPressed()){
                        m_snake.pause();
                        m_controller.setExitVisibility(true);
                        return;
                    }

                    SnakePart head = m_snake.getSnake().get(0);

                    int touchLocX = Gdx.input.getX();
                    int touchLocY = Gdx.graphics.getHeight() - Gdx.input.getY(); //Invert y-axis
                    int snakeHeadX = (int)head.getX();
                    int snakeHeadY = (int)head.getY();

                    //By default, assume same location
                    Snake.Directions currentDirection = head.getDirection();
                    Snake.Directions d1 = currentDirection;
                    Snake.Directions d2 = currentDirection;
                    Snake.Directions deducedDirection;

                    //Calculate as best as possible, the two most logical directions that the user meant the snake to go in.
                    //NB: should not do anything if the user taps directly on the snake head
                    if(touchLocX < snakeHeadX){
                        d1 = Snake.Directions.LEFT;
                    }else if(touchLocX > snakeHeadX){
                        d1 = Snake.Directions.RIGHT;
                    }

                    if(touchLocY < snakeHeadY){
                        d2 = Snake.Directions.DOWN;
                    }else if(touchLocY > snakeHeadY){
                        d2 = Snake.Directions.UP;
                    }

                    //Eliminate one of those directions by determining which one is an opposite direction or the same as the
                    //current direction
                    //NB: by mathematical deduction, one of the two directions will always be either an opposite direction
                    //or the same as the current direction
                    if(d1 != currentDirection && !m_snake.isOpposite(d1)){
                        deducedDirection = d1;
                    }else{
                        deducedDirection = d2;
                    }

                    //Make the snake go in the deduced direction
                    m_snake.setDirection(deducedDirection);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN ) || Gdx.input.isKeyJustPressed(Input.Keys.S)){
                    m_snake.setDirection(Snake.Directions.DOWN);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)){
                    m_snake.setDirection(Snake.Directions.UP);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)){
                    m_snake.setDirection(Snake.Directions.LEFT);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)){
                    m_snake.setDirection(Snake.Directions.RIGHT);
                }else if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
                    m_snake.pause();
                    m_controller.setExitVisibility(true);
                }
            }

            //Debugging tools
            if(DEBUG_MODE){
                if(Gdx.input.isKeyJustPressed(Input.Keys.G)) { //Allow debugger to grow snake at will
                    m_snake.grow();
                }else if(Gdx.input.isKeyJustPressed(Input.Keys.C)){ //Toggle controller
                    m_controller.toggleUse();
                }
            }

        }else if((m_controller.isPausedPressed() && Gdx.input.justTouched()) || Gdx.input.isKeyJustPressed(Input.Keys.P)){
            m_snake.resume();
            m_controller.setExitVisibility(false);
        }else if(m_controller.isExitPressed() && Gdx.input.justTouched()){
            m_gsm.set(new MenuState(m_gsm));
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
        sr.setColor(m_gsm.getPickupColor());
        Pickup pickup = m_snake.getPickup();
        sr.rect(pickup.getX(), pickup.getY(), pickup.getWidth(), pickup.getHeight());

        //Render snake
        sr.setColor(m_gsm.getSnakeColor());

        for(SnakePart part : m_snake.getSnake()){
            sr.rect(part.getX(), part.getY(), part.getWidth(), part.getHeight());
        }

        sr.end();
        sb.end();

        //Draw the controller
        m_controller.draw();

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
