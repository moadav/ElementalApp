package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elemental.R;
import com.example.elemental.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BMIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BMIFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private float height,weight;
    float result;
    private TextView yourbmi,healthyresult,bmimessage;
    private FirebaseFirestore db;
    private DecimalFormat format = new DecimalFormat("0.#");

    public BMIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BMIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BMIFragment newInstance(String param1, String param2) {
        BMIFragment fragment = new BMIFragment();
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
        yourbmi = getView().findViewById(R.id.yourbmi);
        healthyresult = getView().findViewById(R.id.healthyresult);
        bmimessage = getView().findViewById(R.id.bmimessage);

        db = FirebaseFirestore.getInstance();
        fixValuesFromDb();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b_m_i, container, false);
    }


    private void displayIfUserHealhty(){
        if(result > 25) {
            healthyresult.setText("You are considered overweight!");
            healthyresult.setBackgroundResource(R.drawable.textviewshape);
            overweight();
        }else if (result < 18){
            healthyresult.setText("You are considered underweight!");
            healthyresult.setBackgroundResource(R.drawable.textviewshape);
            underweightMessage();
        }else{
            healthyresult.setText("You are considered normal!");
            healthyresult.setBackgroundResource(R.drawable.textviewshape);
            normalweight();
        }
    }

    private void underweightMessage(){
        bmimessage.setText("Having a bmi of less than 18 means that you are malnourished and it is recommended that you should increase " +
                "your calories. Some of the risk of being malnourished is:\n" +
                "\n *  easier to get infections" +
                "\n *  longer time to recover from illness" +
                "\n *  slow wound healing" +
                "\n *  higher risk of heart attack");
    }

    private void overweight(){
        bmimessage.setText("When your bmi is higher than 25, that means your health is at serious risk to develope serious health conditions, for example you are at a higher risk to develop: " +
                "\n" +
                "\n *  type 2 diabetes" +
                "\n *  high blood pressure" +
                "\n *  ashtma" +
                "\n *  reduced fertility");
    }

    private void normalweight(){
        bmimessage.setText("Healthy bmi range indicates that you prevent yourself of setting yourself to get serious health conditions, in contrast to being " +
                "underweight or overweight");
    }




    private void fixValuesFromDb(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email",null))) {
                            weight = Float.parseFloat(document.getString("weight"));
                            height = Float.parseFloat(document.getString("height")) / 100;
                            result = weight / (height * height);
                            yourbmi.setText("Your Bmi is: "+format.format(result));
                            displayIfUserHealhty();
                        }
                    }
                }
            }
        });
    }




}