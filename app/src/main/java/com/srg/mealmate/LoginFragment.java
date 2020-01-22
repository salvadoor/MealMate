package com.srg.mealmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private View view;
    private Button btn_register;
    private Button btn_login;
    private EditText email, password;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        // Inflate the layout for this fragment
        return view;
    }

    private void init(){
        email = view.findViewById(R.id.edit_user);
        password = view.findViewById(R.id.edit_pwd);

        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(emailText.isEmpty()){
                    email.setError("No Email entered");
                    email.requestFocus();
                }
                if(passwordText.isEmpty()){
                    password.setError("No Password entered");
                    password.requestFocus();
                }

                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    ((MainActivity)getActivity()).userSignedIn();
                                   // userSignedIn();
                                   // updateUI(user);
                                } else{
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }
        });

        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(emailText.isEmpty()){
                    email.setError("No Email entered");
                    email.requestFocus();
                }
                if(passwordText.isEmpty()){
                    password.setError("No Password entered");
                    password.requestFocus();
                }
                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    userSignedIn();
                                } else{
                                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    public void userSignedIn(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SettingsFragment fragment2 = new SettingsFragment();
        ft.addToBackStack(null);
        ft.hide(LoginFragment.this);
        ft.add(android.R.id.content, fragment2);
        ft.commit();
    }

}
