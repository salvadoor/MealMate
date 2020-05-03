/*
 * "LoginFragment.java"
 * Layout:  "fragment_login.xml"
 *
 * Fragment used to login an existing user or create a new user
 * upon login/register, call userSignedIn() from parent, MainActivity
 *  and redirect to Account Settings
 *
 */
package com.srg.mealmate.MainScreens;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
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
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private View view;
  //  private Button btn_login, btn_register;
    private EditText emailField, passwordField;
    private long lastClickTime = 0;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment and set view for fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        setListeners();

        return view;
    }


    private void setListeners(){
        // set onClickListeners for Login and Register Buttons
        Button btn_login, btn_register;

        // onClickListener for Register Button
        // create new user and authenticate them, redirect to account settings
        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // prevent multiple successive clicks and make sure both fields are filled
                if(!canAcceptClick() || fieldEmpty()){ return; }

                // if conditions met, run onClick code
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, SR(R.string.createUser_success));
                                    shortToast(SR(R.string.register_pass));
                                    ((MainActivity)getActivity()).userSignedIn();
                                } else{
                                    Log.w(TAG, SR(R.string.createUser_failure), task.getException());
                                    shortToast(SR(R.string.register_fail));
                                }
                            }
                        });
            }
        });

        // onClickListener for Login Button,
        // authenticate user and redirect to account settings upon success
        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // prevent multiple successive clicks and make sure both fields are filled
                if(!canAcceptClick() || fieldEmpty()){ return; }

                // if conditions met, run onClick code
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                // Attempt to sign in and authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, SR(R.string.signIn_success));
                                    shortToast(SR(R.string.login_pass));
                                    ((MainActivity)getActivity()).userSignedIn();
                                } else{
                                    Log.w(TAG, SR(R.string.signIn_failure));
                                    shortToast(SR(R.string.login_fail));
                                }
                            }
                        });
            }
        });

    }


    private boolean fieldEmpty(){
        // Check if email or password field is empty
        // return true if one or both fields is empty and create a Toast
        // return false if no fields are empty
        emailField = view.findViewById(R.id.edit_user);
        passwordField = view.findViewById(R.id.edit_pwd);

       if(isEmpty(emailField)){
           shortToast(SR(R.string.warning_email));
           return true;
       } else if(isEmpty(passwordField)){
           shortToast(SR(R.string.warning_pwd));
           return true;
       }
       return false;
    }


    private boolean isEmpty(EditText et){
        // check if EditText is empty or only contains whitespace
        // returns true if EditText is empty, false if not empty/whitespace
        return et.getText().toString().trim().length() == 0;
    }


    private void shortToast(String message){
        // make short toast to Parent Activity
        // message is string for toast
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    private boolean canAcceptClick(){
        // prevent quick double clicking from filling queue with button clicks
        if(SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return false;
        }
        // else record click time and accept click
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    private String SR(int id){
        // S.R. = String Resource
        // return string for resource with id resourceID
        // helps shorten and simplify code
        return getActivity().getResources().getString(id);
    }

}
