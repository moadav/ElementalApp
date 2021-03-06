package com.example.elemental.Fragments;

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
import android.widget.Toast;

import com.example.elemental.activities.MainActivity;
import com.example.elemental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BMIKalkulatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BMIKalkulatorFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button button;
    private EditText editHeight;
    private EditText editWeight;
    private FirebaseFirestore db;
    private String myWeight,myHeight;

    public BMIKalkulatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BMIKalkulatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BMIKalkulatorFragment newInstance(String param1, String param2) {
        BMIKalkulatorFragment fragment = new BMIKalkulatorFragment();
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
        View view2 = inflater.inflate(R.layout.fragment_b_m_i_kalkulator, container, false);
        // Inflate the layout for this fragment



        return view2;
    }

    @Override
    public void onResume(){
        super.onResume();

        db = FirebaseFirestore.getInstance();
        button = getView().findViewById(R.id.button_id);
        editHeight = getView().findViewById(R.id.height_result);
        editWeight = getView().findViewById(R.id.weight_result);


        button.setOnClickListener(this);
    }

    private void updateValues(){
        if(editHeight.getText().toString().isEmpty()){
            editHeight.setError("Height cannot be empty!");
            editHeight.requestFocus();
            return;
        }else if (Integer.parseInt(editHeight.getText().toString()) <= 0){
            editHeight.setError("Height cannot be 0 or less!");
            editHeight.requestFocus();
            return;
        }
        if(editWeight.getText().toString().isEmpty()){
            editWeight.setError("Weight cannot be empty!");
            editWeight.requestFocus();
            return;
        }else if (Integer.parseInt(editWeight.getText().toString()) <= 0){
            editHeight.setError("Weight cannot be 0 or less!");
            editHeight.requestFocus();
            return;
        }

        myWeight = editWeight.getText().toString();
        myHeight = editHeight.getText().toString();
    }

    private void updateWeightAndHeight(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email", null))) {
                            db.collection("users").document(document.getId()).update("weight", myWeight, "height", myHeight).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getView()).navigate(R.id.action_BMIKalkulatorFragment_to_profileFragment);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to update!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("updatingDB","Failed to get database values");
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_id:
                updateValues();
                updateWeightAndHeight();
                break;
        }



    }
}