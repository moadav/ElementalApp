package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.elemental.Adapters.CalendarAdapter;
import com.example.elemental.interfaces.OnItemListener;
import com.example.elemental.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements OnItemListener,View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private Button lastWeekButton,nextWeekButton;
    FloatingActionButton workoutplan;
    private TextView MonthYear;
    private RecyclerView calenderRecyclerView;
    public static LocalDate  selectedDate = LocalDate.now();
    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        workoutplan = getView().findViewById(R.id.workoutplan);
        workoutplan.setOnClickListener(this);


        calenderRecyclerView = getView().findViewById(R.id.recyclerview);

        MonthYear = getView().findViewById(R.id.MonthYear);



        lastWeekButton = getView().findViewById(R.id.LastWeekButton);
        lastWeekButton.setOnClickListener(this);

        nextWeekButton = getView().findViewById(R.id.nextWeekButton);
        nextWeekButton.setOnClickListener(this);

        setMonthView();

    }
    //hentet fra https://www.youtube.com/watch?v=knpSbtbPz3o
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }
    //hentet fra https://www.youtube.com/watch?v=knpSbtbPz3o
    public void previousMonthAction()
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }
    //hentet fra https://www.youtube.com/watch?v=knpSbtbPz3o
    public void nextMonthAction()
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }


    //hentet fra https://www.youtube.com/watch?v=knpSbtbPz3o
    public ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
        return  daysInMonthArray;
    }

    //hentet fra https://www.youtube.com/watch?v=knpSbtbPz3o
    private void setMonthView()
    {
        MonthYear.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calenderRecyclerView.setLayoutManager(layoutManager);
        calenderRecyclerView.setAdapter(calendarAdapter);

    }
    //hentet fra https://www.youtube.com/watch?v=knpSbtbPz3o
    @Override
    public void onItemClick(int position, LocalDate day) {
        if(!(day == null))
        {
            CalendarFragment.selectedDate = day;
            setMonthView();
        }
    }






    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextWeekButton:
               nextMonthAction();
                break;
            case R.id.LastWeekButton:
                previousMonthAction();
                break;
            case R.id.workoutplan:
                Navigation.findNavController(view).navigate(R.id.action_calendarFragment_to_workoutPlanFragment);
                break;
        }
    }
}