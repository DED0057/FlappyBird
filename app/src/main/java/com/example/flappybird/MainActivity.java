package com.example.flappybird;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
=======
>>>>>>> parent of 20f9643... #ADD pause menu and edit ground sprite
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
<<<<<<< HEAD
    boolean GAME_OVER=false;
    Intent tempIntent;
    Thread myThread;
    StartGame myGame;
    boolean isActive;
=======

>>>>>>> parent of 20f9643... #ADD pause menu and edit ground sprite
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isActive = true;
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
<<<<<<< HEAD
        isActive=true;
                if(GAME_OVER){
                    startActivity(tempIntent);
                    finish();
                }
                else{
                    myGame = new StartGame();
                    Intent myIntent = new Intent(getApplicationContext(), myGame.getClass());
                    tempIntent = myIntent;
                    startActivity(myIntent);
                    finish();
                    GAME_OVER=false;
                }



=======
        Intent myIntent = new Intent(this, StartGame.class);
        startActivity(myIntent);
        finish();
>>>>>>> parent of 20f9643... #ADD pause menu and edit ground sprite
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

<<<<<<< HEAD
   /* @Override
    protected void onPause() {
        super.onPause();

       // showPopup(myGame.getGameView());
        Log.d("pauzujes1?!","truee");
        isActive = false;
        while(true) {
            try {
                Toast.makeText(getApplicationContext(), "joining thread", Toast.LENGTH_LONG).show();
               // myThread.wait(10);
                myThread.join();
                showPopup(myGame.getGameView());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        myThread = null;



    }*/
    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this,v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.pause_menu,popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.resume_game:{
                isActive=true;
                myThread = new Thread();
                myThread.start();
                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }

    }
=======
>>>>>>> parent of 20f9643... #ADD pause menu and edit ground sprite
}
