package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elemental.R;
import com.example.elemental.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterAccountFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private EditText username, password, email, height,weight;
    private ProgressBar progresscircle;
    private Button registerbutton;
    private ImageView imageView2;

    private FirebaseAuth mAuth;


    public RegisterAccountFragment() {
        // Required empty public constructor
    }

    public static RegisterAccountFragment newInstance(String param1, String param2) {
        RegisterAccountFragment fragment = new RegisterAccountFragment();
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

        return inflater.inflate(R.layout.fragment_register_account, container, false);

    }


    @Override
    public void onResume() {
        super.onResume();

        imageView2 = (ImageView) getView().findViewById(R.id.imageView2);
        imageView2.setOnClickListener(this);

        registerbutton = (Button) getView().findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(this);

        username = (EditText) getView().findViewById(R.id.username);

        password = (EditText) getView().findViewById(R.id.password);

        email = (EditText) getView().findViewById(R.id.email);

        height = (EditText) getView().findViewById(R.id.height);

        weight = (EditText) getView().findViewById(R.id.weight);

        progresscircle = (ProgressBar) getView().findViewById(R.id.progresscircle);

        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView2:
                Navigation.findNavController(view).navigate(R.id.action_registerAccountFragment_to_loginFragment);
                break;
            case R.id.registerbutton:
                registerUser();
                break;

        }
    }

    private void registerUser() {

        String emailText = email.getText().toString().trim();
        String usernameText = username.getText().toString().trim();
        String weightText = weight.getText().toString().trim();
        String heightText = height.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if(emailText.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(usernameText.isEmpty()){
            username.setError("Username is required");
            username.requestFocus();
            return;
        }
        if(weightText.isEmpty()){
            weight.setError("Weight is required");
            weight.requestFocus();
            return;
        }
        if(heightText.isEmpty()){
            height.setError("Height is required");
            height.requestFocus();
            return;
        }
        if(passwordText.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }

        if(passwordText.length() < 6){
            password.setError("Password should have atleast 6 characters!");
            password.requestFocus();
            return;
        }

        progresscircle.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailText,passwordText)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    User user = new User(weightText,heightText,usernameText,emailText);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "User has been registered!", Toast.LENGTH_LONG).show();
                                progresscircle.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(getActivity(), "Failed to register! Please try again", Toast.LENGTH_LONG).show();
                                progresscircle.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

    }
}