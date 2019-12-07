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

    Handler handler;
    Runnable runnable;
    Paint txtPaint;
    Typeface plainFont;
    Typeface boldFont;
    final int UPDATE_MILIS=20;
    Bitmap tubeTop,tubeBottom;

    Display display;
    Point point;
    Rect rect;
    //device width and height
    int dWidth, dHeight;
    //game objects(Assets)
    Bird bird;
    Background background;
    Ground ground;
    Score score;
    boolean gameState = false;
    //setting the gab between the top and bottom tube
    //deprecated - all is set in pipemanager
    int tubeGap = 400;
    int minTubeOffset,maxTubeOffset;
    int tubeCount = 2;
    int tubeVelocity = 7;
    int tubeOffset;
    int[] tubesXpos = new int[tubeCount];
    int[] tubeTopYpos = new int[tubeCount];
    //using objects
    //pipemanager handler pipe updates and other stuff
    Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
    PipeManager pipeManager;
    Random random;
    //creating rectangles for collision detection
    //deprecated
    Rect tubeTopRect,tubeBotRect;

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
        score = new Score();
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        //set background of the game screen
        background = new Background(getResources(),R.mipmap.background);
        background.setCollisionRect(new Rect(0,0,dWidth,dHeight));


        //deprecated
        tubeTop = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
        tubeBottom=BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
        tubeTop = RotateBitmap(tubeTop,180);
        tubeTop = Bitmap.createScaledBitmap(tubeTop,dWidth/4,dHeight,true);
        tubeBottom = Bitmap.createScaledBitmap(tubeBottom,dWidth/4,dHeight,true);

        ground = new Ground(getResources(),R.drawable.ground);
        ground.setBitmap(Bitmap.createScaledBitmap(ground.getBitmap(),dWidth,400,true));
        ground.setCollisionRect(new Rect(0,dHeight-300,dWidth,dHeight));


        //set the birds bitmap and put him at the leftmost side and in the middle of the Y axis.
        bird = new Bird(getResources(),R.drawable.bird,dHeight);

        //deprecated
        tubeBotRect = new Rect(0,0,0,0);
        tubeTopRect = new Rect(0,0,0,0);

        //deprecated
        tubeOffset = dWidth;
        //use this

        //tubes have variable length. set the min and max length here
        //deprecated
        minTubeOffset = tubeGap/2;
        maxTubeOffset = dHeight - minTubeOffset - tubeGap;

        //pipemanager
        pipeManager = new PipeManager(getResources(),tubeGap,tubeGap/2,dHeight - minTubeOffset - tubeGap,tubeVelocity,dWidth,dHeight);

        //deprecated
        random = new Random();
        for(int i = 0; i < tubeCount; i++){
            //start at the right edge of the screen
            tubesXpos[i] = dWidth + i*tubeOffset;
            tubeTopYpos[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
        }
        //pipemanager
        pipeManager.reset(1);

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
        gameActive = true;
        Paint bgpaint = new Paint();
        bgpaint.setColor(Color.BLACK);
        canvas.drawBitmap(background.getBitmap(),null,background.getCollisionRect(),null);

        handler.postDelayed(runnable,UPDATE_MILIS);

        if(gameState){

            if(bird.birdIsOnScreen(dHeight)){
                bird.update();
            }

            //set the position of the top pipe and draw it. X is the same as bottom pipe. Y is the top of the screen
            //deprecated
          //  for(int i=0;i<tubeCount;i++) {
             //   tubesXpos[i] -= tubeVelocity;
                //reset the tube, if it reaches the left end of the screen
               // if(tubesXpos[i]<-tubeTop.getWidth()){
                //    tubesXpos[i] += tubeCount * tubeOffset;
               //     tubeTopYpos[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
                 //   score.addScore();
                //}

                //deprecated
                //the only random position is for top tube. The bottom tube depends on the top tube.
                //canvas.drawBitmap(tubeTop, tubesXpos[i], tubeTopYpos[i] - tubeTop.getHeight(), null);
                //use this
                //pipemanager

                //deprecated
                //set the position of the bottom pipe and draw it. Y is

                //canvas.drawBitmap(tubeBottom, tubesXpos[i], tubeTopYpos[i] + tubeGap, null);

          //  }
            //loop through pipemanagers, update and display
            //pipemanager
            pipeManager.update();
            canvas.drawBitmap(pipeManager.getTopPipe().getBitmap(),pipeManager.getXpos(),pipeManager.getYpos()-pipeManager.getTopPipe().getHeight(),null);
            canvas.drawBitmap(pipeManager.getBottomPipe().getBitmap(),pipeManager.getXpos(),pipeManager.getYpos()+pipeManager.getPipeGap(),null);

            if(CollisionDetection()){
                    gameOver();
            }
        }
        //display the bird
        canvas.drawBitmap(bird.getBitmap(),bird.getBirdXpos(),bird.getBirdYpos(),null);

        // 50 is the offset, so the game is a bit easier. it means that 50pixels from each side wont count in collision calculation
        bird.setCollisionRect(30);
        //canvas.drawBitmap(ground,0,dHeight-350,null);
        canvas.drawBitmap(ground.getBitmap(),0,dHeight-ground.getHeight()+50,null);
        canvas.drawText("Score:"+score.getCurrentScore(),(float)(dWidth/2.0),(float)100.00,txtPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            //make the bird jump by 30 units up
            gameState = true;
            bird.setVelocity(-30);
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
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public boolean CollisionDetection(){
        //deprecated. call Assets check for collisions
       // for(int i = 0;i<tubeCount;i++) {
            //collides with the ground
            if(bird.checkForCollisions(ground.getCollisionRect())) return true;
            //collides with the bottom pipe
            if(bird.checkForCollisions(pipeManager.getBottomPipe().getCollisionRect())) return true;
            //collides with the top pipe
            if(bird.checkForCollisions(pipeManager.getTopPipe().getCollisionRect())) return true;

            //using rectangles
           /* tubeTopRect.set(tubesXpos[i],0,tubesXpos[i]+tubeTop.getWidth(),tubeTopYpos[i]);

            tubeBotRect.set(tubesXpos[i],tubeTopYpos[i]+tubeGap,tubesXpos[i]+tubeBottom.getWidth(),dHeight);
            if(Rect.intersects(bird.getCollisionRect(),tubeBotRect) || Rect.intersects(bird.getCollisionRect(),tubeTopRect) || Rect.intersects(bird.getCollisionRect(),ground.getCollisionRect())){
                return true;
            }*/


        //}

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