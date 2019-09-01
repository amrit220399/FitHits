package com.apsinnovations.fithits;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {
public static int oneTimeOnly = 0;
ImageView play,pause,stop,next,prev,cover;
TextView txtmusic,txtartist,txtinittime,txtendtime;
Song song;
MediaPlayer mediaPlayer;
ArrayList<Song> songs;
NotificationManager notificationManager;
Notification notification;
NotificationCompat.Builder builder;
ActionReceiver actionReceiver;
Animation aniRotate;
private double startTime = 0;
private double finalTime = 0;
private SeekBar seekbar;
private Handler myHandler = new Handler();

private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txtinittime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        initViews();
        getSupportActionBar().hide();
        showNotification();
    }

    void initViews(){
        play=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        stop=findViewById(R.id.stop);
        next=findViewById(R.id.next);
        prev=findViewById(R.id.previous);
        cover=findViewById(R.id.cover);
        txtinittime=findViewById(R.id.starttime);
        txtendtime=findViewById(R.id.endtime);
        play.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        stop.setBackgroundResource(R.drawable.ic_stop_black_24dp);
        next.setBackgroundResource(R.drawable.ic_skip_next_black_24dp);
        prev.setBackgroundResource(R.drawable.ic_skip_previous_black_24dp);

        pause.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);

        txtmusic=findViewById(R.id.musicname);
        txtartist=findViewById(R.id.artist_name);

        mediaPlayer = new MediaPlayer();
        seekbar = findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        Intent rcv=getIntent();
        song=new Song();
        song.title=rcv.getStringExtra("keySong");
        song.Location=rcv.getStringExtra("keyLocation");
        song.albumpath=rcv.getStringExtra("keyAlbum");
        song.artist=rcv.getStringExtra("keyArtist");
        song.id=rcv.getStringExtra("keyId");
        songs=rcv.getParcelableArrayListExtra("Songs");
        for(int j=0;j<songs.size()-1;j++){
            Song s2=songs.get(j);
            Log.i("Array",""+j+s2);
        }
       if(song.albumpath!=null) {
           Drawable img = Drawable.createFromPath(song.albumpath);
           cover.setImageDrawable(img);
       }

       txtmusic.setText(song.title);
       txtartist.setText(song.artist);

       try {
            mediaPlayer.setDataSource(""+song.Location);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);

        actionReceiver=new ActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        LocalBroadcastManager.getInstance(this).registerReceiver(actionReceiver, filter);
    }

    @Override
    public void onClick(View view) {
    int id=view.getId();
        if(id==R.id.play){
            if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();}
            mediaPlayer.setLooping(true);
            startTime = mediaPlayer.getCurrentPosition();
            finalTime = mediaPlayer.getDuration();
            cover.startAnimation(aniRotate);
           // aniRotate.setDuration((long)finalTime);
            if (oneTimeOnly == 0) {
                seekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }
            txtinittime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );
            txtendtime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
            );
            pause.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);

            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(UpdateSongTime,0);
        }


        if(id==R.id.pause){
            mediaPlayer.pause();
            startTime = mediaPlayer.getCurrentPosition();
            pause.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);
            cover.clearAnimation();
        }


        if(id==R.id.stop){
            mediaPlayer.stop();
            mediaPlayer.reset();
            startTime=0;
            finalTime=0;
            oneTimeOnly=0;
            try {
                mediaPlayer.setDataSource(""+song.Location);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pause.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            cover.clearAnimation();
        }



        if(id==R.id.next) {
            int pos=0;
            for(int i=0;i<songs.size()-1;i++) {
            Song s1 = songs.get(i);
            if (s1.id.equals(song.id)) {
                    pos = i + 1;
                    if(pos==songs.size()-1){
                        pos=0;
                        i=0;
                    }
                    break;
                }
            }
        Log.i("pos",""+pos);
            song=songs.get(pos);

            txtmusic.setText(song.title);
            txtartist.setText(song.artist);
                if(song.albumpath!=null) {
                    cover.setVisibility(View.VISIBLE);
                    Drawable img = Drawable.createFromPath(song.albumpath);
                    cover.setImageDrawable(img);
                }else{
                    cover.setVisibility(View.INVISIBLE);
                }
            mediaPlayer.stop();
            mediaPlayer.reset();
            startTime=0;
            finalTime=0;
            oneTimeOnly=0;
            try {
                mediaPlayer.setDataSource(""+song.Location);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!mediaPlayer.isPlaying()){
                play.performClick();
            }
            showNotification();
        }


        if(id==R.id.previous){
            int pos=0;
            for(int i=songs.size()-1;i>=0;i--) {
                Song s1 = songs.get(i);
                if (s1.id.equals(song.id)) {
                    pos = i - 1;
                    if(pos==0){
                        pos=songs.size()-1;
                        i=songs.size()-1;
                    }
                    break;
                }

            }
            Log.i("pos",""+pos);
            song=songs.get(pos);

            txtmusic.setText(song.title);
            txtartist.setText(song.artist);
            if(song.albumpath!=null) {
                cover.setVisibility(View.VISIBLE);
                Drawable img = Drawable.createFromPath(song.albumpath);
                cover.setImageDrawable(img);
            }else{
                cover.setVisibility(View.INVISIBLE);
            }
            mediaPlayer.stop();
            mediaPlayer.reset();
            startTime=0;
            finalTime=0;
            oneTimeOnly=0;
            try {
                mediaPlayer.setDataSource(""+song.Location);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!mediaPlayer.isPlaying()){
                play.performClick();
            }
           showNotification();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        startTime=0;
        finalTime=0;
        oneTimeOnly=0;
    }

    void showNotification(){


        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        int icon=R.mipmap.ic_launcher_round;
        long when=System.currentTimeMillis();
      Bitmap largeimage= BitmapFactory.decodeFile(song.albumpath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel("myid", "myChannel2", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(PlayMusicActivity.this, PlayMusicActivity.class);
        Intent playIntent=new Intent(PlayMusicActivity.this,ActionReceiver.class);
        playIntent.putExtra("action","play");

        PendingIntent pendingIntent = PendingIntent.getActivity(PlayMusicActivity.this, 121, intent, 0);
        PendingIntent pendingPlay = PendingIntent.getBroadcast(PlayMusicActivity.this, 131, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(playIntent);

        builder = new NotificationCompat.Builder(this, "myId");
        builder.setContentTitle(""+song.title);
        builder.setContentText(""+song.artist);
        builder.setLargeIcon(largeimage);
        builder.setSmallIcon(icon);
        builder.setWhen(when);
        builder.addAction(R.drawable.ic_skip_previous_black_24dp,null,null)
        .addAction(R.drawable.ic_play_arrow_black_24dp,null,pendingPlay)
        .addAction(R.drawable.ic_skip_next_black_24dp,null,null).setOngoing(true);
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2));
        builder.setContentIntent(pendingIntent);

        notification = builder.build();

        notificationManager.notify(122, notification);

    }
    class ActionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getStringExtra("action");
            Log.i("MyActionReceiver","Receiver Working");
            if(action.equals("play")){

            }
        }
    }
}
