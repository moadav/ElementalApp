package com.example.elemental.AlarmManager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.elemental.R;
import com.example.elemental.activities.MainActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent mainactivity = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent contentIntent = PendingIntent.getActivity(context,0,mainactivity,0);



        //Notifikasjonsmeldingen som jeg redigerer for å få ønsket verdi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"workoutandroid")
                .setSmallIcon(R.drawable.elemental_icon)
                .setContentTitle("Elemental")
                .setContentText("You have an upcoming workout plan!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent);





        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(511,builder.build());




    }
}
