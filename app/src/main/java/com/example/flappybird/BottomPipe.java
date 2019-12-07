package com.example.flappybird;

import android.content.res.Resources;

public class BottomPipe extends Assets {
    private int xPos;

    BottomPipe(Resources resources, int filepath) {
        super(resources, filepath);
        //this.rotate(180);

    }
    public void update(TopPipe topPipe) {

    }
    public void reset() {

    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getxPos() {
        return xPos;
    }

}
