package com.srg.mealmate.Dialogs;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.srg.mealmate.R;
import com.srg.mealmate.Services.FileHelpers.ArrayListStringIO;

import java.util.ArrayList;


public class EditFolderDialogFragment extends DialogFragment {
    private View view;
    private TextView nameTV;
    private String folderName;
    private int itemIndex;
    private ArrayList<String> folders;


    public EditFolderDialogFragment() {
        // Required empty public constructor
    }


    public static EditFolderDialogFragment newInstance(int itemIndex, ArrayList<String> folders){
        EditFolderDialogFragment frag = new EditFolderDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("itemIndex", itemIndex);
        bundle.putSerializable("folders", folders);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_folder_dialog, container, false);

        Bundle bundle = getArguments();
        itemIndex = bundle.getInt("itemIndex");
        folders = (ArrayList<String>) bundle.getSerializable("folders");

        folderName = folders.get(itemIndex);

        init_fields();
        init_OnClickListeners();

        return view;
    }


    private void init_fields(){
        StringBuilder header = new StringBuilder();

        header.append(getActivity().getString(R.string.edit_label))
                .append(" \"")
                .append(folderName)
                .append("\"");

        nameTV = view.findViewById(R.id.folder_name);
        nameTV.setText(header);
    }


    private void init_OnClickListeners(){
        // set OnClick for save and delete buttons
        Button btn_delete, btn_cancel;

        btn_delete = view.findViewById(R.id.btn_delete_folder);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Folder deleted",
                        Toast.LENGTH_SHORT);

                //ArrayListStringIO.setFilename(folderName+"_folder");
                ArrayListStringIO.delete(folderName+"_folder.dat", getActivity());
                folders.remove(itemIndex);
                dismiss();
            }
        });

        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}

