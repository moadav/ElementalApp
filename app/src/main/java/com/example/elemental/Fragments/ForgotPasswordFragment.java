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
import android.widget.Toast;

import com.example.elemental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private EditText forgotemail;
    private ProgressBar progresscircle;
    private Button forgotpasswordbutton;
    private ImageView imageView3;
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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

        imageView3 = (ImageView) getView().findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);

        forgotpasswordbutton = (Button) getView().findViewById(R.id.forgotpasswordbutton);
        forgotpasswordbutton.setOnClickListener(this);

        forgotemail = (EditText) getView().findViewById(R.id.forgotemail);

        progresscircle = (ProgressBar) getView().findViewById(R.id.progresscircle);

        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView3:
                Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                break;
            case R.id.forgotpasswordbutton:
                resetPassword();
                break;

        }
    }

    private void resetPassword() {

        String emailText = forgotemail.getText().toString().trim();

        if(emailText.isEmpty()){
            forgotemail.setError("Input is required");
            forgotemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            forgotemail.setError("Please provide a valid email");
            forgotemail.requestFocus();
            return;
        }

        progresscircle.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Please check your email to reset your password", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "There is no user registered with this email", Toast.LENGTH_LONG).show();

                progresscircle.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }
}