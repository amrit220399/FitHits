package com.apsinnovations.fithits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    TextView eTxtEmail, eTxtUsername, eTxtMobile, eTxtPassword;
    Button btnlogout;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initviews();
    }

    void initviews(){
        eTxtEmail = findViewById(R.id.profile_Email);
        eTxtUsername = findViewById(R.id.profile_User);
        eTxtMobile = findViewById(R.id.profile_mobile);
        eTxtPassword= findViewById(R.id.profile_pass);
        btnlogout=findViewById(R.id.profile_Logout);

        fetchProfile();

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
                finish();
                Intent intent=new Intent(ProfileActivity.this,Login_Activity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void fetchProfile(){
        user=new User();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            user=document.toObject(User.class);
                            Log.i("User => ",""+user);
                            eTxtUsername.setText(user.name);
                            eTxtEmail.setText(user.email);
                            eTxtMobile.setText(user.email);
                            String pass=user.password;
                            String mask = pass.replaceAll("\\w(?=\\w{4})", "*");
                            eTxtPassword.setText(mask);
                        } else {
                            Log.i("Username", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
