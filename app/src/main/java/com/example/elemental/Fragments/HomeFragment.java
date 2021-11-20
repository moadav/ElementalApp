package com.example.elemental.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.elemental.MainActivity;
import com.example.elemental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView welcometext,yourbmi,healthyresult,calorie;
    private FirebaseFirestore db;
    private Button getCalorie;
    private float height,weight;
    float result;
    private Spinner myspinner;
    private DecimalFormat format = new DecimalFormat("0.#");


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();
        welcometext = getView().findViewById(R.id.welcometext);
        yourbmi = getView().findViewById(R.id.yourbmi);
        healthyresult = getView().findViewById(R.id.healthyresult);
        myspinner = getView().findViewById(R.id.myspinner);
        calorie = getView().findViewById(R.id.calorie);
        getCalorie = getView().findViewById(R.id.getCalorie);
        getCalorie.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.activitylevel));
        myspinner.setAdapter(adapter);
        fixValuesFromDb();
    }


    private double getGainWeightCalorie(double calorie){
        return calorie+500;
    }

    private double getLoseWeightCalorie(double calorie){
        return calorie-500;
    }
    private double currentCalorisBodyWeight(int activty){
        Double newweight = weight * 2.2;
        return newweight * activty;
    }

    private void caloriePlan(){
        if(myspinner.getSelectedItemId() == 0) {
            calorie.setText(String.format("If you want to keep your weight: " + format.format(currentCalorisBodyWeight(13))
                    + "\nIf you want to lose weight: " + format.format(getLoseWeightCalorie(currentCalorisBodyWeight(13)))
                    + "\nIf you want to gain weight: " + format.format(getGainWeightCalorie(currentCalorisBodyWeight(13)))));
        }
        else if(myspinner.getSelectedItemId() == 1) {
            calorie.setText("If you want to keep your weight: "+format.format(currentCalorisBodyWeight(15))
                    + "\nIf you want to lose weight: " + format.format(getLoseWeightCalorie(currentCalorisBodyWeight(15)))
                    + "\nIf you want to gain weight: " + format.format(getGainWeightCalorie(currentCalorisBodyWeight(15))));
        }
        else{
            calorie.setText("If you want to keep your weight: "+format.format(currentCalorisBodyWeight(18))
                    + "\nIf you want to lose weight: " + format.format(getLoseWeightCalorie(currentCalorisBodyWeight(18)))
                    + "\nIf you want to gain weight: " + format.format(getGainWeightCalorie(currentCalorisBodyWeight(18))));
        }
    }

    private void displayIfUserHealhty(){
        if(result > 25) {
            healthyresult.setText("You are considered overweight!");
            healthyresult.setBackgroundColor(Color.RED);
        }else if (result < 18){
            healthyresult.setText("You are considered underweight!");
            healthyresult.setBackgroundColor(Color.BLUE);
        }else{
            healthyresult.setText("You are considered normal!");
            healthyresult.setBackgroundColor(Color.GREEN);
        }
    }




    private void fixValuesFromDb(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email",null))) {
                            welcometext.setText("Welcome " +document.getString("username"));
                            weight = Float.parseFloat(document.getString("weight"));
                            height = Float.parseFloat(document.getString("height")) / 100;
                            result = weight / (height * height);
                            yourbmi.setText("Your Bmi is: "+result);
                            displayIfUserHealhty();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getCalorie:
                caloriePlan();
                break;

        }
    }
}