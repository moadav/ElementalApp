package com.example.elemental;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.NavigationUI;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.core.View;


public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarToggle;

    /*
       <androidx.drawerlayout.widget.DrawerLayout
    android:id="@+id/drawerLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:openDrawer="start">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true">

        <com.google.android.material.appbar.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            style="@style/Widget.MaterialComponents.AppBarLayout.PrimarySurface"
            android:fitsSystemWindows="true">

            <com.google.android.material.appbar.MaterialToolbar
                android:id="@+id/topAppBar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:title="ttt"
                app:navigationIcon="@drawable/ic_baseline_menu_24"
                style="@style/Widget.MaterialComponents.Toolbar.PrimarySurface"
                android:background="@android:color/transparent"
                android:elevation="0dp" />

        </com.google.android.material.appbar.AppBarLayout>

        <!-- Screen content -->
        <!-- Use app:layout_behavior="@string/appbar_scrolling_view_behavior" to fit below top app bar -->

    </androidx.coordinatorlayout.widget.CoordinatorLayout>



</androidx.drawerlayout.widget.DrawerLayout>


 <com.google.android.material.appbar.MaterialToolbar
                android:id="@+id/topAppBar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:title="ttt"

                />

<com.google.android.material.appbar.MaterialToolbar
                android:id="@+id/topAppBar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:title="ttt"
                app:navigationIcon="@drawable/ic_baseline_menu_24"
                style="@style/Widget.MaterialComponents.Toolbar.PrimarySurface"
                android:background="@android:color/transparent"
                android:elevation="0dp" />
     */


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
    protected void onResume() {
        super.onResume();

        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawerMenu);


        actionBarToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        actionBarToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.item:
                        Toast.makeText(getApplicationContext(), "Failed to register! Please try again", Toast.LENGTH_LONG).show();
                        break;

                }



                return false;
            }
        });




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
                Intent login = new Intent(this,LoginActivity.class);
                startActivity(login);

                break;
        }
        return super.onOptionsItemSelected(item) || actionBarToggle.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
