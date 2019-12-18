package com.apsinnovations.fithits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {
public static int oneTimeOnly = 0;
ImageView play,stop,next,prev,cover,favourites,shuffle,repeat;
TextView txtmusic,txtartist,txtinittime,txtendtime;
Song song;
MediaPlayer mediaPlayer;
ArrayList<Song> songs;
NotificationManager notificationManager;
Notification notification;
NotificationCompat.Builder builder;
MyNotificationReceiver myNotificationReceiver;
Animation aniRotate;
boolean playing,FavUnfav,shuffling,repeating;

    NotificationCompat.Action action;
private double startTime = 0;
private double finalTime = 0;
private SeekBar seekbar;
private Handler myHandler = new Handler();

private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txtinittime.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 0);
        }
    };

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_play_music_2);
            aniRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            initializecontrols();
             initViews();
            getSupportActionBar().hide();
            showNotification();

    }
    void initializecontrols(){
    shuffling=false;
    repeating=false;
    FavUnfav=false;
    }

    void initViews(){
        play=findViewById(R.id.play);
        //pause=findViewById(R.id.pause);
        stop=findViewById(R.id.stop);
        next=findViewById(R.id.next);
        prev=findViewById(R.id.previous);
        favourites=findViewById(R.id.favourites);
        repeat=findViewById(R.id.repeat);
        shuffle=findViewById(R.id.shuffle);
        cover=findViewById(R.id.cover);
        txtinittime=findViewById(R.id.starttime);
        txtendtime=findViewById(R.id.endtime);
//        play.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
//        pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
//        stop.setBackgroundResource(R.drawable.ic_stop_black_24dp);
//        next.setBackgroundResource(R.drawable.ic_skip_next_black_24dp);
//        prev.setBackgroundResource(R.drawable.ic_skip_previous_black_24dp);
//        play.setBackgroundResource(R.drawable.icon_play);
        //pause.setBackgroundResource(R.drawable.icon_pause);
//        stop.setBackgroundResource(R.drawable.icon_stop);
//        next.setBackgroundResource(R.drawable.icon_next);
//        prev.setBackgroundResource(R.drawable.icon_prev);

//        pause.setVisibility(View.INVISIBLE);
//        stop.setVisibility(View.INVISIBLE);
        stop.setEnabled(false);

        txtmusic=findViewById(R.id.musicname);
        txtartist=findViewById(R.id.artist_name);

        mediaPlayer = new MediaPlayer();
        seekbar = findViewById(R.id.seekBar);
//        seekbar.setClickable(false);
      seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
              if(b==true) {
                  mediaPlayer.seekTo(i);
                  myHandler.postDelayed(UpdateSongTime, 0);
              }
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }
      });


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
       }else{
           cover.setImageResource(R.mipmap.ic_launcher_round);
       }

       txtmusic.setText(song.title);
       txtartist.setText(song.artist);

       try {
            mediaPlayer.setDataSource(""+song.Location);

        } catch (IOException e) {
            e.printStackTrace();
        }
       try{
           mediaPlayer.prepare();
       }catch (Exception e){
           e.printStackTrace();
       }

        playing=false;
        play.setOnClickListener(this);
        //pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        favourites.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        repeat.setOnClickListener(this);

        myNotificationReceiver=new MyNotificationReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        filter.addAction("prev");
        filter.addAction("next");
        filter.addAction("pause");
//        filter.addAction("activity");
        registerReceiver(myNotificationReceiver,filter);
    }
    @Override
    public void onClick(View view) {
    int id=view.getId();
        if(id==R.id.play){
            if(playing==false) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                mediaPlayer.setLooping(true);
                startTime = mediaPlayer.getCurrentPosition();
                finalTime = mediaPlayer.getDuration();
                cover.startAnimation(aniRotate);
                // aniRotate.setDuration((long)finalTime);
                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    myHandler.postDelayed(UpdateSongTime, 0);
                    oneTimeOnly = 1;
                }
                txtinittime.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );
                txtendtime.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );
//                pause.setVisibility(View.VISIBLE);
//                stop.setVisibility(View.VISIBLE);
                stop.setEnabled(true);
//                play.setVisibility(View.INVISIBLE);
                play.setImageResource(R.drawable.icon_pause);

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 0);
                playing = true;
                showNotification();
            }else{
                mediaPlayer.pause();
                startTime = mediaPlayer.getCurrentPosition();
//                pause.setVisibility(View.INVISIBLE);
                play.setImageResource(R.drawable.icon_play);
//                stop.setVisibility(View.VISIBLE);
                stop.setEnabled(true);
//                play.setVisibility(View.VISIBLE);
                cover.clearAnimation();
                playing=false;
                showNotification();
            }

        }


//        if(id==R.id.pause){
//            mediaPlayer.pause();
//            startTime = mediaPlayer.getCurrentPosition();
//            pause.setVisibility(View.INVISIBLE);
//            stop.setVisibility(View.VISIBLE);
//            play.setVisibility(View.VISIBLE);
//            cover.clearAnimation();
//            playing=false;
//            showNotification();
//        }


        if(id==R.id.stop){
            mediaPlayer.stop();
            mediaPlayer.reset();
            startTime=0;
            finalTime=0;
            oneTimeOnly=0;
            playing=false;
            play.setImageResource(R.drawable.icon_play);
            try {
                mediaPlayer.setDataSource(""+song.Location);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            pause.setVisibility(View.INVISIBLE);
//            stop.setVisibility(View.INVISIBLE);
            stop.setEnabled(false);
//            play.setVisibility(View.VISIBLE);
            cover.clearAnimation();
        }



        if(id==R.id.next) {
            int pos=0;
            for(int i=0;i<songs.size()-1;i++) {
            Song s1 = songs.get(i);
            if (s1.id.equals(song.id)) {
                    pos = i + 1;
                    if(pos==songs.size()){
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
                playing=false;
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
                    if(pos==-1){
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
                playing=false;
                play.performClick();
            }
           showNotification();
        }


        if(id==R.id.favourites){
            if(FavUnfav==false){
                Toast.makeText(this,"Removed from Favourites!",Toast.LENGTH_SHORT).show();
                favourites.setImageResource(R.drawable.ic_favorites);
                FavUnfav=true;
            }
            else{
                Toast.makeText(this,"Added to Favourites :)",Toast.LENGTH_SHORT).show();
                favourites.setImageResource(R.drawable.ic_favorites_colour);
                FavUnfav=false;

            }
        }
        if(id==R.id.shuffle){
            if(shuffling==false){
                Toast.makeText(this,"Shuffle OFF",Toast.LENGTH_SHORT).show();
                shuffle.setImageResource(R.drawable.ic_shuffle_black);
                shuffling=true;
            }
            else{
                Toast.makeText(this,"Shuffle ON",Toast.LENGTH_SHORT).show();
                shuffle.setImageResource(R.drawable.ic_shuffle);
                shuffling=false;
            }

        }
        if(id==R.id.repeat){
            if(repeating==false){
                Toast.makeText(this,"Loop All",Toast.LENGTH_SHORT).show();
                repeat.setImageResource(R.drawable.ic_exchange);
                repeating=true;
            }
            else{
                Toast.makeText(this,"Loop Once",Toast.LENGTH_SHORT).show();
                repeat.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                repeating=false;
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myNotificationReceiver);
        mediaPlayer.stop();
        mediaPlayer.reset();
        startTime=0;
        finalTime=0;
        oneTimeOnly=0;
cancelNotification();
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

        Intent intent =new Intent(this,PlayMusicActivity.class);

//        intent.setAction("activity");

//        intent.setAction("activity");
        //Intent pauseIntent=new Intent("pause");
        Intent playIntent=new Intent("play");
        Intent nextIntent=new Intent("next");
        Intent prevIntent=new Intent("prev");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 121, intent, 0);
        PendingIntent pendingPlay = PendingIntent.getBroadcast(this, 131, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingPause = PendingIntent.getBroadcast(this, 131, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingNext = PendingIntent.getBroadcast(this, 131, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingPrev = PendingIntent.getBroadcast(this, 131, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        if(playing==true) {
            action = new NotificationCompat.Action(R.drawable.ic_pause_black_24dp,null,pendingPlay);
            playing = true;
        }else {
            action = new NotificationCompat.Action(R.drawable.ic_play_arrow_black_24dp, null, pendingPlay);
            playing=false;

        }
        builder = new NotificationCompat.Builder(this, "myId");
        builder.setContentTitle(""+song.title);
        builder.setContentText(""+song.artist);
        builder.setLargeIcon(largeimage);
        builder.setSmallIcon(icon);
        builder.setWhen(when);
        builder.addAction(R.drawable.ic_skip_previous_black_24dp,null,pendingPrev)
        .addAction(action)
        .addAction(R.drawable.ic_skip_next_black_24dp,null,pendingNext).setOngoing(true);
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        notification = builder.build();

        notificationManager.notify(122, notification);

    }
    void cancelNotification(){
    notificationManager.cancelAll();
    }

}
