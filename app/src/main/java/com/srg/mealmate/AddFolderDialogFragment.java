package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AddFolderDialogFragment extends DialogFragment {
    private View view;
    private ArrayList<String> folders;


    public AddFolderDialogFragment() {
        // Required empty public constructor
    }

    public static AddFolderDialogFragment newInstance(ArrayList<String> folders){
        AddFolderDialogFragment frag = new AddFolderDialogFragment();

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

        Bundle bundle = getArguments();
        folders = bundle.getStringArrayList("folders");

        initOnClickListeners();

        return view;
    }


    private void initOnClickListeners(){
        Button btn_add, btn_cancel;

        btn_add = view.findViewById(R.id.btn_folder_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText folderName = view.findViewById(R.id.edit_name);

                String name = folderName.getText().toString();

                if (name.equals("") || !name.matches("[a-zA-Z ]+")){
                    Log.d(TAG, "No name entered or invalid");
                    Toast.makeText(getActivity(),
                            "Invalid name, use only letters",
                            Toast.LENGTH_SHORT)
                            .show();
                } else if (name.equals("ALL") || name.equals("MY RECIPES")) {
                    Log.d(TAG, "bad name");
                    Toast.makeText(getActivity(),
                            "Cannot use '" + name + "'",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Log.d(TAG, "adding new folder");
                    folders.add(name);
                }
                dismiss();

            }
        });

        btn_cancel = view.findViewById(R.id.btn_folder_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
