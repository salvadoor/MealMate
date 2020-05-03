/*
 * "SettingsFragment.java"
 * Layout:  "fragment_settings.xml"
 *
 * Fragment used to access user settings
 *
 */
package com.srg.mealmate.MainScreens;


import android.app.ActivityManager;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.RecipeSearchMapping;
import com.srg.mealmate.Services.FileHelpers.RecipeSearchMapIO;

import java.util.ArrayList;

import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class SettingsFragment extends Fragment {
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View view;
    private ArrayList<RecipeSearchMapping> searchMap = new ArrayList<>();

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
        Button btn_logout, btn_reset_pwd, btn_del_account, btn_del_data;

        // log out
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).userSignedOut();
            }
        });

        // sned reset password email
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

        // delete application data and close app
        btn_del_data = view.findViewById(R.id.btn_del_data);
        btn_del_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
                } catch (Exception e){
                    Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // delete user account
        btn_del_account = view.findViewById(R.id.btn_del_account);
        btn_del_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).userDelete();
            }
        });

    }

}
