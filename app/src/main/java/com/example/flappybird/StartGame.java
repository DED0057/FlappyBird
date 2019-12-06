package com.example.flappybird;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StartGame extends Activity {
    boolean gameActive=false;
    public static boolean isPaused;
    GameView gameView;
    private static MusicManager musicManager;
    boolean continueBGMusic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        continueBGMusic=true;
        isPaused=false;
        gameView = new GameView(this);
        setContentView(gameView);
        gameActive=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!continueBGMusic)
            musicManager.pause();
        if(gameView.getGameActive()) {
            try {
                gameView.pause();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        continueBGMusic = false;
        musicManager.start(this, R.raw.background_music_takeonme);
        //onResume is called even when starting the game. Get around that.
        if(gameView.getGameActive()) {
            //gameView.resume();
            showPopup(gameView);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                gameView.resume();
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pause_menu, popup.getMenu());
        popup.show();
    }

public boolean isPaused(){
        return isPaused;
    }


}
