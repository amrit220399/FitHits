package com.apsinnovations.fithits;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopWatchFragment extends Fragment {
    TextView textView ;

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 100);

            textView.setText("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };
    ImageView start,  reset, lap ;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler;

    int Seconds, Minutes, MilliSeconds ;
    boolean started=false;
    ListView listView ;
    String[] ListElements = new String[] {  };
    List<String> ListElementsArrayList ;
    MyTimeListAdapter adapter ;


    public StopWatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // getActivity().getActionBar().setTitle("Stopwatch");
        return inflater.inflate(R.layout.fragment_stop_watch, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.textView);
        start = view.findViewById(R.id.start);
        reset = view.findViewById(R.id.stop);
        lap = view.findViewById(R.id.lap) ;
        listView = view.findViewById(R.id.listview1);
        lap.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        handler = new Handler() ;
        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new MyTimeListAdapter(getContext(),
                R.layout.time_laps,
                ListElementsArrayList
        );

        listView.setAdapter(adapter);

    start.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(started==false) {
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            start.setImageResource(R.drawable.ic_stop_pause);
            reset.setVisibility(View.INVISIBLE);
            lap.setVisibility(View.VISIBLE);
            started=true;
            }
            else{
                        TimeBuff += MillisecondTime;
                        handler.removeCallbacks(runnable);
                        start.setImageResource(R.drawable.ic_watch_play);
                        reset.setVisibility(View.VISIBLE);
                        lap.setVisibility(View.INVISIBLE);
                        started=false;
                    }
        }
    });



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                textView.setText("00:00:00");
                lap.setVisibility(View.INVISIBLE);
                ListElementsArrayList.clear();

                adapter.notifyDataSetChanged();
                reset.setVisibility(View.INVISIBLE);
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListElementsArrayList.add(textView.getText().toString());

                adapter.notifyDataSetChanged();

            }
        });

    }
}
