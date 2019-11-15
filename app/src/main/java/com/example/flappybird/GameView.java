package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.widget.EditText;
import android.os.Vibrator;

import java.util.Random;



public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final int UPDATE_MILIS=20;
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
    int birdStateCounter=0;
    boolean birdWingsUp=false;
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
    int[] tubesXpos = new int[tubeCount] ;
    int[] tubeTopYpos = new int[tubeCount];
    Random random;
    int tubeVelocity = 7;
    Vibrator v;

    //creating rectangles for collision detection
    Rect birdRect,tubeTopRect,tubeBotRect, groundRect;

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

        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;

       tubeTop = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
       tubeBottom=BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
        tubeTop = RotateBitmap(tubeTop,180);
        tubeTop = Bitmap.createScaledBitmap(tubeTop,dWidth/4,dHeight,true);
        tubeBottom = Bitmap.createScaledBitmap(tubeBottom,dWidth/4,dHeight,true);
        //initializing rectangle corresponding to the display dimensions
        rect = new Rect(0,0,dWidth,dHeight);
        //create 5 states of bird (seamless animation)
        birds = new Bitmap[5];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_wingsup_scaleddown);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_wingsup3);
        birds[2] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_wingsup2);
        birds[3] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_wingsup1);
        birds[4] = BitmapFactory.decodeResource(getResources(),R.drawable.blue_bird_scaleddown);

        //set the bird in the middle of the screen
        birdXpos = 1;
        birdYpos = dHeight/2 - birds[1].getHeight()/2;
        birdRect=new Rect(birdXpos,birdYpos,birdXpos+birds[0].getWidth(),birdYpos+birds[0].getHeight());
        groundRect = new Rect(0,dHeight,dWidth,dHeight+1);
        tubeBotRect = new Rect(0,0,0,0);
        tubeTopRect = new Rect(0,0,0,0);
        tubeOffset = dWidth;
        //tubes have variable length. set the min and max length here
        minTubeOffset = tubeGap/2;
        maxTubeOffset = dHeight - minTubeOffset - tubeGap;
        //put the first tube in the middle of the screen
        random = new Random();
        for(int i = 0; i < tubeCount; i++){
            //start at the right edge of the screen
            tubesXpos[i] = dWidth + i*tubeOffset;
            tubeTopYpos[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background
       // canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);
        handler.postDelayed(runnable,UPDATE_MILIS);
        //switch between bird images between every display update
       //creating bird flight animation
        birdState=birdStateCounter;
        if(birdStateCounter==4){
            birdWingsUp=true;
        }
        if(birdStateCounter==0){
            birdWingsUp=false;
        }
        if(birdWingsUp){
            birdStateCounter--;
        }else{
            birdStateCounter++;
        }

        if(gameState){
            if((birdYpos< dHeight - birds[0].getHeight() ) || velocity<0 ){
                //let the bird fall with incremental speed
                    velocity += gravity;
                    birdYpos += velocity;
            }
            //set the position of the top pipe and draw it. X is the same as bottom pipe. Y is the top of the screen
            for(int i=0;i<tubeCount;i++) {
                tubesXpos[i] -= tubeVelocity;
                //reset the tube, if it reaches the left end of the screen
                if(tubesXpos[i]<-tubeTop.getWidth()){
                    tubesXpos[i] += tubeCount * tubeOffset;
                    tubeTopYpos[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
                }
                //the only random position is for top tube. The bottom tube depends on the top tube.
                canvas.drawBitmap(tubeTop, tubesXpos[i], tubeTopYpos[i] - tubeTop.getHeight(), null);
                //set the position of the bottom pipe and draw it. Y is
                canvas.drawBitmap(tubeBottom, tubesXpos[i], tubeTopYpos[i] + tubeGap, null);
            }
            if(CollisionDetection()){
                gameOver();
            }
        }
        //display the bird
        canvas.drawBitmap(birds[birdState],birdXpos,birdYpos,null);
        birdRect.set(birdXpos+50,birdYpos,birdXpos+birds[0].getWidth()-50,birdYpos+birds[0].getHeight()-50);
       // canvas.drawRect(birdRect,new Paint());
        Log.d("Bird rect coordinated","Left: "+birdRect.left+" Top:"+birdRect.top+" right: "+birdRect.right+" bottom: "+birdRect.bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            //make the bird jump by 30 units up
            gameState = true;
            velocity = -30;
        }
        //return true when user inputs touch event

        return true;


    }
    public void gameOver(){
        v.vibrate(VibrationEffect.createOneShot(50,1));
        Log.d("Game Over","PROHRAL JSI");

    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public boolean CollisionDetection(){
        int tubeBotHeight= tubeBottom.getHeight();
        int tubeTopHeight = tubeTop.getHeight();
        int tubeWidth = tubeBottom.getWidth();
        //birdXpos can't be
        //if(birdX>=PipeX && birdX<=pipeX+PipeWidth) - bird is inside of the tubes(or in the gap between them)
        //TODO 1: try a new approach using Rect.intersects
        for(int i = 0;i<tubeCount;i++) {
         //   if ((birdXpos >= tubesXpos[i] && birdXpos <= (tubesXpos[i] + tubeWidth))) {
                //bird is inside the tube or in the gap
                //now check the Y axis collision
                //TODO 1: make sure that the collision counts with the whole bird. not only one pixel
               /* if((birdYpos <= tubeTopYpos[i]) || (birdYpos >= tubeTopYpos[i]+tubeGap))
                {
                    return true;
                }
            }*/
               //using rectangles
            tubeTopRect.set(tubesXpos[i],0,tubesXpos[i]+tubeTop.getWidth(),tubeTopYpos[i]);
            //Rect(int left, int top, int right, int bottom)
            tubeBotRect.set(tubesXpos[i],tubeTopYpos[i]+tubeGap,tubesXpos[i]+tubeBottom.getWidth(),dHeight);
            if(Rect.intersects(birdRect,tubeBotRect) || Rect.intersects(birdRect,tubeTopRect) || Rect.intersects(birdRect,groundRect)){
                return true;
            }

        }

        return false;
    }
}
