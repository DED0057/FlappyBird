/*
 * *
 *  * Created by Matyas Dedek (DED0057)
 *  * 2019 .
 *  * Last modified 7.12.19 23:17
 *
 *
 */

package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;


/*
*this class represents the player (bird). It extends the assets class ,which means it has it's own bitmap.
* */

/**
 * Represents the player (bird) sprite.
 */
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
        this.birdXpos = 100;
        //put the bird in the middle of the Y axis
        this.birdYpos = ( (dHeight/2) - (this.getBitmap().getHeight()/2) );
        this.setCollisionRect();
        velocity = 0;
        gravity = 3;
        isFalling=false;
    }

    /**
     * Updates the bird's position
     */
    public void update() {
        //bird is falling - make him rotate down
        Log.d("birdY:","y:"+getBirdYpos());

        this.setVelocity(this.getVelocity()+this.getGravity());
        this.birdYpos = this.birdYpos + this.getVelocity();
        isFalling=true;
    }

    /**
     * Check whether the bird is in falling state
     * @return boolean
     */
    public boolean isFalling() {
        return isFalling;
    }

    /**
     * Make the bird jump up by changing it's velocity to a negative number (y-30 is up)
     */
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

    /**
     * useless
     * @param birdXpos
     */
    public void setBirdXpos(int birdXpos) {
        this.birdXpos = birdXpos;
    }
    /**
     * useless
     * @param birdYpos
     */
    public void setBirdYpos(int birdYpos) {
        this.birdYpos = birdYpos;
    }

    /**
     * useless
     * @param xpos
     * @param ypos
     */
    public void setPosition(int xpos,int ypos){
        this.birdXpos = xpos;
        this.birdYpos = ypos;
    }

    /**
     * useless
     * @param wingsUp
     */
    public void setWingsUp(boolean wingsUp) {
        this.wingsUp = wingsUp;
    }

    /**
     * useless
     * @return boolean
     */
    public boolean isWingsUp() {
        return wingsUp;
    }

    /**
     * sets the rectangle around the bird sprite. This rectangle is then used in collision detection.
     */
    public void setCollisionRect() {
        Rect birdRect=new Rect(this.birdXpos,this.birdYpos,this.birdXpos+this.getBitmap().getWidth(),birdYpos+this.getBitmap().getHeight());
        super.setCollisionRect(birdRect);
    }

    /**
     * sets the rectangle around the bird sprite. This rectangle is then used in collision detection.
     * Offset represents how much of the bird can come into contact with another rectangle and not make the player lose.
     * @param offset
     */
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

    /**
     * check whether the bird sprite is in view
     * @param dHeight
     * @return boolean
     */
    public boolean birdIsOnScreen(int dHeight){
        if((birdYpos< dHeight - this.getHeight()) || this.getVelocity()<0 ){
            return true;
        }
        return false;
    }
}
