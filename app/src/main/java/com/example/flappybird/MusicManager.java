package com.example.flappybird;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicManager {
    static MediaPlayer mp;
    static boolean isActive;
    private static int gameplayMusic=R.raw.background_music_takeonme;
    private static int gameoverMusic=R.raw.gameover_sound;
    private static int currentMusic=-1;

    public static void start(Context context, int music){
        if(currentMusic!=-1){
            //playing something else. change the music
            pause();

            if (mp != null) {
                if (!mp.isPlaying()) {
                    mp.start();
                    currentMusic=music;
                }
            } else {
                mp = MediaPlayer.create(context, gameplayMusic); //Ur BackGround Music
            }
            if (mp == null) {
                Log.e("Music Manager:", "player was not created successfully");
            }
            else{
                try {
                    mp.setLooping(true);
                    mp.start();
                } catch (Exception e) {
                    Log.e("Music Manager:", e.getMessage(), e);
                }
            }
        }
        else{
            //not playing anything
            if(music>-1){
                mp = MediaPlayer.create(context,music);
            }
            else{
                mp = MediaPlayer.create(context,gameplayMusic);
            }
        }
        mp.start();
    }
    public static void pause(){
        if(mp != null){
            if(mp.isPlaying()){
                mp.pause();
            }
        }
    }
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
