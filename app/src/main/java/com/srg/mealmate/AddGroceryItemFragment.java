package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class AddGroceryItemFragment extends Fragment {
    private View view;
    private Spinner spinner;
    private static String[] unitOptions = {"whole/other", "ounce", "teaspoon", "tablespoon", "pinch"};


    public AddGroceryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_grocery_item, container, false);

        initSpinner();
        initOnClickListeners();

        return view;
    }


    private void initSpinner(){
        spinner = view.findViewById(R.id.spinner_units);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.unit_item,unitOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    private void initOnClickListeners(){
        Button btn_add, btn_cancel;

        btn_add = view.findViewById(R.id.btn_item_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemQuantity = view.findViewById(R.id.edit_quantity);
                EditText itemName = view.findViewById(R.id.edit_name);

                if(!fieldEmpty()){
                    int quantity = Integer.parseInt(itemQuantity.getText().toString());
                    String name = itemName.getText().toString();
                   // String units = itemUnits.getText().toString();
                    String units = spinner.getSelectedItem().toString();
                    if(units=="whole/other"){
                        units = "";
                    }

                    ((MainActivity)getActivity()).addItem(quantity, units, name);
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


        if(isEmpty(itemName) || isEmpty(itemQuantity) || spinner.getSelectedItem()==null){
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
