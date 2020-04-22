package com.srg.mealmate.Dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.FileHelpers.DoubleValueIO;

import java.util.ArrayList;


public class AddInstructionDialog extends DialogFragment {
    private static final String TAG = "AddInstruction";
    private ArrayList<String> instructions;
    private View view;

    public AddInstructionDialog() {
        // Required empty public constructor
    }

    public static AddInstructionDialog newInstance(ArrayList<String> instructions){
        AddInstructionDialog frag = new AddInstructionDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable("instructions", instructions);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_instruction_dialog, container, false);

        Bundle bundle = getArguments();
        instructions = (ArrayList<String>) bundle.getSerializable("instructions");

        initOnClickListeners();

        return view;
    }


    private void initOnClickListeners(){
        Button btn_add, btn_cancel;

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = view.findViewById(R.id.edit_instruction);

                String step = et.getText().toString();

                if(step.trim().length() == 0 || step.length() > 250){
                    Toast.makeText(getActivity(),
                            "instruction must be between 1-250 characters long",
                            Toast.LENGTH_SHORT).show();
                } else{
                    instructions.add(step);
                    dismiss();
                }

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
