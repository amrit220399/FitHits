package com.apsinnovations.fithits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup extends AppCompatActivity {
    EditText eTxtName, eTxtEmail, eTxtPassword;
    Button btnSignup;
    TextView txtLogin;
    User user;
    ProgressDialog progressDialog;

    void initviews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        eTxtName = findViewById(R.id.username);
        eTxtEmail = findViewById(R.id.email);
        eTxtPassword = findViewById(R.id.password);
        btnSignup = findViewById(R.id.signup_btn);
        txtLogin = findViewById(R.id.login);
        user = new User();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.name = eTxtName.getText().toString().trim();
                user.email = eTxtEmail.getText().toString().trim();
                user.password = eTxtPassword.getText().toString().trim();

                //Toast.makeText(signup.this, user.toString(), Toast.LENGTH_SHORT).show();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
               // String namePattern = "[a-z]";
                boolean flag=true;

                 if (!(Patterns.EMAIL_ADDRESS.matcher(user.email).matches())){
                    flag=false;
                    eTxtEmail.setText("");
                    eTxtEmail.setFocusable(true);
                    eTxtEmail.setError("Invalid Email");

                }if(user.password.length()<8){
                    flag=false;
                    eTxtPassword.setText("");
                    eTxtPassword.setFocusable(true);
                    eTxtPassword.setError("Invalid Password");

                }if(user.name.isEmpty()){
                    flag=false;
                    eTxtName.setText("");
                    eTxtName.setFocusable(true);
                    eTxtName.setError("Invalid Name");
                }
//                if(!(user.name.matches(namePattern)) && (user.name.length() > 0))
//                 {
//                    flag=false;
//                    eTxtName.setText("");
//                    eTxtName.setFocusable(true);
//                    eTxtName.setError("Invalid Name");
//                    progressDialog.dismiss();
//                }
                if(flag==true){
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                        registerUserInFirebase();
                }
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        initviews();
    }


    void registerUserInFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInFirebase();

                        } else {
                            progressDialog.dismiss();
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(signup.this,"User Already Registered",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(signup.this, "Something Went Wrong !!", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });


    }

    void saveUserInFirebase() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid(); // This is uid of User which we have just created
        Log.i("SignupUser => ",""+user);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).set(user)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(signup.this, NavigationDrawerActivity.class);
                            startActivity(intent);
                            showNotification();
                            finish();
                        } else {
                            Toast.makeText(signup.this, "Something Went Wrong !!!", Toast.LENGTH_LONG).show();
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
        builder.setContentText("It's more than a Music App");
        builder.setSmallIcon(icon);
        builder.setWhen(when);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(101, notification);

    }
}

