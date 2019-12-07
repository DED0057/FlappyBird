package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

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

    public void rotate(float angle){

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void scaleBitmap(int width,int height){
        this.setBitmap( Bitmap.createScaledBitmap(this.getBitmap(),width,height,true));
    }
    public void setCollisionRect(Rect collisionRect) {
        CollisionRect = collisionRect;
    }

    public Rect getCollisionRect() {
        return CollisionRect;
    }
    public int getHeight(){
        return this.getBitmap().getHeight();
    }
    public int getWidth(){
        return this.getBitmap().getWidth();
    }
    public boolean checkForCollisions(Rect r){
        if(this.CollisionRect.intersect(r)) return true;
        return false;
    }
}
