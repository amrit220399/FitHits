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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView Name,Email;
    User user;

//StopWatchFragment stopWatchFragment;
ProgressDialog progressDialog;
//FragmentManager manager;
//FragmentTransaction fragmentTransaction;
//String backfragmentname;
//    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            manager=getSupportFragmentManager();
//            fragmentTransaction=manager.beginTransaction();
                Fragment fragment=null;

            switch (item.getItemId()) {
                case R.id.navigation_music:
                    fragment=new RecyclerViewFragment();
                    getSupportActionBar().setTitle("MyMusic");
                    showNotification();



//                    mTextMessage.setText("Loading...MyMusic");
//                    fragmentTransaction.replace(R.id.include,recyclerViewFragment).commit();

//                    return true;
                    break;
                case R.id.navigation_stopwatch:
                    fragment=new StopWatchFragment();
                    getSupportActionBar().setTitle("StopWatch");
//                   fragmentTransaction.replace(R.id.include,stopWatchFragment).commit();

//                    transaction.add(R.id.frame_layout, targetFragment, tag)
//                            .addToBackStack(tag)
//                            .commit();
//                    mTextMessage.setText("");
//                   mTextMessage.setText(R.string.title_Stopwatch);
//                    return true;
                    break;
                case R.id.navigation_online:
                    fragment=new OnlineMusicFragment();
                    getSupportActionBar().setTitle("OnlineMusic");
//                    fragmentTransaction.replace(R.id.include,recyclerViewFragment).commit();
//                    mTextMessage.setText(R.string.title_Online);

//                    return true;
                    break;
            }

            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        if ( fragment!= null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment,fragmentTag);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        fetchProfile();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
       // mTextMessage = findViewById(R.id.message);



        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//manager=getSupportFragmentManager();
//fragmentTransaction=manager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(0,0,0,0);

//        stopWatchFragment=new StopWatchFragment();
        navView.setSelectedItemId(R.id.navigation_music);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
//        MenuItem menuItem=menu.findItem(R.id.searchview);
//        SearchView searchView= (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                musicAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action


        } else if (id == R.id.nav_settings) {


        } else if (id == R.id.nav_languages) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_rate_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void fetchProfile(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        Name = hView.findViewById(R.id.txtName);
        Email = hView.findViewById(R.id.txtEmail);
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
                                Name.setText(user.name);
                                Email.setText(user.email);
                        } else {
                            Log.i("Username", "Error getting documents.", task.getException());
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