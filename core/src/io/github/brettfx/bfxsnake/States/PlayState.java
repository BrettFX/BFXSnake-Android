package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import io.github.brettfx.bfxsnake.Scenes.Controller;
import io.github.brettfx.bfxsnake.Sprites.Pickup;
import io.github.brettfx.bfxsnake.Sprites.Snake;
import io.github.brettfx.bfxsnake.Sprites.SnakePart;

import static io.github.brettfx.bfxsnake.BFXSnake.NOTIFICATION_COLOR;
import static io.github.brettfx.bfxsnake.BFXSnake.OPACITY;

/**
 * @author brett
 * @since 12/24/2017
 */
public class PlayState extends State {

    //The closer to zero the slower the animation speed
    private static final float NOTIFICATION_FADE_SPEED = 0.01f;
    public static boolean DEBUG_MODE = false;

    private Snake m_snake;

    private Controller m_controller;

    private boolean m_gameOver;

    private Stage m_gameOverStage;
    private Stage m_scoreStage;
    private Stage m_notificationStage;
    private Stage m_pauseStage;

    private Label m_scoreLabel;
    private boolean m_highScoreNotified;

    private Label m_notificationLabel;
    private float m_notificationAlpha;

    private BitmapFont m_bitmapFont;

    private TextButton m_btnPlayAgain;
    private TextButton m_btnBack;
    private TextButton m_btnResume;
    private TextButton m_btnExit;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        m_gsm.setDifficulty();
        m_gsm.flush();

        m_controller = new Controller(m_gsm);

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
                difficultyVal = 10;
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
        m_gameOverStage = new Stage(viewport);
        m_scoreStage = new Stage(viewport);
        m_notificationStage = new Stage(viewport);
        m_pauseStage = new Stage(viewport);

        //Need to create an input multiplexer to prevent from overwriting other input processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(m_gameOverStage);
        inputMultiplexer.addProcessor(m_controller.getStage());
        inputMultiplexer.addProcessor(m_pauseStage);

        Gdx.input.setInputProcessor(inputMultiplexer);

        m_bitmapFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_bitmapFont.getData().setScale(BFXSnake.DEF_FONT_SIZE, BFXSnake.DEF_FONT_SIZE);

        m_gsm.setTextButtonFont(m_bitmapFont);

        //Create a play again button
        m_btnPlayAgain = new TextButton("PLAY AGAIN", m_gsm.getButtonStyle());
        m_btnPlayAgain.pad(BFXSnake.BUTTON_PADDING);
        m_btnPlayAgain.setColor(BFXSnake.BUTTON_COLOR);
        m_btnPlayAgain.setVisible(false);
        m_btnPlayAgain.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                Score.flush(); //Flush the score to write a new high score (if there is one)
                m_gsm.set(new PlayState(m_gsm));
            }
        });

        m_btnBack = new TextButton("BACK TO MENU", m_gsm.getButtonStyle());
        m_btnBack.pad(BFXSnake.BUTTON_PADDING);
        m_btnBack.setColor(BFXSnake.BUTTON_COLOR);
        m_btnBack.setVisible(false);
        m_btnBack.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        m_btnExit = new TextButton("EXIT", m_gsm.getButtonStyle());
        m_btnExit.pad(BFXSnake.BUTTON_PADDING);
        m_btnExit.setColor(BFXSnake.BUTTON_COLOR);
        m_btnExit.getLabel().setColor(Color.RED);
        m_btnExit.setVisible(false);
        m_btnExit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        m_btnResume = new TextButton("RESUME", m_gsm.getButtonStyle());
        m_btnResume.pad(BFXSnake.BUTTON_PADDING);
        m_btnResume.setColor(BFXSnake.BUTTON_COLOR);
        m_btnResume.setVisible(false);
        m_btnResume.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_DOWN_SOUND, Sound.class).play();
                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                BFXSnake.m_assetManager.get(BFXSnake.BUTTON_CLICK_UP_SOUND, Sound.class).play();
                m_snake.resume();
                m_btnExit.setVisible(false);
                m_btnResume.setVisible(false);
            }
        });

        //Setting up the font of the text to be displayed on the gameover screen
        Label.LabelStyle fontStyle = new Label.LabelStyle(m_bitmapFont, m_bitmapFont.getColor());

        Table gameOverTable = new Table();
        gameOverTable.center();
        gameOverTable.top().padTop(Gdx.graphics.getHeight() / 6);

        //Table will take up entire stage
        gameOverTable.setFillParent(true);

        //Create game over label
        Label gameOverLabel = new Label("GAME OVER", fontStyle);
        gameOverTable.add(gameOverLabel).expandX();

        m_gameOverStage.addActor(gameOverTable);

        float padding = Gdx.graphics.getWidth() / BFXSnake.SCALE_FACTOR;

        //For the button widths
        Label smallLabel = new Label(BFXSnake.SMALL_LABEL_TEXT, new Label.LabelStyle(m_bitmapFont, m_bitmapFont.getColor()));

        gameOverTable.row().padTop(Gdx.graphics.getHeight() / 6);
        gameOverTable.add(m_btnPlayAgain).width(smallLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        gameOverTable.row().padTop(padding);

        gameOverTable.add(m_btnBack).width(smallLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);

        m_gameOver = false;

        m_highScoreNotified = false;

        //Create a table for the exit button
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();

        pauseTable.add(m_btnResume).width(smallLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);
        pauseTable.row().padTop(padding);
        pauseTable.add(m_btnExit).width(smallLabel.getWidth() * BFXSnake.DEF_BUTTON_WIDTH_SCALE);

        m_pauseStage.addActor(pauseTable);

        Table scoreTable = new Table();
        scoreTable.top().left();
        scoreTable.setFillParent(true);

        Label.LabelStyle scoreStyle = new Label.LabelStyle(m_snake.getScore().getScoreFont(), m_snake.getScore().getScoreFont().getColor());
        m_scoreLabel = new Label(String.valueOf(m_snake.getScore().getCurrentScore()), scoreStyle);
        m_scoreLabel.setColor(m_scoreLabel.getColor().r,
                m_scoreLabel.getColor().g,
                m_scoreLabel.getColor().b,
                OPACITY);

        scoreTable.add(m_scoreLabel).pad(10f);

        m_scoreStage.addActor(scoreTable);

        Table notificationTable = new Table();
        notificationTable.center();
        notificationTable.setFillParent(true);

        m_notificationLabel = new Label("", fontStyle);
        m_notificationLabel.setColor(NOTIFICATION_COLOR);

        notificationTable.add(m_notificationLabel);

        m_notificationStage.addActor(notificationTable);

        //Initially invisible
        m_notificationAlpha = 0.0f;

        if(DEBUG_MODE){
            m_gameOverStage.setDebugAll(true);
            m_scoreStage.setDebugAll(true);
            m_notificationStage.setDebugAll(true);
        }
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
                    m_btnResume.setVisible(true);
                    m_btnExit.setVisible(true);
                }
            }else{ //Handle case when controller is disabled
                if(Gdx.input.justTouched()){
                    if(m_gameOver){
                        return;
                    }

                    if(m_controller.isPausedPressed()){
                        m_snake.pause();
                        m_btnResume.setVisible(true);
                        m_btnExit.setVisible(true);
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
                    m_btnResume.setVisible(true);
                    m_btnExit.setVisible(true);
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
            m_btnResume.setVisible(false);
            m_btnExit.setVisible(false);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        m_snake.update(dt);

        //Draw the notification table if required
        //Show new high score notification if the high score has changed and the notification has not be shown yet
        if(m_snake.getScore().isNewHighScore() && !m_highScoreNotified){
            m_notificationLabel.setText("NEW HIGH SCORE!");
            m_notificationAlpha = 1.0f;
            m_highScoreNotified = true;
        }

        //Displays a fade-out animation for the notification
        m_notificationLabel.setColor(NOTIFICATION_COLOR.r, NOTIFICATION_COLOR.g, NOTIFICATION_COLOR.b, m_notificationAlpha);
        m_notificationAlpha = m_notificationAlpha >= 0 ? m_notificationAlpha - NOTIFICATION_FADE_SPEED : 0.0f;

        m_gameOver = m_snake.isColliding();
        m_scoreLabel.setText(String.valueOf(m_snake.getScore().getCurrentScore()));
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
            final Color baseColor = Color.DARK_GRAY;
            sr.setColor(baseColor.r, baseColor.g, baseColor.b, BFXSnake.OPACITY);
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

        //Draw the score
        m_scoreStage.draw();

        //Draw the notification stage (may be invisible)
        m_notificationStage.draw();

        m_pauseStage.draw();

        //Determine if game over and show game over if it is game over
        if(m_gameOver){

            //Render button overlay for play again button
            m_btnPlayAgain.setVisible(true);

            //Render button overlay for back button
            m_btnBack.setVisible(true);

            m_gameOverStage.draw();
        }
    }

    @Override
    public void dispose() {
        m_snake.dispose();
        m_controller.dispose();
        m_bitmapFont.dispose();
        m_gameOverStage.dispose();
        m_scoreStage.dispose();
        m_notificationStage.dispose();
        m_pauseStage.dispose();
    }
}
