package com.apsinnovations.fithits;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

Handler handler=new Handler(){
    @Override
    public void handleMessage(@NonNull Message msg) {
        if(msg.what == 111){
            Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            finish();
        }else{
            Intent intent = new Intent(MainActivity.this, signup.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            finish();
        }
    }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            handler.sendEmptyMessageDelayed(111, 1000);
        }else{
            handler.sendEmptyMessageDelayed(222, 1000);
        }
    }
}
