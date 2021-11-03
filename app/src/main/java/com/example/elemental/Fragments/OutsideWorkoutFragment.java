package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.elemental.R;
import com.google.android.material.appbar.AppBarLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OutsideWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutsideWorkoutFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton calves,quads,abdomin,shoulder,traps,triceps,chest;
    public OutsideWorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OutsideWorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OutsideWorkoutFragment newInstance(String param1, String param2) {
        OutsideWorkoutFragment fragment = new OutsideWorkoutFragment();
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
        return inflater.inflate(R.layout.fragment_outside_workout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        quads = (ImageButton) getView().findViewById(R.id.quadpic);
        quads.setOnClickListener(this);

        calves = (ImageButton) getView().findViewById(R.id.calvespic);
        calves.setOnClickListener(this);

        shoulder = (ImageButton) getView().findViewById(R.id.shoulderpic);
        shoulder.setOnClickListener(this);

        traps = (ImageButton) getView().findViewById(R.id.trappic);
        traps.setOnClickListener(this);

        chest = (ImageButton) getView().findViewById(R.id.chestpic);
        chest.setOnClickListener(this);

        triceps = (ImageButton) getView().findViewById(R.id.triceppic);
        triceps.setOnClickListener(this);

        abdomin = (ImageButton) getView().findViewById(R.id.abdominpic);
        abdomin.setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calvespic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_calvesOutsideFragment);
                break;
            case R.id.quadpic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_quadsOutsideFragment);
                break;
            case R.id.shoulderpic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_shoulderOutsideFragment);
                break;
            case R.id.abdominpic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_abdominalOutsideFragment);
                break;
            case R.id.triceppic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_tricepsOutsideFragment);
                break;
            case R.id.chestpic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_chestOutsideFragment);
                break;
            case R.id.trappic:
                Navigation.findNavController(view).navigate(R.id.action_outsideWorkoutFragment_to_trapsOutsideFragment);
                break;
        }
    }
}