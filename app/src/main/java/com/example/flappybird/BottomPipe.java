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

/**
 * Represents the bottom pipe sprite. only X axis location is needed, because both pipes share the same one
 */
public class BottomPipe extends Assets {
    private int xPos;

    BottomPipe(Resources resources, int filepath) {
        super(resources, filepath);
    }


    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getxPos() {
        return xPos;
    }

}
