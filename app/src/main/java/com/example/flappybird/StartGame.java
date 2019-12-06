package com.example.flappybird;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class StartGame extends Activity {

    GameView gameView;
    private static MusicManager musicManager;
    boolean continueBGMusic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        continueBGMusic=true;
        musicManager.start(this,R.raw.background_music_takeonme);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!continueBGMusic)
            MusicManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        continueBGMusic=false;

    }
}
