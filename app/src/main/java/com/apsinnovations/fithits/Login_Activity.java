package com.apsinnovations.fithits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login_Activity extends AppCompatActivity {
    EditText eTxtEmail, eTxtPass;
    Button btnLogin;
    TextView txtSignup;
    User user;
    ProgressDialog progressDialog;
    void initviews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        eTxtEmail = findViewById(R.id.email);
        eTxtPass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_btn);
        txtSignup = findViewById(R.id.signup);
        user = new User();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.email = eTxtEmail.getText().toString().trim();
                user.password = eTxtPass.getText().toString().trim();
                boolean flag=true;
                if (!(Patterns.EMAIL_ADDRESS.matcher(user.email).matches())){
                    flag=false;
                    eTxtEmail.setText("");
                    eTxtEmail.setFocusable(true);
                    eTxtEmail.setError("Invalid Email");

                }if(user.password.length()<8){
                    flag=false;
                    eTxtPass.setText("");
                    eTxtPass.setFocusable(true);
                    eTxtPass.setError("Invalid Password");

                }
                if(flag==true) {
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    loginUserfromFirebase();
                }

            }
        });


        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, signup.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        getSupportActionBar().hide();
        initviews();
    }

    void loginUserfromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            showNotification();
                            Toast.makeText(Login_Activity.this, "Login Success!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login_Activity.this, NavigationDrawerActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(Login_Activity.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                            }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                Toast.makeText(Login_Activity.this,"User Not Found",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(Login_Activity.this, "Something Went Wrong !!", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }

    void showNotification(){

        // To show the Notification
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        int icon=R.mipmap.ic_launcher_round;
        long when=System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Associate a NotificationChannel to NotificationManager
            NotificationChannel notificationChannel = new NotificationChannel("myId", "myChannel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 111, intent, 0);

        // Create Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myId");
        builder.setContentTitle("Welcome to FitHits");
        builder.setContentText("Good to See You Back");
        builder.setSmallIcon(icon);
        builder.setWhen(when);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(101, notification);

    }
}