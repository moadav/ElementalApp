package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.elemental.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutItemsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progresscircle;
    private Button savebutton, timebutton;
    private EditText myplanedittext,titleEditText;
    public static TextView date,time,description,name;

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

        progresscircle = getView().findViewById(R.id.progresscircle);

        savebutton = getView().findViewById(R.id.savebutton);
        timebutton = getView().findViewById(R.id.timebutton);
        myplanedittext = getView().findViewById(R.id.myplanedittext);
        titleEditText = getView().findViewById(R.id.titleEditText);

        date = getView().findViewById(R.id.date);
        time = getView().findViewById(R.id.time);
        description = getView().findViewById(R.id.description);
        name = getView().findViewById(R.id.name);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_items, container, false);
    }
}