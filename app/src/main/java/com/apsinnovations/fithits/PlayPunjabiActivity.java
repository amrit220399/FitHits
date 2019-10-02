package com.apsinnovations.fithits;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlayPunjabiActivity extends AppCompatActivity implements View.OnClickListener {
    public static int oneTimeOnly = 0;
ImageView play,cover;
TextView name;
boolean playing;
int onetimemediaset=0;
ArrayList<String> arraySongs;
ArrayList<String> arrayPath;
    FirebaseStorage storage;
    StorageReference storageRef;
    ProgressDialog progressDialog;
    TextView txtinittime,txtendtime,txtBuffering;
    MediaPlayer mediaPlayer;
    String current;
    SelectMusicLanguage selectMusicLanguage;
    private double startTime = 0;
    private double finalTime = 0;
    private SeekBar seekbar;
    private Handler myHandler = new Handler();
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if(mediaPlayer.isPlaying())
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
        setContentView(R.layout.activity_play_punjabi);
        progressDialog=new ProgressDialog(PlayPunjabiActivity.this);
        progressDialog.setMessage("Buffering");
        progressDialog.setCancelable(false);
        progressDialog.show();
        initViews();

       // new FetchSongsTask().execute("");

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference pathref : listResult.getItems()) {
                    String path = pathref.getStorage().toString();
                    Log.i("PATH", "" + path);
                    arrayPath.add(path);
                    pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String songUrl = uri.toString();
                            Log.i("URL", "" + songUrl);
                           arraySongs.add(songUrl);
                            initMediaplayer();
                        }
                    });

                }


            }
        });


    }

    void initViews(){
        play=findViewById(R.id.online_play);
        play.setVisibility(View.INVISIBLE);
        name=findViewById(R.id.online_musicname);
        cover=findViewById(R.id.online_cover);
        playing=false;
        Intent rcv=getIntent();
        selectMusicLanguage=new SelectMusicLanguage();
        selectMusicLanguage.image=rcv.getIntExtra("keyImage",0);
        selectMusicLanguage.Name=rcv.getStringExtra("keyName");
        storage = FirebaseStorage.getInstance("gs://fithits-2f7b1.appspot.com");
        if(selectMusicLanguage.Name.equals("Keep Punjabi")){
            storageRef = storage.getReference().child("punjabi_music");
            Log.i("Storage", storageRef.toString());
        }else if(selectMusicLanguage.Name.equals("BollyWood Hits")){
            storageRef = storage.getReference().child("bollywood_music");
            Log.i("Storage", storageRef.toString());
        }else if(selectMusicLanguage.Name.equals("Rap Trap")){
            storageRef = storage.getReference().child("rap_trap");
            Log.i("Storage", storageRef.toString());
        }


        name.setText(""+selectMusicLanguage.Name);
        cover.setImageResource(selectMusicLanguage.image);

        txtinittime=findViewById(R.id.starttime);
        txtendtime=findViewById(R.id.endtime);
//        txtBuffering=findViewById(R.id.buffering);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        arrayPath=new ArrayList<>();
arraySongs=new ArrayList<>();

//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                Log.i("Music","Prepared");
//                if(playing==false) {
//                    mediaPlayer.start();
//                    playing = true;
//                }else{
//                    mediaPlayer.pause();
//                    playing=false;
//                }
//            }
//        });

    }
    void initMediaplayer(){

//        new FetchSongsTask().execute("");

        if(onetimemediaset==0) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            current = arraySongs.get(0);
            try {
                mediaPlayer.setDataSource(current);
                mediaPlayer.prepare();
//                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            play.setOnClickListener(this);
            play.setVisibility(View.VISIBLE);
            onetimemediaset=1;
            progressDialog.dismiss();
        }
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.online_play) {
            if (playing == false) {
//                   fetchSongsTask.execute("");
                play.setImageResource(R.drawable.ic_online_pause);
                mediaPlayer.start();
                startTime = mediaPlayer.getCurrentPosition();
                finalTime = mediaPlayer.getDuration();
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
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        Log.i("Music", "Error");
                        mediaPlayer.reset();
                        return false;
                    }
                });
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        if (i < 0 || i > 100) {
                            //System.out.println("Doing math: (" + (Math.abs(percent)-1)*100.0 + " / " +Integer.MAX_VALUE+ ")" );
                            i = (int) Math.round((((Math.abs(i) - 1) * 100.0 / Integer.MAX_VALUE)));
                            Log.i("Music", "Buffering..." + i);
//                            bufftime=i;
//                            txtBuffering.setText("Buffering..." + bufftime);
//                            Toast.makeText(PlayPunjabiActivity.this,"Buffering..."+bufftime,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.i("Music", "Completed");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        int pos = 0;
                        for (int i = 0; i < arraySongs.size() - 1; i++) {
                            String s = arraySongs.get(i);
                            if (s.equals(current)) {
                                Random random = new Random();
                                pos = random.nextInt(arraySongs.size());
                                if (pos == arraySongs.size()) {
                                    pos = 0;
                                    i = 0;
                                }
                                break;
                            }
                        }
                        try {
                            Log.i("Music", "Source" + pos);
                            mediaPlayer.setDataSource("" + arraySongs.get(pos));
                            current = arraySongs.get(pos);
                            mediaPlayer.prepare();
                            seekbar.setMax((int) finalTime);
                            myHandler.postDelayed(UpdateSongTime, 0);
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
//                            mediaPlayer.start();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                playing = true;
            } else {
                play.setImageResource(R.drawable.ic_online_play);
                mediaPlayer.pause();
                startTime = mediaPlayer.getCurrentPosition();
                playing = false;
            }
        }

    }

    class FetchSongsTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            current=OnlineMusic.punjabimusic.get(0);
            try {
                mediaPlayer.setDataSource(""+OnlineMusic.punjabimusic.get(0));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    Log.i("Music","Prepared");
//                    if(playing==false) {
//                        mediaPlayer.start();
//                        playing = true;
//                    }else{
//                        mediaPlayer.pause();
//                        playing=false;
//                    }
//                }
//            });
//            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                    Log.i("Music","Error");
//                    mediaPlayer.reset();
//                    return false;
//                }
//            });
//            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//                @Override
//                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//                    if (i < 0 || i > 100) {
//                        //System.out.println("Doing math: (" + (Math.abs(percent)-1)*100.0 + " / " +Integer.MAX_VALUE+ ")" );
//                        i = (int) Math.round((((Math.abs(i)-1)*100.0/Integer.MAX_VALUE)));
//                        Toast.makeText(PlayPunjabiActivity.this,"Buffering..."+i,Toast.LENGTH_SHORT).show();
//
//                    }
//                    Log.i("Music","Buffering"+i);
//                }
//            });
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    Log.i("Music","Completed");
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    int pos=0;
//                    for(int i=0;i<OnlineMusic.punjabimusic.size()-1;i++) {
//                        String s = OnlineMusic.punjabimusic.get(i);
//                        if (s.equals(current)) {
//                            Random random=new Random();
//                            pos = random.nextInt(OnlineMusic.punjabimusic.size());
//                            if(pos==OnlineMusic.punjabimusic.size()){
//                                pos=0;
//                                i=0;
//                            }
//                            break;
//                        }
//                    }
//                    try {
//                        Log.i("Music","Source"+pos);
//                        mediaPlayer.setDataSource(""+OnlineMusic.punjabimusic.get(pos));
//                        current=OnlineMusic.punjabimusic.get(pos);
//                        mediaPlayer.prepare();
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(PlayPunjabiActivity.this);
            progressDialog.setMessage("Buffering");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mediaPlayer.start();
progressDialog.dismiss();
        }


    }

}
