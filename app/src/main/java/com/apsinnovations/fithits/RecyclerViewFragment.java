package com.apsinnovations.fithits;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    ArrayList<Song> songs;
View view;
    TextView textView;
    LinearLayout indexLayout;
ProgressDialog progressDialog;


    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().getActionBar().setTitle("MyMusic");
        view= inflater.inflate(R.layout.fragment_recycler_view,container,false);
        recyclerView = view.findViewById(R.id.recyclerViewfragment);
            new MyAsyncTask().execute();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem=menu.findItem(R.id.searchview);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                musicAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    class MyAsyncTask extends AsyncTask{

    @Override
    protected Object doInBackground(Object[] objects) {

            ContentResolver musicResolver = getActivity().getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Uri albumUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            Cursor albumCursor;
            songs = new ArrayList<Song>();

            if (musicCursor != null && musicCursor.moveToFirst()) {
                int id = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.TITLE_KEY);
                int title = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int artist = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int location = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.DATA);
                int albumid = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);
                do {
                    String thisTitle = musicCursor.getString(title);
                    String thisArtist = musicCursor.getString(artist);
                    String thisLocation = musicCursor.getString(location);
                    Long thisalbum = Long.valueOf(musicCursor.getString(albumid));
                    String thisid = musicCursor.getString(id);
                    albumCursor = musicResolver.query(albumUri, null,
                            MediaStore.Audio.Albums._ID + "=" + thisalbum, null, null);
                    if (albumCursor != null && albumCursor.moveToFirst()) {
                        String albumCoverPath = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        songs.add(new Song(thisid,albumCoverPath, thisTitle, thisArtist, thisLocation));
                        albumCursor.close();
                    } else {
                        Song song = new Song();
                        song.putSongData(thisid,thisTitle, thisArtist, thisLocation);
                        songs.add(song);
                    }
                }
                while (musicCursor.moveToNext());

            }
            //Song.songs=songs;
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        musicAdapter = new MusicAdapter(getActivity(), R.layout.song_card_view, songs);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(musicAdapter);
        progressDialog.dismiss();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading MyMusic");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}




}
