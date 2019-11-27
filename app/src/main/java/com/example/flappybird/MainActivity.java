package com.example.flappybird;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean GAME_OVER=false;
    Intent tempIntent;
    //handling pause

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Score myScore = new Score();

        Intent myint = getIntent();
        String gameOver= myint.getStringExtra("gameOverConf");
        String maxScore = myint.getStringExtra("maxScore");
        TextView playText = findViewById(R.id.playText);
        TextView gameoverTxt = findViewById(R.id.gameOverTxt);
        TextView maxScoretxt = findViewById(R.id.ScoreTxt);
        TextView highScoreTxt = findViewById(R.id.highScoreTxt);
        playText.setText("PLAY");
        if((gameOver!=null) && (maxScore!=null)) {
            if(Integer.parseInt(maxScore)>myScore.highScore(getBaseContext())){
                myScore.storeHighScore(getBaseContext(),Integer.parseInt(maxScore));
            }
            gameoverTxt.setText(gameOver);
            maxScoretxt.setText("Your Score: " + maxScore);

            playText.setText("PLAY AGAIN");
            GAME_OVER=true;

        }
        highScoreTxt.setText("Highscore: "+myScore.highScore(getBaseContext()));

    }
    public void startGame(View view){

        if(GAME_OVER){
            startActivity(tempIntent);
            finish();
        }
        else{
            Intent myIntent = new Intent(this, StartGame.class);
            tempIntent = myIntent;
            startActivity(myIntent);
            finish();
            GAME_OVER=false;
        }

    }






}
