package com.apsinnovations.fithits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class PlayMusic extends AppCompatActivity {

    PlayMusicService player;
    boolean serviceBound = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
