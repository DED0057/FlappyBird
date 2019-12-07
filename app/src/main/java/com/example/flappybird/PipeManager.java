package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.nio.channels.Pipe;
import java.util.Random;

public class PipeManager {
    private static int pipeGap;
    private static int minTubeOffset,maxTubeOffset;
    private static int pipeOffset;
    private static int pipeVelocity;
    private int dWidth,dHeight;
    Random random;
    BottomPipe bottomPipe;
    TopPipe topPipe;
    Score score;

    PipeManager(Resources resources,int pipeGap,int minTubeOffset,int maxTubeOffset, int pipeVelocity,int dWidth, int dHeight){
        //pipe settings
        this.setPipeGap(pipeGap);
        this.setMinTubeOffset(minTubeOffset);
        this.setMaxTubeOffset(maxTubeOffset);
        this.setPipeVelocity(pipeVelocity);
        this.setPipeOffset(dWidth);
        this.dWidth = dWidth;
        this.dHeight = dHeight;
        //inicialize the objects
        this.score = new Score();
        this.bottomPipe = new BottomPipe(resources,R.drawable.pipe_bottomnew);
        this.topPipe = new TopPipe(resources,R.drawable.pipe_bottomnew);
        this.topPipe.scaleBitmap(dWidth/4,dHeight);
        this.bottomPipe.scaleBitmap(dWidth/4,dHeight);
        this.topPipe.setCollisionRect(new Rect(0,0,0,0));
        this.bottomPipe.setCollisionRect(new Rect(0,0,0,0));
        this.random = new Random();

    }

    public void update() {
    //update top and bottom pipes
        this.setXpos(this.getXpos()-pipeVelocity);
        if(isOffScreen()){
            this.setXpos((2*getPipeOffset()) - getTopPipe().getWidth());
            this.setYpos((minTubeOffset)+random.nextInt(maxTubeOffset-minTubeOffset+1));
        }
        updateRectangles();
    }

    /**
     * update the rectangles around pipes which are used when calculation collisions
     */
    public void updateRectangles(){
        this.bottomPipe.setCollisionRect( new Rect( getXpos(),getYpos()+getPipeGap(),getXpos()+bottomPipe.getWidth(),dHeight ) );
        this.topPipe.setCollisionRect( new Rect( getXpos(),0,getXpos()+topPipe.getWidth(),getYpos() ) );
    }
    public boolean isOffScreen(){

        if(this.getXpos() < -topPipe.getWidth()){
            score.addScore();
            return true;
        }
        return false;
    }

    public void reset(int offsetMultiplier){
        this.setXpos((getPipeOffset()*offsetMultiplier));
        this.setYpos(minTubeOffset+random.nextInt(maxTubeOffset-minTubeOffset+1));
        this.topPipe.setCollisionRect(new Rect(0,0,0,0));
        this.bottomPipe.setCollisionRect(new Rect(0,0,0,0));
    }

    public BottomPipe getBottomPipe() {
        return bottomPipe;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public TopPipe getTopPipe() {
        return topPipe;
    }
    public int getXpos(){
        return this.bottomPipe.getxPos();
    }
    public void setXpos(int xPos){
        this.bottomPipe.setxPos(xPos);
    }
    public int getYpos(){
        return this.topPipe.getyPos();
    }
    public void setYpos(int yPos){
        this.topPipe.setyPos(yPos);
    }
    /**
     * sets the starting parameters to the pipes
     */

    public static int getPipeOffset() {
        return pipeOffset;
    }

    public void setPipeOffset(int pipeOffset) {
        this.pipeOffset = pipeOffset;
    }

    public static int getPipeVelocity() {
        return pipeVelocity;
    }

    /**
     *
     * @return static int
     */
    public static int getPipeGap() {
        return pipeGap;
    }

    /**
     *
     * @param minTubeOffset
     */
    public static void setMinTubeOffset(int minTubeOffset) {
        PipeManager.minTubeOffset = minTubeOffset;
    }

    /**
     *
     * @param maxTubeOffset
     */
    public static void setMaxTubeOffset(int maxTubeOffset) {
        PipeManager.maxTubeOffset = maxTubeOffset;
    }

    /**
     *
     * @return static int
     */
    public static int getMaxTubeOffset() {
        return maxTubeOffset;
    }

    /**
     *
     * @return static int
     */
    public static int getMinTubeOffset() {
        return minTubeOffset;
    }

    /**
     *
     * @param pipeVelocity
     */
    public void setPipeVelocity(int pipeVelocity) {
        this.pipeVelocity = pipeVelocity;
    }

    /**
     *
     * @param pipeGap
     */
    public void setPipeGap(int pipeGap) {
        this.pipeGap = pipeGap;
    }
}
