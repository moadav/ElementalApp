package com.example.elemental.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
 * Use the {@link EditUsernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUsernameFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText resetusername;
    private Button resetbutton;
    private ImageView training;
    private ProgressBar progressBar;
    private FirebaseFirestore fireBase;

    public EditUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditUsernameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditUsernameFragment newInstance(String param1, String param2) {
        EditUsernameFragment fragment = new EditUsernameFragment();
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

        resetbutton = getView().findViewById(R.id.resetbutton);
        resetbutton.setOnClickListener(this);

        training = getView().findViewById(R.id.training);
        training.setOnClickListener(this);

        resetusername = getView().findViewById(R.id.resetusername);
        fireBase = FirebaseFirestore.getInstance();
        progressBar = getView().findViewById(R.id.progressbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_username, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resetbutton:
                resetUsername();
                break;
            case R.id.training:
                Navigation.findNavController(getView()).navigate(R.id.action_editUsernameFragment_to_optionFragment);
                break;
        }
    }




    private void resetUsername(){
        progressBar.setVisibility(View.VISIBLE);

        fireBase.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().containsValue(MainActivity.sharedPreferences.getString("email",null))) {
                            fireBase.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document2 : task.getResult()){
                                        if(resetusername.getText().toString().equals(document2.getString("username"))){
                                            Toast.makeText(getContext(), "Username is taken!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    if(resetusername.getText().toString().length() < 12) {
                                        fireBase.collection("users").document(document.getId()).update("username", resetusername.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Username updated!", Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(getView()).navigate(R.id.action_editUsernameFragment_to_optionFragment);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to update username", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else
                                        Toast.makeText(getContext(), "Username cannot be longer than 12 letters", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Failed to get database",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                }
            }
        });

        progressBar.setVisibility(View.GONE);
    }




}