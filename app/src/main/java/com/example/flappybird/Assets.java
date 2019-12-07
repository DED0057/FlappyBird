package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

public abstract class Assets{
   private  static Bitmap bitmap;
    Rect CollisionRect;

    Assets(Resources resources, int filepath){
        this.bitmap = BitmapFactory.decodeResource(resources,filepath);
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public static Bitmap rotate(float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void setBitmap(Bitmap bitmap) {
        Assets.bitmap = bitmap;
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
    public boolean checkForCollisions(Assets opposition){
        //check for collisions with opposition rectangle
        return false;
    }
}
