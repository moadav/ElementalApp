package com.example.elemental;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Service extends android.app.Service {
    private static Calendar calendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void cancelAlarm(int id,Context context){
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_ONE_SHOT);


        alarmManager.cancel(pendingIntent);

    }

    public void fixPendingintent(Context context,long id,int month,int hour, int minute,int day,int year){
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,(int)id,intent,PendingIntent.FLAG_ONE_SHOT);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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

    private void getWorkouts(){


        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email",null))) {

                            db.collection("users").document(document.getId()).collection("workouts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(!task.getResult().isEmpty()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String date = document.getString("date");
                                            String desc = document.getString("description");
                                            String name = document.getString("name");
                                            String time = document.getString("time");
                                            long workoutNumber = document.getLong("workoutNumber");
                                            WorkoutPlan workoutPlan = new WorkoutPlan(name, date, desc, time, workoutNumber);
                                            WorkoutPlan.workoutPlans.add(workoutPlan);
                                        }
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



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        workoutplansChannel();
        getWorkouts();

        return START_STICKY;
    }
}
