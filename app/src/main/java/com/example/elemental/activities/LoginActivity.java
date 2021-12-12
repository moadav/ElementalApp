package com.example.elemental.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.elemental.R;
import com.example.elemental.service.Service;

public class LoginActivity extends AppCompatActivity  {

    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}