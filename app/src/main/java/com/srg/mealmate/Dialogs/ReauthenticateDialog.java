/*
 * "ReauthenticateDialog.java"
 * Layout:  "dialog_reauthenticate.xml"
 *
 * DialogFragment used to get users password again and re-authenticate them
 *
 */
package com.srg.mealmate.Dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;

public class ReauthenticateDialog extends DialogFragment {
    private static final String TAG = "ReauthenticateDialog";
    private FirebaseUser user;
    private EditText password_et;
    private View view;

    public ReauthenticateDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dialog_reauthenticate, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        password_et = view.findViewById(R.id.edit_pwd);
        set_onClickListeners();

        return view;
    }


    private void set_onClickListeners(){
        Button btn_delete, btn_cancel;

        btn_delete = view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_account();
            }
        });

        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void delete_account(){
        String email = user.getEmail();
        String password = password_et.getText().toString();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // re-authenticate user, only get password, use email from current authentication
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "User account has been deleted");
                                            ((MainActivity) getActivity()).userSignedOut();
                                            dismiss();
                                        }
                                    }
                                });
                    }
                });
    }
}
