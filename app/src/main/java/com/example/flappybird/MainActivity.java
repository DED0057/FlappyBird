package com.example.flappybird;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

        }
        highScoreTxt.setText("Highscore: "+myScore.highScore(getBaseContext()));

    }
    public void startGame(View view){
        Intent myIntent = new Intent(this, StartGame.class);
        startActivity(myIntent);
        finish();
    }


}
