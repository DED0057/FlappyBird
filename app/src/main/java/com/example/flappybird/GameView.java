package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.os.Handler;

public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final int UPDATE_MILIS=300;
    Bitmap background;
    Display display;
    Point point;
    Rect rect;
    //device width and height
    int dWidth, dHeight;
    //declaration for the bird bitmap
    Bitmap[] birds;
    //temp variable to store bird state (wings down/up)
    int birdState = 0;
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
        //initializing rectangle corresponding to the display dimensions
        rect = new Rect(0,0,dWidth,dHeight);
        //create two states of bird
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_scaleddown);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_wingsup_scaleddown);
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
        double birdXpos,birdYpos;
        //display bird in the middle of the screen
        birdXpos=dWidth/2 - birds[0].getWidth()/2;
        birdYpos = dHeight/2 - birds[1].getHeight()/2;
        canvas.drawBitmap(birds[birdState],(int)birdXpos,(int)birdYpos,null);
    }
}
