package com.example.flappybird;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartGame extends AppCompatActivity {


    GameView gameView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(getApplicationContext());
        setContentView(gameView);
    }

   /* @Override
    protected void onPause() {
        super.onPause();
        Log.d("pauzujes?!","truee");
       //call pause view
        showPopup(gameView);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }*/
    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this,v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.pause_menu,popupMenu.getMenu());
        popupMenu.show();
    }


    public GameView getGameView() {
        return gameView;
    }
}
