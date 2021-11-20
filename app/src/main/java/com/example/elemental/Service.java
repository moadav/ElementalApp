package com.example.elemental;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Service extends android.app.Service {
    private PendingIntent pendingIntent;
    private Calendar calendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

public static Date returnTime(String time){
    DateFormat formatter = new SimpleDateFormat("hh:mm");
    Date date = null;
    try {
        date = (Date)formatter.parse(time);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    return date;
}

    private void chancel(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);


        alarmManager.cancel(pendingIntent);

    }

    private void alarm(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmBroadcastReceiver.class);


        int id = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_ONE_SHOT);



        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,14);
        calendar.set(Calendar.MINUTE,58);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        id = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_ONE_SHOT);


        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,14);
        calendar.set(Calendar.MINUTE,59);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        Toast.makeText(this, "Alarm is set!" , Toast.LENGTH_SHORT).show();

    }

    private void fixPendingintent(long id,int month,long milli,int day,int year){
        Intent intent = new Intent(this,AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,(int)id,intent,PendingIntent.FLAG_ONE_SHOT);





        int minutes = (int) ((milli / (1000*60)) % 60);
        int hours   = (int) ((milli / (1000*60*60)) % 24);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hours);
        calendar.set(Calendar.MINUTE,minutes);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.DAY_OF_MONTH,day);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }



    public void test(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);



        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email",null))) {

                            db.collection("users").document(document.getId()).collection("workouts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                                        LocalDate localDate = LocalDate.parse(documentSnapshot.getString("date"));

                                        if(LocalDate.now().isBefore(localDate) || returnTime(currentTime).before(returnTime(documentSnapshot.getString("time"))) )
                                        fixPendingintent(documentSnapshot.getLong("workoutNumber"),localDate.getMonth().getValue(),returnTime(documentSnapshot.getString("time")).getTime(),localDate.getDayOfMonth(),localDate.getYear());
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
    public int onStartCommand(Intent intent, int flags, int startId) {
workoutplansChannel();
//alarm();
test();
        return START_STICKY;
    }
}
