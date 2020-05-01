/*
 * "AddFolderDialog.java"
 * Layout:  "fragment_add_folder_dialog.xml"
 *
 * DialogFragment used to create a new folder
 *      for saved recipes
 * gets name of new folder from user
 *
 * Last Modified: 03.28.2020 01:05pm
 */
package com.srg.mealmate.Dialogs;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.srg.mealmate.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AddFolderDialog extends DialogFragment {
    private View view;
    private ArrayList<String> folders; // list of folder names


    public AddFolderDialog() {
        // Required empty public constructor
    }

    // create new instance and pass the list of folder names as a bundle
    public static AddFolderDialog newInstance(ArrayList<String> folders){
        AddFolderDialog frag = new AddFolderDialog();

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("folders", folders);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_folder_dialog, container, false);

        // get list of folders from bundle
        Bundle bundle = getArguments();
        folders = bundle.getStringArrayList("folders");

        initOnClickListeners();

        return view;
    }


    private void initOnClickListeners(){
        Button btn_add, btn_cancel;

        // add button to add/create the folder
        btn_add = view.findViewById(R.id.btn_folder_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText folderName = view.findViewById(R.id.edit_name);

                String name = folderName.getText().toString();


                if (name.equals("") || !name.matches("[a-zA-Z ]+")){
                    // if folder name uses non-letter characters
                        // notify user
                    Log.d(TAG, "No name entered or invalid");
                    Toast.makeText(getActivity(),
                            "Invalid name, use only letters",
                            Toast.LENGTH_SHORT)
                            .show();
                } else if (name.equals("MY RECIPES")) {
                    // if folder name matches preset folder name for user recipes
                        /// notify user
                    Log.d(TAG, "bad name");
                    Toast.makeText(getActivity(),
                            "Cannot use '" + name + "'",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // else, add folder
                    Log.d(TAG, "adding new folder");
                    folders.add(name);
                }
                dismiss();

            }
        });

        // cancel button
        btn_cancel = view.findViewById(R.id.btn_folder_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
