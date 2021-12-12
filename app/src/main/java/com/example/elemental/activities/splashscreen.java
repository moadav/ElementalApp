package com.example.elemental.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.elemental.R;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2*1000);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    Log.d("ERROR", e.toString());
                }
            }
        };
        // start thread
        background.start();
    }
}