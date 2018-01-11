package io.github.brettfx.bfxsnake.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.github.brettfx.bfxsnake.BFXSnake;

import static io.github.brettfx.bfxsnake.BFXSnake.FONT_SIZE;

/**
 * Created by Brett on 5/31/2016.
 *
 *  Keep score with this class. Embodying all the essential methods that a score class would embody,
 *  this class also writes to a file so that a high score can be recorded.
 */
public class Score{
    //Object values used for incrementing score
    private static final int PICKUP_VALUE = 1;

    //Store the high score for future use
    private static Preferences preferences = Gdx.app.getPreferences(BFXSnake.PREFS_NAME);

    private static int highScore = preferences.getInteger("HighScore", 0);

    private BitmapFont scoreFont;

    private int currentScore;

    public Score(Color color){
        currentScore = 0;

        //Get highScore from filed
        highScore = preferences.getInteger("HighScore", 0);

        scoreFont = new BitmapFont(Gdx.files.internal(BFXSnake.SCORE_FONT));
        scoreFont.setColor(color);
        scoreFont.getData().setScale(FONT_SIZE, FONT_SIZE);
    }

    private void setHighScore(){
        if(currentScore > highScore) {
            highScore = currentScore;

            preferences.putInteger("HighScore", highScore);
            preferences.flush();
        }
    }

    public static void resetHighScore(){
        preferences.putInteger("HighScore", 0);
        preferences.flush();
    }

    public void add(){
        currentScore += PICKUP_VALUE;
        setHighScore();
    }

    public int getCurrentScore(){
        return currentScore;
    }

    public static int getHighScore(){
        return highScore;
    }

    public BitmapFont getScoreFont(){
        return scoreFont;
    }
}
