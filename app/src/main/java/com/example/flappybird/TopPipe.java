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
 * Represents the TopPipe sprite
 * Only Y axis location is needed, because bottom pipe's Y loc. calc. is based on top pipes'.
 */
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
