/*
 * *
 *  * Created by Matyas Dedek (DED0057)
 *  * 2019 .
 *  * Last modified 7.12.19 23:23
 *
 *
 */

package com.example.flappybird;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Handles music
 */
public class MusicManager {
    static MediaPlayer mp;
    static boolean isActive;
    private static int gameplayMusic=R.raw.background_music_takeonme;
    private static int gameoverMusic=R.raw.gameover_sound;
    private static int currentMusic=-1;
    private static int currentPosition = -1;

    /**
     * make the music start playing.
     * Resume is true if the music has been paused and you want to resume from the previous time.
     * @param context
     * @param music
     * @param resume
     */
    public static void start(Context context, int music,boolean resume){
        if(resume) {
            //play from the previous point
            mp.seekTo(currentPosition);
            mp.start();
        }else {
            if (currentMusic != -1) {
                //playing something else. change the music
                pause();

                if (mp != null) {
                    //music player has been initialized
                    if (!mp.isPlaying()) {
                        mp.start();
                        currentMusic = music;
                    }
                } else {
                    //music player hasn't been initialized
                    mp = MediaPlayer.create(context, gameplayMusic); //BackGround Music
                    currentMusic = gameplayMusic;
                }
                if (mp == null) {
                    Log.e("Music Manager:", "player was not created successfully");
                } else {
                    try {
                        mp.setLooping(true);
                        mp.start();
                    } catch (Exception e) {
                        Log.e("Music Manager:", e.getMessage(), e);
                    }
                }
            } else {
                //not playing anything
                if (music > -1) {
                    mp = MediaPlayer.create(context, music);
                    currentMusic = music;
                } else {
                    mp = MediaPlayer.create(context, gameplayMusic);
                    currentMusic = gameplayMusic;
                }
            }

            mp.start();
        }

    }

    /**
     * pauses the current track
     */
    public static void pause(){
        if(mp != null){
            if(mp.isPlaying()){
                mp.pause();
                currentPosition=mp.getCurrentPosition();
            }
        }
    }

    /**
     * stop and release the music player
     */
    public static void release(){
        try{
            if(mp!=null){
                if(mp.isPlaying()){
                    mp.stop();
                }
                mp.release();
            }
        }
        catch(Exception e){
            Log.e("Music Manager: ", e.getMessage(), e);
        }
        currentMusic=-1;
    }

    public int getGameplayMusic(){return gameplayMusic;}
    public int getGameoverMusic(){return gameoverMusic;}
    public int getCurrentMusic(){return currentMusic;}
}
