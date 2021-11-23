package com.example.elemental.Fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.elemental.MainActivity;
import com.example.elemental.R;
import com.example.elemental.Service;
import com.example.elemental.WorkoutPlan;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutItemsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int hour,minute;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private ProgressBar progresscircle;
    private Button savebutton, timebutton;
    private EditText myplanedittext,titleEditText;
    private TextView date,time,description,name;
    private Service service = new Service();

    public WorkoutItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutItemsFragment newInstance(String param1, String param2) {
        WorkoutItemsFragment fragment = new WorkoutItemsFragment();
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

        progresscircle = getView().findViewById(R.id.progresscircle);

        savebutton = getView().findViewById(R.id.savebutton);
        savebutton.setOnClickListener(this);
        timebutton = getView().findViewById(R.id.timebutton);
        timebutton.setOnClickListener(this);
        myplanedittext = getView().findViewById(R.id.myplanedittext);
        titleEditText = getView().findViewById(R.id.titleEditText);

        date = getView().findViewById(R.id.date);
        time = getView().findViewById(R.id.time);
        description = getView().findViewById(R.id.description);
        name = getView().findViewById(R.id.name);


        date.setText(ProfileFragment.singleworkout.getDate());
        time.setText(ProfileFragment.singleworkout.getTime());
        description.setText(ProfileFragment.singleworkout.getDescription());
        name.setText(ProfileFragment.singleworkout.getName());
    }


    private void Update(){
        progresscircle.setVisibility(View.VISIBLE);

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
                                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                                            if (document2.getLong("workoutNumber") == ProfileFragment.singleworkout.getWorkoutNumber()) {
                                                String description = myplanedittext.getText().toString();
                                                String name = titleEditText.getText().toString();
                                                String time = timebutton.getText().toString();
                                                long workoutNumber = document2.getLong("workoutNumber");
                                                db.collection("users").document(document.getId()).collection("workouts").document(document2.getId()).update("description", description, "name", name, "time", time).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        LocalDate myDateForWorkout = LocalDate.parse(date.getText().toString());
                                                        service.fixPendingintent(getContext(), workoutNumber, myDateForWorkout.getMonthValue(), hour, minute, myDateForWorkout.getDayOfMonth(), myDateForWorkout.getYear());
                                                        WorkoutPlan.workoutPlans.get(ProfileFragment.workoutposition).setDescription(description);
                                                        WorkoutPlan.workoutPlans.get(ProfileFragment.workoutposition).setName(name);
                                                        WorkoutPlan.workoutPlans.get(ProfileFragment.workoutposition).setTime(time);

                                                        Toast.makeText(getContext(), "Workout updated!", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                });

                                            }
                                        }

                                    }else
                                        Toast.makeText(getContext(), "Error with updating data", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error with saving data", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                    }
                }
            }
        });
        progresscircle.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_items, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.timebutton:
                timePicker();
                break;
            case R.id.savebutton:
                Update();
                Navigation.findNavController(getActivity(),  R.id.Nav_container).navigate(R.id.profileFragment);
                break;
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