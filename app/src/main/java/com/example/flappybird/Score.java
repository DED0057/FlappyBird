package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;

public class Score {
    private static final String PREF_DEFAULT = "com.example.flappybird.scorePref";
    private static final String HIGH_SCORE = "high_score";
    public static boolean NEW_BEST_SCORE = false;

    public static int highScore(Context context) {
        SharedPreferences p = context.getSharedPreferences(PREF_DEFAULT, Context.MODE_PRIVATE);
        return p.getInt(HIGH_SCORE, 0);
    }

    public static void storeHighScore(Context context, int score) {
        NEW_BEST_SCORE = true;
        SharedPreferences p = context.getSharedPreferences(PREF_DEFAULT,Context.MODE_PRIVATE);
        p.edit().putInt(HIGH_SCORE, score).commit();
    }
}
