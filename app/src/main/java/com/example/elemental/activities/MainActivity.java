package com.example.elemental.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.elemental.R;
import com.example.elemental.models.WorkoutPlan;
import com.example.elemental.service.Service;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnItemSelectedListener {

    //activity
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public static MainActivity mainActivity;
    private ActionBarDrawerToggle actionBarToggle;
    public NavigationView navigationView;
    private static boolean startOnce = false;
    public static SharedPreferences sharedPreferences,sharedPreferences2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nightmode();
        setContentView(R.layout.activity_main);

        mainActivity = this;
        if(!startOnce) {
            startService(new Intent(getApplicationContext(), Service.class));
            startOnce = true;
        }


        toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigview);
        navigationView.bringToFront();
        BottomNavigationView bottomNavigationView = findViewById(R.id.Bottom_navigation);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.Nav_container);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportActionBar().setTitle("ELEMENTAL");
    }

    //funksjon som da sjekker om det skal være natt tema eller ikke
    private void nightmode(){
        sharedPreferences2 = getApplicationContext().getSharedPreferences("isNight",Context.MODE_PRIVATE);
        Boolean nightmode = sharedPreferences2.getBoolean("night_mode",false);


        if (nightmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.logout_app:
                removeSharedPreference();
                stopMyService();
                signOut();
                finish();
                removeDarkmode();
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

//funksjon som fjerner alle sharedpreferences
    public static void removeSharedPreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("password");
        editor.remove("email");
        editor.apply();

        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.remove("night_mode");
        editor2.apply();

    }

//funksjon som stopper service
    private void stopMyService(){
        stopService(new Intent(getApplicationContext(),Service.class));
    }

    //funksjon som håndterer ting som må logges ut
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        super.onBackPressed();
    }

    //funksjon som fjerner natt tema
    private void removeDarkmode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            //Bottom navigation navigation
            case R.id.sportTipsFragment:
            case R.id.sportdrawmenu:
                navigationView.setCheckedItem(R.id.sportdrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.sportTipsFragment);
                break;

            case R.id.homeFragment:

                //navigation drawer navigation
            case R.id.homedrawmenu:
                navigationView.setCheckedItem(R.id.homedrawmenu);
               // Navigation.findNavController(item.getActionView()).navigate(R.id.t_to_tricepsOutsideFragment);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.homeFragment);
                break;

            case R.id.BMISkjemaFragment:
            case R.id.bmidrawmenu:
                navigationView.setCheckedItem(R.id.bmidrawmenu);
                Navigation.findNavController(this,  R.id.Nav_container).navigate(R.id.BMISkjemaFragment);
                break;

            case R.id.calendarFragment:
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
                stopMyService();
                signOut();
                finish();
                removeDarkmode();
                break;

        }
        return false;
    }

}
