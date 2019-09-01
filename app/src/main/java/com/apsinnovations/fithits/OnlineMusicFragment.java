package com.apsinnovations.fithits;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineMusicFragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    ArrayList<Song> songs;
    String url;


    public OnlineMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        new MyAsyncTask().execute();
        return inflater.inflate(R.layout.fragment_online_music,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.onlinerecyclerView);

        url="gs://fithits-2f7b1.appspot.com/";
        ContentResolver musicResolver = getActivity().getContentResolver();
        Cursor musicCursor = musicResolver.query(Uri.parse(url), null, null, null, null);
        songs = new ArrayList<Song>();

        if(musicCursor!=null && musicCursor.moveToFirst()){

            int title = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int artist = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int location= musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            do {
                String thisTitle = musicCursor.getString(title);
                String thisArtist = musicCursor.getString(artist);
                String thisLocation = musicCursor.getString(location);
                Song song=new Song();
                //song.putSongData(thisTitle, thisArtist, thisLocation);
                songs.add(song);

            }
            while (musicCursor.moveToNext());

            musicAdapter=new MusicAdapter(getActivity(),R.layout.song_card_view,songs);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager1);
            recyclerView.setAdapter(musicAdapter);
            setRetainInstance(true);
        }


    }
}


class MyAsyncTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        try{

            URL url = new URL("https://beatsapi.media.jio.com/v2_1/beats-api/jio/src/response/home/%7Blanguage%7D");

            URLConnection connection = url.openConnection();

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            StringBuilder builder;
            builder = new StringBuilder();


            while((line = reader.readLine()) !=null){
                builder.append(line);
            }

            // We now need to display the data in Toast or Log

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

