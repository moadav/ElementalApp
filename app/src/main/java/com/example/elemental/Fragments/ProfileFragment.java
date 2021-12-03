package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.elemental.activities.MainActivity;
import com.example.elemental.R;
import com.example.elemental.Adapters.WorkoutAdapter;
import com.example.elemental.models.WorkoutPlan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ListView listView;
    private Button editBMI;
    public static WorkoutPlan singleworkout;
    public static int workoutposition;
    public static WorkoutAdapter workoutAdapter;
    private TextView useroverview;
    private FirebaseFirestore db;


    public ProfileFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        useroverview = getView().findViewById(R.id.useroverview);
        getUserOverview();
        editBMI = getView().findViewById(R.id.editBMI);
        editBMI.setOnClickListener(this);
        workoutAdapter = new WorkoutAdapter(getContext(),WorkoutPlan.workoutPlans);
        listView = getView().findViewById(R.id.workoutadapter);
        listAdapter();

    }

    private void getUserOverview(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot document : task.getResult()){
                    if(document.getString("email").equals(MainActivity.sharedPreferences.getString("email", null))){
                        useroverview.setText("Weight: " + document.getString("weight") +" kg"+ "\n\n Height: " + document.getString("height") +" cm");
                        return;

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void listAdapter(){

        listView.setAdapter(workoutAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                workoutposition = position;

                singleworkout = WorkoutPlan.workoutPlans.get(position);
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_workoutItemsFragment);
                workoutAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.editBMI:
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_BMIKalkulatorFragment);
                break;
        }
    }
}