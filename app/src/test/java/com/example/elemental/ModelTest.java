package com.example.elemental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.elemental.models.User;
import com.example.elemental.models.WorkoutPlan;

import org.junit.Test;

public class ModelTest {

    @Test
    public void CheckIfCorrectUserObject() {
            User user = new User("85", "195", "loa", "mo@gmail", "44");
            assertNotNull(user);
    }
    @Test
    public void CheckIfCorrectWorkoutObject() {
        WorkoutPlan workoutPlan = new WorkoutPlan("85", "12.12.2012", "descp", "12:12", 55);
        assertNotNull(workoutPlan);
    }

}
