package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.elemental.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SportTipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportTipsFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton imagebuttonOutside, imagebuttonHome;

    public SportTipsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportTipsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportTipsFragment newInstance(String param1, String param2) {
        SportTipsFragment fragment = new SportTipsFragment();
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
        return inflater.inflate(R.layout.fragment_sport_tips, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        imagebuttonOutside = (ImageButton) getView().findViewById(R.id.imagebuttonOutside);
        imagebuttonOutside.setOnClickListener(this);

        imagebuttonHome =  (ImageButton) getView().findViewById(R.id.imagebuttonHome);
        imagebuttonHome.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imagebuttonOutside:
                Toast.makeText(getActivity(), "outsideworkout listfragment", Toast.LENGTH_LONG).show();
                break;
            case R.id.imagebuttonHome:
                Navigation.findNavController(view).navigate(R.id.action_sportTipsFragment_to_homeWorkoutFragment);
                break;

        }


    }
}