package com.example.elemental.Fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.elemental.R;
import com.example.elemental.Service;
import com.example.elemental.WorkoutPlan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutPlanFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button timebutton,savebutton;
    private int hour,minute;
    private Service service = new Service();
    public static EditText titleEditText,PlantEditText;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private ProgressBar progresscircle;


    public WorkoutPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutPlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutPlanFragment newInstance(String param1, String param2) {
        WorkoutPlanFragment fragment = new WorkoutPlanFragment();
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
    public void onResume() {
        super.onResume();

        db = FirebaseFirestore.getInstance();

        progresscircle = (ProgressBar) getView().findViewById(R.id.progresscircle);

        timebutton = getView().findViewById(R.id.timebutton);
        timebutton.setOnClickListener(this);

        savebutton = getView().findViewById(R.id.savebutton);
        savebutton.setOnClickListener(this);


        titleEditText = getView().findViewById(R.id.titleEditText);
        PlantEditText = getView().findViewById(R.id.myplanedittext);

        sharedPreferences = getContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_plan, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.timebutton:
                timePicker();
                break;
            case R.id.savebutton:
                saveWorkout();
                Navigation.findNavController(view).navigate(R.id.action_workoutPlanFragment_to_calendarFragment);
                break;



        }
    }


    private void saveWorkout(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("HHmmss",  Locale.JAPAN).format(now));
        WorkoutPlan workoutPlan = new WorkoutPlan(titleEditText.getText().toString(),CalendarFragment.selectedDate.toString(), PlantEditText.getText().toString(), timebutton.getText().toString(), id);
        progresscircle.setVisibility(View.VISIBLE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


        if(CalendarFragment.selectedDate.isBefore(LocalDate.now())){
            Toast.makeText(getActivity(), "Please select a date later than todays time!", Toast.LENGTH_LONG).show();
        }else {

            if (timebutton.getText().toString().equals("Select time") || titleEditText.getText().toString().isEmpty() || PlantEditText.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "The fields cannot be left empty!!", Toast.LENGTH_LONG).show();
            } else {

                LocalTime time = LocalTime.parse(timebutton.getText().toString());

                LocalTime nowTime = LocalTime.now();

                time.format(formatter);
                nowTime.format(formatter);

                if (CalendarFragment.selectedDate.equals(LocalDate.now()) && time.isBefore(nowTime)) {
                    Toast.makeText(getContext(), "Time cannot be less than todays time!", Toast.LENGTH_SHORT).show();
                } else {

                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().containsValue(sharedPreferences.getString("email", null))) {

                                        db.collection("users").document(document.getId()).collection("workouts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (CalendarFragment.selectedDate.isBefore(LocalDate.now())) {
                                                    Toast.makeText(getActivity(), "Workout failed to save. Please pick a better date", Toast.LENGTH_LONG).show();
                                                } else {

                                                    db.collection("users").document(document.getId()).collection("workouts").add(workoutPlan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            WorkoutPlan.workoutPlans.add(workoutPlan);
                                                            service.fixPendingintent(getContext(), id, CalendarFragment.selectedDate.getMonthValue(), hour, minute, CalendarFragment.selectedDate.getDayOfMonth(), CalendarFragment.selectedDate.getYear());
                                                            Toast.makeText(getActivity(), "Workout has been saved!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "Workout has not been saved", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("error", e.toString(), null);
                                            }
                                        });


                                        progresscircle.setVisibility(View.GONE);

                                        return;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }



    private void timePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timebutton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));



            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),onTimeSetListener,hour,minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}