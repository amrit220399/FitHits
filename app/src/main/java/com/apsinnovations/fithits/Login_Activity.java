package com.apsinnovations.fithits;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    EditText eTxtEmail, eTxtPass;
    Button btnLogin;
    TextView txtSignup;
    User user;

    void initviews() {
        eTxtEmail = findViewById(R.id.email);
        eTxtPass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_btn);
        txtSignup = findViewById(R.id.signup);
        user = new User();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.email = eTxtEmail.getText().toString();
                user.password = eTxtPass.getText().toString();
                loginUserfromFirebase();


            }
        });


        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, signup.class);
                startActivity(intent);
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
                        if (task.isComplete()) {
                            Toast.makeText(Login_Activity.this, "Login Success!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login_Activity.this, NavigationDrawerActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Login_Activity.this, "Something Went Wrong !!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}