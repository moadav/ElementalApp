package com.example.elemental;

import com.google.firebase.database.Exclude;

import java.time.LocalDate;
import java.util.ArrayList;

public class WorkoutPlan {

    private String name;
    private String date;
    private String description;
    private String time;

    public static ArrayList<WorkoutPlan> workoutPlans = new ArrayList<>();


    public static ArrayList<WorkoutPlan> getWorkoutPlans(){
        return  workoutPlans;
    }

    public WorkoutPlan(String name,String date, String description,String time) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.time = time;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
