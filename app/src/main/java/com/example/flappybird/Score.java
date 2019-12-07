/*
 * *
 *  * Created by Matyas Dedek (DED0057)
 *  * 2019 .
 *  * Last modified 7.12.19 23:23
 *
 *
 */

package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * represents player's score and manages it's loading and saving. Each new highscore is stored in shared preferences
 */
public class Score {
    private static final String PREF_DEFAULT = "com.example.flappybird.scorePref";
    private static final String HIGH_SCORE = "high_score";
    public static boolean NEW_BEST_SCORE = false;
    private static int currentScore;
    Score(){
        currentScore =0;

    }

    /**
     * add 1 score to the current score
     */
    public void addScore(){
        this.setCurrentScore(this.getCurrentScore()+1);
    }

    /**
     * returns current score
     * @return int
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * sets the current score
     * @param currentScore
     */
    public static void setCurrentScore(int currentScore) {
        Score.currentScore = currentScore;
    }

    /**
     * returns current highscore from shared preferences
     * @param context
     * @return static int
     */
    public static int highScore(Context context) {
        SharedPreferences p = context.getSharedPreferences(PREF_DEFAULT, Context.MODE_PRIVATE);
        return p.getInt(HIGH_SCORE, 0);
    }

    /**
     * stores a new highscore in shared preferences
     * @param context
     * @param score
     */
    public static void storeHighScore(Context context, int score) {
        NEW_BEST_SCORE = true;
        SharedPreferences p = context.getSharedPreferences(PREF_DEFAULT,Context.MODE_PRIVATE);
        p.edit().putInt(HIGH_SCORE, score).commit();
    }

    /**
     * checks whether the current score is higher than highscore
     * @param context
     */
    public void checkForNewHighscore(Context context){
        if(currentScore>highScore(context)){
            storeHighScore(context,currentScore);
        }
    }
}
