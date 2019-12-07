/*
 * *
 *  * Created by Matyas Dedek (DED0057)
 *  * 2019 .
 *  * Last modified 7.12.19 22:55
 *
 *
 */

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

    /**
     * Manages pause of the music, when user leaves the game
     */
    @Override
    protected void onPause() {
        super.onPause();
        isPaused=true;
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

    /**
     * Manages start of the music when user returns to the game
     */
    @Override
    protected void onResume() {
        super.onResume();
        continueBGMusic = false;
        if(isPaused)
        musicManager.start(this, R.raw.background_music_takeonme,true);
        else musicManager.start(this, R.raw.background_music_takeonme,false);
        //onResume is called even when starting the game. Get around that.
        if(gameView.getGameActive()) {
            showPopup(gameView);
        }

    }

    /**
     * shows popup menu when the user returns to the game
     * @param v
     */
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                gameView.resume();
                isPaused=false;
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pause_menu, popup.getMenu());
        popup.show();
    }

}
