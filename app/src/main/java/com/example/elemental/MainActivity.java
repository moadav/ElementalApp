package com.example.elemental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.elemental.Fragments.BMIKalkulatorFragment;
import com.example.elemental.Fragments.CalendarFragment;
import com.example.elemental.Fragments.HomeFragment;
import com.example.elemental.Fragments.SportTipsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private Toolbar toolbar;
    private BMIKalkulatorFragment bmiKalkulatorFragment = new BMIKalkulatorFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private SportTipsFragment sportTipsFragment = new SportTipsFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);
        loadFragmentPage(homeFragment);
        bottomNavigation = findViewById(R.id.Bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sport_check:
                loadFragmentPage(sportTipsFragment);
                return true;

            case R.id.calendar_check:
                loadFragmentPage(calendarFragment);
                return true;

            case R.id.bmi_check:
                loadFragmentPage(bmiKalkulatorFragment);
                return true;

            case R.id.home_check:
                loadFragmentPage(homeFragment);
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                return true;

        }

        return false;
    }


    private void loadFragmentPage(Fragment fragment){
        if (getSupportFragmentManager().findFragmentById(R.id.Nav_container) !=  fragment)
            getSupportFragmentManager().beginTransaction().replace(R.id.Nav_container,fragment).addToBackStack(null).commit();
    }

}
