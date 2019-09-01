package com.apsinnovations.fithits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                user.name = eTxtName.getText().toString();
                user.email = eTxtEmail.getText().toString();
                user.password = eTxtPassword.getText().toString();

                //Toast.makeText(signup.this, user.toString(), Toast.LENGTH_SHORT).show();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
               // String namePattern = "[a-z]";
                boolean flag=true;
                progressDialog.show();
                progressDialog.setCancelable(false);
                if (!((user.email.matches(emailPattern)) && (user.email.length() > 0))){
                    flag=false;
                    eTxtEmail.setText("");
                    eTxtEmail.setFocusable(true);
                    eTxtEmail.setError("Invalid Email");
                    progressDialog.dismiss();
                }if(!(user.password.length()>8)){
                    flag=false;
                    eTxtPassword.setText("");
                    eTxtPassword.setFocusable(true);
                    eTxtPassword.setError("Invalid Password");
                    progressDialog.dismiss();
                }
                if(!(user.name.length()>0)){
                    flag=false;
                    eTxtName.setText("");
                    eTxtName.setFocusable(true);
                    eTxtName.setError("Invalid Name");
                    progressDialog.dismiss();
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
                        registerUserInFirebase();
                }
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, Login_Activity.class);
                startActivity(intent);
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
                        if (task.isComplete()) {
                            saveUserInFirebase();

                        } else {
                            Toast.makeText(signup.this, "Something Went Wrong !!", Toast.LENGTH_LONG).show();
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
                        if (task.isComplete()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(signup.this, NavigationDrawerActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(signup.this, "Something Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}

