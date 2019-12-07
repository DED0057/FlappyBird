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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

/**
 * Represents every sprite in the app.
 * handles bitmap storing and bitmap management
 */
public abstract class Assets{
   private Bitmap bitmap;
   private Bitmap backup;
    Rect CollisionRect;

    Assets(Resources resources, int filepath){
        this.bitmap = BitmapFactory.decodeResource(resources,filepath);
        this.backup = BitmapFactory.decodeResource(resources,filepath);
    }
    Assets(Resources resources, int filepath, int filepath2){
        this.bitmap = BitmapFactory.decodeResource(resources,filepath);
        this.backup = BitmapFactory.decodeResource(resources,filepath2);
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public Bitmap getBackup() {
        return backup;
    }

    /**
     * Rotate the bitmap by angle.
     * @param angle
     */
    public void rotate(float angle){
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * set a new bitmap
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * make a scaled version of the bitmap
     * @param width
     * @param height
     */
    public void scaleBitmap(int width,int height){
        this.setBitmap( Bitmap.createScaledBitmap(this.getBitmap(),width,height,true));
    }

    /**
     * sets the rectangle around the sprite. This rectangle is then used in collision detection.
     * @param collisionRect
     */
    public void setCollisionRect(Rect collisionRect) {
        CollisionRect = collisionRect;
    }

    /**
     * Returns the rectangle around the sprite
     * @return Rect
     */
    public Rect getCollisionRect() {
        return CollisionRect;
    }

    /**
     * returns bitmap height
     * @return int
     */
    public int getHeight(){
        return this.getBitmap().getHeight();
    }

    /**
     * returns bitmap width
     * @return int
     */
    public int getWidth(){
        return this.getBitmap().getWidth();
    }

    /**
     * checks whether another rectangle came into contact with this Asset's rectangle
     * @param r
     * @return boolean
     */
    public boolean checkForCollisions(Rect r){
        if(this.CollisionRect.intersect(r)) return true;
        return false;
    }
}
