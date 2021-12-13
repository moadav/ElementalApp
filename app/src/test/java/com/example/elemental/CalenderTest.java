package com.example.elemental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.elemental.Fragments.CalendarDateFragment;
import com.example.elemental.Fragments.CalendarFragment;
import com.example.elemental.models.User;
import com.example.elemental.models.WorkoutPlan;

import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalenderTest {


    @Test
    public void CheckIfCalenderGetsValues() {
        CalendarFragment calendarFragment = new CalendarFragment();
        LocalDate localDate = LocalDate.parse("2021-11-23");
        ArrayList<LocalDate> s = calendarFragment.daysInMonthArray(localDate);
        assertEquals(42,s.size());
    }
    @Test
    public void CheckIfCorrectDate() {

        //funksjon implemntasjon hentet fra CalenderFragment
        CalendarFragment.selectedDate = LocalDate.parse("2021-11-23");
        LocalDate newdate =  CalendarFragment.selectedDate.minusMonths(1);
        assertEquals(newdate.getMonth().getValue(),10);
    }

}
