package com.example.elemental;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.fragment.NavHostFragment;
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
import com.example.elemental.Fragments.OptionFragment;
import com.example.elemental.Fragments.ProfileFragment;
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
    public NavigationView navigationView;
    public static SharedPreferences sharedPreferences,sharedPreferences2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(),Service.class));


        toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigview);
        navigationView.bringToFront();
        BottomNavigationView bottomNavigationView = findViewById(R.id.Bottom_navigation);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.Nav_container);
        NavController navController = navHostFragment.getNavController();


       // NavController navController = Navigation.findNavController(this,  R.id.Nav_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportActionBar().setTitle("Elemental");
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerMenu);
        nightmode();
        actionBarToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        actionBarToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);

    }


    private void nightmode(){
        sharedPreferences2 = getApplicationContext().getSharedPreferences("isNight",Context.MODE_PRIVATE);
        Boolean nightmode = sharedPreferences2.getBoolean("night_mode",false);

        if (nightmode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    private void removeDarkmodeOptions(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.logout_app:
                removeDarkmodeOptions();
                removeSharedPreference();
                stopService();
                signOut();
                break;
            case R.id.profile_app:
                navigationView.setCheckedItem(R.id.profile);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.profileFragment);
                break;
            case R.id.options_app:
                navigationView.setCheckedItem(R.id.options);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.optionFragment);
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

            case R.id.BMISkjemaFragment:
                navigationView.setCheckedItem(R.id.bmidrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.BMISkjemaFragment);
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
                Navigation.findNavController(this, R.id.Nav_container).navigate(R.id.BMISkjemaFragment);
                break;
            case R.id.calendardrawmenu:
                navigationView.setCheckedItem(R.id.calendardrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.calendarFragment);
                break;


                case R.id.profile:
                    navigationView.setCheckedItem(R.id.profile);
                    Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.profileFragment);
                    break;

            case R.id.options:
                navigationView.setCheckedItem(R.id.options);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.optionFragment);
                break;
            case R.id.logout:
                removeSharedPreference();
                stopService();
                signOut();
                break;

        }
        return false;
    }
}
