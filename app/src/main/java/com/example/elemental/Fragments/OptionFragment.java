package com.example.elemental.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elemental.MainActivity;
import com.example.elemental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView resetyourpass,editusername,deleteAccount,editBMI;
    private SwitchCompat lightmode;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private ProgressBar progressbar;
    public OptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OptionFragment newInstance(String param1, String param2) {
        OptionFragment fragment = new OptionFragment();
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
        return inflater.inflate(R.layout.fragment_option, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
            sharedPreferences = getContext().getSharedPreferences("isNight", Context.MODE_PRIVATE);
            Boolean tet = sharedPreferences.getBoolean("night_mode",false);

        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        progressbar = getView().findViewById(R.id.progressbar);

         resetyourpass = getView().findViewById(R.id.resetyourpass);
         resetyourpass.setOnClickListener(this);

         editusername = getView().findViewById(R.id.editusername);
         editusername.setOnClickListener(this);

         deleteAccount = getView().findViewById(R.id.deleteAccount);
         deleteAccount.setOnClickListener(this);

            editBMI = getView().findViewById(R.id.editBMI);
            editBMI.setOnClickListener(this);

         lightmode = getView().findViewById(R.id.lightmode);
         lightmode.setOnCheckedChangeListener(this);



         if (tet)
             lightmode.setChecked(true);
         else
             lightmode.setChecked(false);

    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(!b) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("night_mode",false);
            editor.commit();

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("night_mode",true);
            editor.commit();
        }

    }

    private void resetPass(){
    progressbar.setVisibility(View.VISIBLE);
        fireStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email", null))) {
                            mAuth.sendPasswordResetEmail(document.getString("email")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Please check your email to reset your password", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                        Toast.makeText(getActivity(), "Failed to send email...", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("gettingDBvalues","Failed to get database values");
            }
        });

        progressbar.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resetyourpass:
                resetPass();
                break;
            case R.id.editBMI:
                Navigation.findNavController(getActivity(),  R.id.Nav_container).navigate(R.id.BMIKalkulatorFragment);
                break;

        }
    }
}