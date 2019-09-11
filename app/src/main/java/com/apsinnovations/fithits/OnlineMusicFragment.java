package com.apsinnovations.fithits;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineMusicFragment extends Fragment {

    ArrayList<Song> songs;
MediaPlayer mediaPlayer;
    ArrayList<String> arrayList;
    String current;



    public OnlineMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        for(int i=0;i<OnlineMusic.punjabimusic.size();i++){
            Log.i("URL-Array",OnlineMusic.punjabimusic.get(i));
        }
//        for(int i=0;i<OnlineMusic.punjabimusicpath.size();i++){
//            Log.i("URLPath-Array",OnlineMusic.punjabimusicpath.get(i));
//        }
        new FetchSongsTask().execute("");
        return inflater.inflate(R.layout.fragment_online_music,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





    }

    class FetchSongsTask extends AsyncTask{

    @Override
    protected Object doInBackground(Object[] objects) {
        try {

                mediaPlayer=new MediaPlayer();
                current=OnlineMusic.punjabimusic.get(0);
                mediaPlayer.setDataSource(""+OnlineMusic.punjabimusic.get(0));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.prepareAsync();
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
        }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.i("Music","Prepared");
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.i("Music","Error");
                    mediaPlayer.reset();
                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    if (i < 0 || i > 100) {
                        //System.out.println("Doing math: (" + (Math.abs(percent)-1)*100.0 + " / " +Integer.MAX_VALUE+ ")" );
                        i = (int) Math.round((((Math.abs(i)-1)*100.0/Integer.MAX_VALUE)));

                    }
                    Log.i("Music","Buffering"+i);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.i("Music","Completed");
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    int pos=0;
                    for(int i=0;i<OnlineMusic.punjabimusic.size()-1;i++) {
                        String s = OnlineMusic.punjabimusic.get(i);
                        if (s.equals(current)) {
                            Random random=new Random();
                            pos = random.nextInt(OnlineMusic.punjabimusic.size());
                            if(pos==OnlineMusic.punjabimusic.size()){
                                pos=0;
                                i=0;
                            }
                            break;
                        }
                    }
                    try {
                        Log.i("Music","Source"+pos);
                        mediaPlayer.setDataSource(""+OnlineMusic.punjabimusic.get(pos));
                        current=OnlineMusic.punjabimusic.get(pos);
                        mediaPlayer.prepare();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}



}


