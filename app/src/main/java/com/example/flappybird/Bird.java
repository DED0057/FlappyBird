package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/*
*this class represents the player (bird). It extends the assets class ,which means it has it's own bitmap.
* */
public class Bird extends Assets {
    private int birdXpos;
    private int birdYpos;
    private boolean wingsUp;
    private static int velocity;
    private static int gravity;


    //set a variable to control the animation. Bird rotates down when it's falling
    private boolean isFalling;
    //constructor calling the Assets constructor and initializing the birds position.


    Bird(Resources resources, int filepath,int filepath2,int dHeight) {
        super(resources, filepath,filepath2);
       // this.rotate(45);
        this.birdXpos = 1;
        //put the bird in the middle of the Y axis
        this.birdYpos = ( (dHeight/2) - (this.getBitmap().getHeight()/2) );
        this.setCollisionRect();
        velocity = 0;
        gravity = 3;
        isFalling=false;
    }

    public void update() {
        //bird is falling - make him rotate down
        Log.d("birdX:","x:"+getBirdXpos());

        this.setVelocity(this.getVelocity()+this.getGravity());
        this.birdYpos = this.birdYpos + this.getVelocity();
        isFalling=true;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void makeJump() {
        setVelocity(-30);
        isFalling=false;
    }

    public int getBirdXpos(){
        return birdXpos;
    }

    public int getBirdYpos() {
        return birdYpos;
    }

    public void setBirdXpos(int birdXpos) {
        this.birdXpos = birdXpos;
    }

    public void setBirdYpos(int birdYpos) {
        this.birdYpos = birdYpos;
    }

    public void setPosition(int xpos,int ypos){
        this.birdXpos = xpos;
        this.birdYpos = ypos;
    }

    public void setWingsUp(boolean wingsUp) {
        this.wingsUp = wingsUp;
    }

    public boolean isWingsUp() {
        return wingsUp;
    }
    //create custom collision rectangle
    public void setCollisionRect() {
        Rect birdRect=new Rect(this.birdXpos,this.birdYpos,this.birdXpos+this.getBitmap().getWidth(),birdYpos+this.getBitmap().getHeight());
        super.setCollisionRect(birdRect);
    }
    public void setCollisionRect(int offset) {
        Rect birdRect=new Rect(birdXpos+offset,birdYpos,birdXpos+this.getBitmap().getWidth()-50,birdYpos+this.getBitmap().getHeight()-offset);
        super.setCollisionRect(birdRect);
    }

    public int getGravity() {
        return gravity;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
    public boolean birdIsOnScreen(int dHeight){
        if((birdYpos< dHeight - this.getHeight()) || this.getVelocity()<0 ){
            return true;
        }
        return false;
    }
}
