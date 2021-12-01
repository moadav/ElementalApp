package com.example.elemental.models;

import java.util.ArrayList;

public class WorkoutPlan {

    private String name;
    private String date;
    private String description;
    private String time;
    private long workoutNumber;

    public static ArrayList<WorkoutPlan> workoutPlans = new ArrayList<>();

    public WorkoutPlan(String name,String date, String description,String time, long workoutNumber) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.time = time;
        this.workoutNumber = workoutNumber;
    }


    public String getDate() {
        return date;
    }

    public long getWorkoutNumber() {
        return workoutNumber;
    }

    public void setWorkoutNumber(long workoutNumber) {
        this.workoutNumber = workoutNumber;
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
