package com.example.elemental;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.elemental.Fragments.CalendarFragment;
import com.example.elemental.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnItemSelectedListener {

    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarToggle;
    NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private FirebaseFirestore  db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  workoutplansChannel();

        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(),Service.class));

        toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigview);
        getWorkouts();

        navigationView.bringToFront();
        BottomNavigationView bottomNavigationView = findViewById(R.id.Bottom_navigation);
        NavController navController = Navigation.findNavController(this,  R.id.Nav_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(this);


        getSupportActionBar().setTitle("Elemental");


//alarm();
    }

    @Override
    protected void onResume() {
        super.onResume();

        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerMenu);

        actionBarToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        actionBarToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);

    }
    private void alarm(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);



        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,33);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        Toast.makeText(this, "Alarm is set!" , Toast.LENGTH_SHORT).show();

    }
    private void chancel(){
         alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);


        alarmManager.cancel(pendingIntent);

    }

    private void workoutplansChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "workoutplansChannel";

            String desk = "channel for workouts";

            int important = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("workoutandroid",name,important);
            channel.setDescription(desk);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }



    private void getWorkouts(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(sharedPreferences.getString("email",null))) {

                            db.collection("users").document(document.getId()).collection("workouts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        String date = document.getString("date");
                                        String desc = document.getString("description");
                                        String name = document.getString("name");
                                        String time = document.getString("time");
                                        WorkoutPlan workoutPlan = new WorkoutPlan(name,date,desc,time);
                                        WorkoutPlan.workoutPlans.add(workoutPlan);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("error",e.toString(),null);
                                }
                            });
                            return;
                        }
                    }
                }
            }
        });








    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.logout_app:
                removeSharedPreference();
                stopService();
                signOut();
                break;
            case R.id.profile_app:
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.profileFragment);
                break;
        }

        return super.onOptionsItemSelected(item) || actionBarToggle.onOptionsItemSelected(item);

    }


    private void removeSharedPreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("password");
        editor.remove("email");
        editor.apply();
    }


    private void stopService(){
        stopService(new Intent(getApplicationContext(),Service.class));
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            //Bottom navigation navigation
            case R.id.sportTipsFragment:
                navigationView.setCheckedItem(R.id.sportdrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.sportTipsFragment);
                break;

            case R.id.homeFragment:
                navigationView.setCheckedItem(R.id.homedrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.homeFragment);
                break;

            case R.id.BMIKalkulatorFragment:
                navigationView.setCheckedItem(R.id.bmidrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.BMIKalkulatorFragment);
                break;

            case R.id.calendarFragment:
                navigationView.setCheckedItem(R.id.calendardrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.calendarFragment);
                break;

                //navigation drawer navigation
            case R.id.homedrawmenu:
                navigationView.setCheckedItem(R.id.homedrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.homeFragment);
                break;
            case R.id.sportdrawmenu:
                navigationView.setCheckedItem(R.id.sportdrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.sportTipsFragment);
                break;
            case R.id.bmidrawmenu:
                navigationView.setCheckedItem(R.id.bmidrawmenu);
                Navigation.findNavController(this, R.id.Nav_container).navigate(R.id.BMIKalkulatorFragment);
                break;
            case R.id.calendardrawmenu:
                navigationView.setCheckedItem(R.id.calendardrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.calendarFragment);
                break;

                case R.id.profile:
                    Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.profileFragment);
                    break;

        }
        return false;
    }
}
