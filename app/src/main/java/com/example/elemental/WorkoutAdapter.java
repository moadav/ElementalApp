package com.example.elemental;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.elemental.Fragments.CalendarFragment;
import com.example.elemental.Fragments.WorkoutPlanFragment;

import java.lang.reflect.WildcardType;
import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<WorkoutPlan> {


    public WorkoutAdapter(@NonNull Context context, List<WorkoutPlan> workoutPlanList) {
        super(context, 0,workoutPlanList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WorkoutPlan workoutPlan = getItem(position);

        if (convertView == null )
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendarcell,parent,false);
        TextView cellDay = convertView.findViewById(R.id.cellDay);



        String workoutTitle ="Date: " + workoutPlan.getDate() + "\n" + "Title: " + workoutPlan.getName() + "\n" + "Description: " + workoutPlan.getDescription() + "\n" + "Time: " + workoutPlan.getTime();
        cellDay.setText(workoutTitle);
        return convertView;

    }
}
