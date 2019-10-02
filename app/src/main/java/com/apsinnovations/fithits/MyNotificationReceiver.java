package com.apsinnovations.fithits;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyNotificationReceiver extends BroadcastReceiver {
    PlayMusicActivity playMusicActivity;

    public MyNotificationReceiver(PlayMusicActivity playMusicActivity) {
        this.playMusicActivity = playMusicActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action.equals("play")){
            playMusicActivity.play.performClick();
        }
        if(action.equals("prev")){
            playMusicActivity.prev.performClick();
        }
        if(action.equals("next")){
            playMusicActivity.next.performClick();
        }
        if(action.equals("pause")){
            playMusicActivity.play.performClick();
        }

    }

}
