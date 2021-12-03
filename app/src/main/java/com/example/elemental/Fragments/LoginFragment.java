package com.example.elemental.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elemental.R;
import com.example.elemental.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView register,forgotpass;
    private EditText emailadr,passord;
    private Button login;
    private FirebaseFirestore fireStore;
    private LoginActivity loginActivity = new LoginActivity();
    private FirebaseAuth mAuth;
    private ProgressBar progresscircle;
    private SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);

        register = (TextView) getView().findViewById(R.id.registeraccount);
        register.setOnClickListener(this);

        login = (Button) getView().findViewById(R.id.login);
        login.setOnClickListener(this);

        forgotpass = (TextView) getView().findViewById(R.id.forgotpassword) ;
        forgotpass.setOnClickListener(this);

        emailadr = (EditText) getView().findViewById(R.id.emailadr);
        passord = (EditText) getView().findViewById(R.id.passord);

        progresscircle = (ProgressBar) getView().findViewById(R.id.progresscircle);

        fireStore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        Login();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registeraccount:
                removeString();
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerAccountFragment);
                break;
            case R.id.login:
                Login();
                break;
            case R.id.forgotpassword:
                removeString();
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
                break;
        }
    }

    private void removeString(){
        emailadr.getText().clear();
        passord.getText().clear();
    }

    private void Login() {

        String emailRemember = sharedPreferences.getString("email", null);
        String passwordRemember = sharedPreferences.getString("password", null);


        if (emailRemember != null || passwordRemember != null) {
            removeString();
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainActivity);
        } else {


            String email = emailadr.getText().toString().trim();
            String password = passord.getText().toString().trim();

            if (email.isEmpty()) {
                emailadr.setError("Input is required");
                emailadr.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailadr.setError("Please provide a valid email");
                emailadr.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passord.setError("Input is required");
                passord.requestFocus();
                return;
            }


            progresscircle.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        fireStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if (user.isEmailVerified()) {
                                        Toast.makeText(getActivity(), "Logging on...", Toast.LENGTH_LONG).show();

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", emailadr.getText().toString());
                                        editor.putString("password", passord.getText().toString());
                                        editor.commit();
                                        removeString();

                                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainActivity);

                                    } else {
                                        user.sendEmailVerification();
                                        Toast.makeText(getActivity(), "Please verify your account! Verification has been sendt to your email", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Failed to login! Please provide correct input", Toast.LENGTH_LONG).show();
                                }
                                progresscircle.setVisibility(View.GONE);
                            }

                        });

                    } else {
                        Toast.makeText(getActivity(), "User does not exist!", Toast.LENGTH_LONG).show();
                        progresscircle.setVisibility(View.GONE);
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Log.e("LoginActivity", "Failed Registration", e);

                    }

                }
            });


        }
    }
}