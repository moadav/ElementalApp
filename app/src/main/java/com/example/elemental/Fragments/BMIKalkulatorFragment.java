package com.example.elemental.Fragments;

import static android.content.ContentValues.TAG;
import static java.lang.Float.parseFloat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elemental.R;

import java.lang.ref.PhantomReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BMIKalkulatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BMIKalkulatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String resultBMI;
    private  Button button;
    private TextView editResult;
    private EditText editHeight;
    private EditText editWeight;
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

        button = getView().findViewById(R.id.button_id);
        editResult = getView().findViewById(R.id.Bmi_Result);
        editHeight = getView().findViewById(R.id.height_result);
        editWeight = getView().findViewById(R.id.weight_result);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                float weight = Float.valueOf(editWeight.getText().toString());

                float height =  Float.valueOf(editHeight.getText().toString()) / 100;

                float result = weight / (height * height);

                resultBMI = Float.toString(result);
                Toast.makeText(getContext(), resultBMI, Toast.LENGTH_SHORT).show();
                editResult.setText("Your BMI is: " + resultBMI);

            }
        });
    }
}