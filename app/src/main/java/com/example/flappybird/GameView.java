package com.example.flappybird;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;


public class GameView extends View {

    private static boolean pause_flg = false;
    public static boolean gameActive = false;
    static boolean gameover=false;
    Handler handler;
    Runnable runnable;
    Runnable runnable2;
    Paint txtPaint;
    Typeface plainFont;
    Typeface boldFont;
    final int UPDATE_MILIS=15;
    Display display;
    Point point;
    //device width and height
    int dWidth, dHeight;
    //game objects(Assets)
    Bird bird;
    Background background;
    Ground ground;
    Score score;
    boolean gameState = false;

    //setting the gap between the top and bottom tube
    int tubeGap = 350;
    int minTubeOffset,maxTubeOffset;
    int tubeVelocity = 15;
    int tubeOffset;

    //pipemanager handles pipe updates and other stuff
    //PipeManager pipeManager;
    PipeManager[] pipeManagers;



    public GameView(Context context) {
        super(context);

        handler= new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //check if game is not paused
                if(!is_paused())
                invalidate();
            }
        };
        runnable2 = new Runnable() {
            @Override
            public void run() {
                if(CollisionDetection()) gameover=true;
            }
        };

        score = new Score();
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        //set background of the game screen
        background = new Background(getResources(),R.mipmap.background);
        background.setCollisionRect(new Rect(0,0,dWidth,dHeight));

        ground = new Ground(getResources(),R.drawable.ground);
        ground.setBitmap(Bitmap.createScaledBitmap(ground.getBitmap(),dWidth,400,true));
        ground.setCollisionRect(new Rect(0,dHeight-200,dWidth,dHeight));

        //set the birds bitmap and put him at the leftmost side and in the middle of the Y axis.
        bird = new Bird(getResources(),R.drawable.bird,R.drawable.birdfalling,dHeight);

        tubeOffset = dWidth;

        //tubes have variable length. set the min and max length here
        minTubeOffset = tubeGap/2;
        maxTubeOffset = dHeight-(minTubeOffset + tubeGap);

        pipeManagers = new PipeManager[2];
        pipeManagers[0]=new PipeManager(getResources(),tubeGap,minTubeOffset,maxTubeOffset,tubeVelocity,dWidth,dHeight);
        pipeManagers[1]=new PipeManager(getResources(),tubeGap,minTubeOffset,maxTubeOffset,tubeVelocity,dWidth,dHeight);

        pipeManagers[0].reset(1);
        pipeManagers[1].reset(2);

        txtPaint = new Paint();
        plainFont = Typeface.create("Arial",Typeface.ITALIC);
        boldFont = Typeface.create(plainFont,Typeface.BOLD);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setTypeface(boldFont);
        txtPaint.setTextSize(36);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint bgpaint = new Paint();
        bgpaint.setColor(Color.BLACK);
        canvas.drawBitmap(background.getBitmap(),null,background.getCollisionRect(),null);
        handler.postDelayed(runnable,UPDATE_MILIS);
        handler.postDelayed(runnable2,UPDATE_MILIS);
        if(gameover) gameOver();
        if(gameState){

            if(bird.birdIsOnScreen(dHeight)){
                bird.update();
            }
            for(PipeManager pm:pipeManagers){
                if(pm.isOffScreen()) score.addScore();
                pm.update();
                canvas.drawBitmap(pm.getTopPipe().getBitmap(),pm.getXpos(),pm.getYpos()-pm.getTopPipe().getHeight(),null);
                canvas.drawBitmap(pm.getBottomPipe().getBitmap(),pm.getXpos(),pm.getYpos()+pm.getPipeGap(),null);
            }

        }
        //display the bird
        if(bird.isFalling() && bird.getVelocity()>5){
            canvas.drawBitmap(bird.getBackup(),bird.getBirdXpos(),bird.getBirdYpos(),null);
        }
            else{
            canvas.drawBitmap(bird.getBitmap(), bird.getBirdXpos(), bird.getBirdYpos(), null);
        }
        // 30 is the offset, so the game is a bit easier. it means that 50pixels from each side wont count in collision calculation
        bird.setCollisionRect(30);
        canvas.drawBitmap(ground.getBitmap(),0,dHeight-ground.getHeight()+50,null);
        canvas.drawText("Score:"+score.getCurrentScore(),(float)(dWidth/2.0),(float)100.00,txtPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            //make the bird jump by 30 units up
            gameState = true;
            bird.makeJump();
        }
        //return true when user inputs touch event

        return true;
    }

    public void gameOver() {

        Log.d("Game Over","PROHRAL JSI");
        //display game over screen with score
        gameState=false;
        Intent myInt = new Intent(getContext(),MainActivity.class);
        myInt.putExtra("gameOverConf","Game Over");
        myInt.putExtra("maxScore",String.valueOf(score.getCurrentScore()));
        getContext().startActivity(myInt);
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    public boolean CollisionDetection(){
            //collides with the ground
        for(PipeManager pm:pipeManagers){
            //collides with the bottom pipe
            if(bird.checkForCollisions(pm.getBottomPipe().getCollisionRect())) return true;
            //collides with the top pipe
            if(bird.checkForCollisions(pm.getTopPipe().getCollisionRect())) return true;
        }

            if(bird.checkForCollisions(ground.getCollisionRect())) return true;

        return false;
    }

    public boolean is_paused(){
        return pause_flg;
    }
    public void pause() throws InterruptedException {
        //ondraw will not be called till the user presses resume button
        pause_flg=true;
    }
    public void resume(){
        //reset ondraw and resume the game
        invalidate();
        pause_flg=false;
    }
    public static boolean getGameActive(){return gameActive;}

}