package com.example.elemental;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elemental.Fragments.CalendarFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Service extends android.app.Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

public static Date returnTime(String time){
    DateFormat formatter = new SimpleDateFormat("hh:mm a");
    Date date = null;
    try {
        date = (Date)formatter.parse(time);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    return date;
}


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }
}
