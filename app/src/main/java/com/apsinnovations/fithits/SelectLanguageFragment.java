package com.apsinnovations.fithits;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLanguageFragment extends Fragment {
RecyclerView recyclerView;
ArrayList<SelectMusicLanguage> selectMusicLanguages;
LanguageAdapter languageAdapter;
    ProgressDialog progressDialog;
    boolean layout;
    View view;

    public SelectLanguageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_select_language, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewLanguage);
        layout=false;
        new MyAsyncTask().execute();
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchview);
        final MenuItem viewItem = menu.findItem(R.id.action_layout);


        viewItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (layout == false) {
                    viewItem.setIcon(R.drawable.ic_view_list_black_24dp);
                    LinearLayoutManager linear = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linear);
                    languageAdapter = new LanguageAdapter(getActivity(), R.layout.card_select_online_music, selectMusicLanguages);
                    recyclerView.setAdapter(languageAdapter);
                    layout = true;
                } else if (layout == true) {
                    GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(grid);
                    languageAdapter = new LanguageAdapter(getActivity(), R.layout.card_grid_select_language, selectMusicLanguages);
                    recyclerView.setAdapter(languageAdapter);
                    viewItem.setIcon(R.drawable.ic_view_module_black_24dp);
                    layout = false;
                }
                return false;
            }
        });
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                languageAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
    class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            SelectMusicLanguage s1=new SelectMusicLanguage(R.drawable.punjabi_music,"Keep Punjabi");
            SelectMusicLanguage s2=new SelectMusicLanguage(R.drawable.bollywood_hits,"BollyWood Hits");
            SelectMusicLanguage s3=new SelectMusicLanguage(R.drawable.rap_trap,"Rap Trap");
            SelectMusicLanguage s4=new SelectMusicLanguage(R.drawable.late_nights,"Late Nights");
            SelectMusicLanguage s5=new SelectMusicLanguage(R.drawable.rock_music,"Rock Music");
            SelectMusicLanguage s6=new SelectMusicLanguage(R.drawable.italian_music,"Italian Music");
            selectMusicLanguages=new ArrayList<>();
            selectMusicLanguages.add(s1);
            selectMusicLanguages.add(s2);
            selectMusicLanguages.add(s3);
            selectMusicLanguages.add(s4);
            selectMusicLanguages.add(s5);
            selectMusicLanguages.add(s6);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(grid);
            languageAdapter=new LanguageAdapter(getActivity(),R.layout.card_grid_select_language,selectMusicLanguages);
            recyclerView.setAdapter(languageAdapter);
            progressDialog.dismiss();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }

}
