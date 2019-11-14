package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import java.util.Random;

public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final int UPDATE_MILIS=30;
    Bitmap background, tubeTop,tubeBottom;
    Display display;
    Point point;
    Rect rect;
    //device width and height
    int dWidth, dHeight;
    //declaration for the bird bitmap
    Bitmap[] birds;
    //temp variable to store bird state (wings down/up)
    int birdState = 0;
    //physics variables
    int velocity=0,gravity=3;
    //storing the birds position
    int birdXpos,birdYpos;
    boolean gameState = false;
    //setting the gab between the top and bottom tube
    int tubeGap = 400;
    int minTubeOffset,maxTubeOffset;
    int tubeCount = 4;
    int tubeOffset;
    int tubesXpos;
    int tubeTopYpos,tubeBottomYpos;
    Random random;
    public GameView(Context context) {
        super(context);
        handler= new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //call onDraw()
                invalidate();
            }
        };
        //set background of the game screen
        background = BitmapFactory.decodeResource(getResources(),R.mipmap.background);

        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;

        tubeTop = BitmapFactory.decodeResource(getResources(),R.drawable.tube_top_scaled_down);
        tubeTop = Bitmap.createScaledBitmap(tubeTop,dWidth/6,dHeight,true);
        tubeBottom =  BitmapFactory.decodeResource(getResources(),R.drawable.tube_bottom_scaled_down);
        tubeBottom = Bitmap.createScaledBitmap(tubeBottom,dWidth/6,dHeight,true);


        //initializing rectangle corresponding to the display dimensions
        rect = new Rect(0,0,dWidth,dHeight);
        //create two states of bird
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_scaleddown);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_wingsup_scaleddown);
        //set the bird in the middle of the screen
        birdXpos = 1;
        birdYpos = dHeight/2 - birds[1].getHeight()/2;

        tubeOffset = dWidth*3/4;
        //tubes have variable length. set the min and max length here
        minTubeOffset = tubeGap/2;
        maxTubeOffset = dHeight - minTubeOffset - tubeGap;
        //put the first tube in the middle of the screen
        random = new Random();
        tubesXpos = dWidth / 2 - tubeTop.getWidth()/2;
        //set the pipes lengths between min and max gap offset
        tubeTopYpos = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
      //  tubeBottomYpos = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background
       // canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);
        handler.postDelayed(runnable,UPDATE_MILIS);
        //switch between bird images between every display update
        if(birdState==0){
            birdState=1;
        }else{
            birdState=0;
        }
        if(gameState){
            if((birdYpos< dHeight - birds[0].getHeight()) || velocity<0 ){
                velocity += gravity;
                birdYpos += velocity;
                Log.d("birdY:"," "+birdYpos);
            }
            //set the position of the top pipe and draw it. X is the same as bottom pipe. Y is the top of the screen

            canvas.drawBitmap(tubeTop, tubesXpos,tubeTopYpos - tubeTop.getHeight(),null);
            //set the position of the bottom pipe and draw it. Y is
            canvas.drawBitmap(tubeBottom,tubesXpos,tubeTopYpos+tubeGap,null);
            if(birdYpos>dHeight){
                gameOver();
            }
        }
        //display the bird
        canvas.drawBitmap(birds[birdState],birdXpos,birdYpos,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            //make the bird jump by 30 units up
            gameState = true;
            velocity = -30;
            tubeTopYpos = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
            //tubeBottomYpos = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
        }
        //return true when user inputs touch event

        return true;


    }
    public void gameOver(){


    }
}
