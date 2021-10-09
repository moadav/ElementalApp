package com.example.elemental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.elemental.Fragments.BMIKalkulatorFragment;
import com.example.elemental.Fragments.CalendarFragment;
import com.example.elemental.Fragments.SportTipsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private Toolbar toolbar;
    private BMIKalkulatorFragment bmiKalkulatorFragment = new BMIKalkulatorFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private SportTipsFragment sportTipsFragment = new SportTipsFragment();
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);


        bottomNavigation = (BottomNavigationView) findViewById(R.id.Bottom_navigation);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.Nav_container,sportTipsFragment).commit();
                Log.d("asdasdasda","DICK DICK DICK");
                return true;

            case R.id.calendar_check:
                getSupportFragmentManager().beginTransaction().replace(R.id.Nav_container,calendarFragment).commit();
                Log.d("asdasdasda","DICK DICK DICK");

                return true;
            case R.id.bmi_check:
                getSupportFragmentManager().beginTransaction().replace(R.id.Nav_container,bmiKalkulatorFragment).commit();
                Log.d("asdasdasda","DICK DICK DICK");
                return true;

        }

        return false;
    }
}
