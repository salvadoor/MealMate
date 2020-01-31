package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddGroceryItemFragment extends Fragment {
    private View view;

    public AddGroceryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_grocery_item, container, false);

        initOnClickListeners();

        return view;
    }

    private void initOnClickListeners(){
        Button btn_add, btn_cancel;

        btn_add = view.findViewById(R.id.btn_item_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemName = view.findViewById(R.id.edit_name);
                EditText itemQuantity = view.findViewById(R.id.edit_quantity);

                if(!fieldEmpty()){
                    String name = itemName.getText().toString();
                    int quantity = Integer.parseInt(itemQuantity.getText().toString());

                    ((MainActivity)getActivity()).addItem(name, quantity);
                }
            }
        });

        btn_cancel = view.findViewById(R.id.btn_item_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).cancelItem();
            }
        });
    }

    private boolean fieldEmpty(){
        // return true if one or both fields is empty and create a Toast
        // return false if no fields are empty
        EditText itemName = view.findViewById(R.id.edit_name);
        EditText itemQuantity = view.findViewById(R.id.edit_quantity);

        if(isEmpty(itemName)){
            return true;
        } else if(isEmpty(itemQuantity)){
            return true;
        }
        return false;
    }

    private boolean isEmpty(EditText et){
        // check if EditText is empty or only contains whitespace
        // returns true if EditText is empty, false if not empty/whitespace
        return et.getText().toString().trim().length() == 0;
    }

}
