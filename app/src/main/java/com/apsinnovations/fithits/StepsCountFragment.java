package com.apsinnovations.fithits;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsCountFragment extends Fragment implements SensorEventListener {
     SensorManager mSensorManager;
     Sensor mSensor;
     boolean isSensorPresent = false;
     TextView mSteps,lSteps;
     SeekBar seekBar;

    public StepsCountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_steps_count, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSteps = view.findViewById(R.id.stepscount);
        lSteps=view.findViewById(R.id.stepsleft);
seekBar=view.findViewById(R.id.stepseekbar);
seekBar.setClickable(false);
seekBar.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
});
        mSensorManager =  (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
        {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        }
        else
        {
            isSensorPresent = false;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if(isSensorPresent)
        {
            mSensorManager.registerListener( this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isSensorPresent)
        {
            mSensorManager.unregisterListener(this);
        }else
            Toast.makeText(getActivity(),"Sensor Not Found!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mSteps.setText(String.valueOf(sensorEvent.values[0]));
        int s=(int)Float.parseFloat(String.valueOf(sensorEvent.values[0]));
        seekBar.setProgress(s);
        lSteps.setText("Well Done Buddy! Just "+(30000-s)+" more steps to go.");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
