package com.example.elemental;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.NavigationUI;

import android.content.ClipData;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);
        
        BottomNavigationView bottomNavigationView = findViewById(R.id.Bottom_navigation);
        NavController navController = Navigation.findNavController(this,  R.id.Nav_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);
        getSupportActionBar().setTitle("Elemental");
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
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(item.getActionView()).navigate(R.id.action_homeFragment_to_loginActivity);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
