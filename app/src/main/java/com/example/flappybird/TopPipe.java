package com.example.flappybird;

import android.content.res.Resources;

public class TopPipe extends Assets {
    private int yPos;

    TopPipe(Resources resources, int filepath) {
        super(resources, filepath);
        this.rotate(180);
    }


    public void update() {

    }


    public void reset() {

    }



    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getyPos() {
        return yPos;
    }


}
