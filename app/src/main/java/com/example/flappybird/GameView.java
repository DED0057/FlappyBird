package com.example.flappybird;

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

import java.util.Random;
import java.util.Timer;


public class GameView extends View {

    private static boolean pause_flg = false;
    public static boolean gameActive = false;
    private Timer timer;

    Handler handler;
    Runnable runnable;
    Paint txtPaint;
    Typeface plainFont;
    Typeface boldFont;
    final int UPDATE_MILIS=20;
    Bitmap background, tubeTop,tubeBottom,ground;
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
    int maxScore=0;

    //creating rectangles for collision detection
    Rect birdRect,tubeTopRect,tubeBotRect, groundRect;

    public GameView(Context context) {
        super(context);
        timer = new Timer();
        handler= new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //call onDraw()
                if(!is_paused())
                invalidate();
            }
        };

        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        //set background of the game screen

        background = BitmapFactory.decodeResource(getResources(),R.mipmap.background);

        tubeTop = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
        tubeBottom=BitmapFactory.decodeResource(getResources(),R.drawable.pipe_bottomnew);
        tubeTop = RotateBitmap(tubeTop,180);
        tubeTop = Bitmap.createScaledBitmap(tubeTop,dWidth/4,dHeight,true);
        tubeBottom = Bitmap.createScaledBitmap(tubeBottom,dWidth/4,dHeight,true);
        ground = BitmapFactory.decodeResource(getResources(),R.drawable.ground);
        ground = Bitmap.createScaledBitmap(ground,dWidth,400,true);
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
        groundRect = new Rect(0,dHeight-300,dWidth,dHeight);
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
        canvas.drawRect(0,0,dWidth,dHeight,bgpaint);
        canvas.drawBitmap(background,null,rect,null);
        canvas.drawText("Score:"+maxScore+is_paused(),(float)(dWidth/2.0),(float)100.00,txtPaint);
        handler.postDelayed(runnable,UPDATE_MILIS);
        //do{handler.postDelayed(runnable,UPDATE_MILIS);}while(is_paused()!=true);

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
                    maxScore++;
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

        canvas.drawBitmap(ground,0,dHeight-350,null);

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
    public void gameOver() {

        Log.d("Game Over","PROHRAL JSI");
        //display game over screen with score
        gameState=false;
        Intent myInt = new Intent(getContext(),MainActivity.class);
        myInt.putExtra("gameOverConf","Game Over");
        myInt.putExtra("maxScore",String.valueOf(maxScore));
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

        for(int i = 0;i<tubeCount;i++) {

            //using rectangles
            tubeTopRect.set(tubesXpos[i],0,tubesXpos[i]+tubeTop.getWidth(),tubeTopYpos[i]);

            tubeBotRect.set(tubesXpos[i],tubeTopYpos[i]+tubeGap,tubesXpos[i]+tubeBottom.getWidth(),dHeight);
            if(Rect.intersects(birdRect,tubeBotRect) || Rect.intersects(birdRect,tubeTopRect) || Rect.intersects(birdRect,groundRect)){
                return true;
            }

        }

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