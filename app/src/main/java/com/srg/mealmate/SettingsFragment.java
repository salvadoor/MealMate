package com.srg.mealmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private FirebaseUser user;
    private FirebaseAuth auth;
    private View view;
    private Button btn_reset_pwd, btn_logout;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView emailTV = view.findViewById(R.id.user_email);
        emailTV.setText(user.getEmail());

        setListeners();

        return view;
    }

    private void setListeners(){
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).userSignedOut();
            }
        });

        btn_reset_pwd = view.findViewById(R.id.btn_reset_pwd);
        btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getEmail();

                auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String message;
                                if(task.isSuccessful()){
                                    message = getResources().getString(R.string.notify_email);
                                    Log.d(TAG, message);
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}
