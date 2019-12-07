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
   // Bitmap[] birds;
    Bird bird;
    //temp variable to store bird state (wings down/up)
    //deprecated
    //int birdState = 0;
    //deprecated
    //int birdStateCounter=0;
    //deprecated
    //boolean birdWingsUp=false;

    //physics variables
   // int velocity=0,gravity=3;
    //storing the birds position
    //deprecated
   // int birdXpos,birdYpos;
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
    //deprecated
    Rect tubeTopRect,tubeBotRect, groundRect;
    //Rect birdRect;

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
        //set the birds bitmap and put him at the leftmost side and in the middle of the Y axis.
        bird = new Bird(getResources(),R.drawable.bird,dHeight);

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

        if(gameState){

            if(bird.birdIsOnScreen(dHeight)){
                bird.update();
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
        canvas.drawBitmap(bird.getBitmap(),bird.getBirdXpos(),bird.getBirdYpos(),null);

        // 50 is the offset, so the game is a bit easier. it means that 50pixels from each side wont count in collision calculation
        bird.setCollisionRect(10);
        canvas.drawBitmap(ground,0,dHeight-350,null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            //make the bird jump by 30 units up
            gameState = true;
            //deprecated
            //velocity = -30;
            //use this instead
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
        //deprecated. call Assets check for collisions
        for(int i = 0;i<tubeCount;i++) {

            //using rectangles
            tubeTopRect.set(tubesXpos[i],0,tubesXpos[i]+tubeTop.getWidth(),tubeTopYpos[i]);

            tubeBotRect.set(tubesXpos[i],tubeTopYpos[i]+tubeGap,tubesXpos[i]+tubeBottom.getWidth(),dHeight);
            if(Rect.intersects(bird.getCollisionRect(),tubeBotRect) || Rect.intersects(bird.getCollisionRect(),tubeTopRect) || Rect.intersects(bird.getCollisionRect(),groundRect)){
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