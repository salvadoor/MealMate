/*
 * "AddItemDialog.java"
 * Layout:  "fragment_add_item_dialog.xml"
 *
 * DialogFragment used to add a grocery item to list of grocery items
 *  used for both grocery list and for adding ingredients in New Recipe form
 *
 * Last Modified: 04.01.2020 06:45am
 */
package com.srg.mealmate.Dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.FileHelpers.DoubleValueIO;

import java.util.ArrayList;
import java.util.HashMap;


public class AddItemDialog extends DialogFragment {
    private View view;
    private Spinner spinner;
    private ArrayList<GroceryItem> items;
    private HashMap<String, Double> itemMap = new HashMap<>();
    private HashMap<String, Double> itemHash;
    private String[] unitOptions;


    public AddItemDialog() {
        // Required empty public constructor
    }


    public static AddItemDialog newInstance(/*HashMap hashMap, */
                                                    ArrayList<GroceryItem> list,
                                                    HashMap itemHash){
        // create new instance and pass list of grocery items and hashMap
        AddItemDialog frag = new AddItemDialog();

        Bundle bundle = new Bundle();
        // bundle.putSerializable("hashMap", hashMap);
        bundle.putSerializable("items", list);
        bundle.putSerializable("hashmap", itemHash);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_item_dialog, container, false);

        Bundle bundle = getArguments();
        items = (ArrayList<GroceryItem>) bundle.getSerializable("items");
        itemHash = (HashMap<String, Double>) bundle.getSerializable("hashmap");

        unitOptions = getContext().getResources().getStringArray(R.array.food_units);

        initSpinner();
        initOnClickListeners();

        return view;
    }


    private void initSpinner(){
        // set spinner, use unitOptions that gets data from string-array resource
        spinner = view.findViewById(R.id.spinner_units);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.unit_item, unitOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    private void initOnClickListeners(){
        Button btn_add, btn_cancel;

        // attempt to add the item
        btn_add = view.findViewById(R.id.btn_item_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemQuantity = view.findViewById(R.id.edit_quantity);
                EditText itemName = view.findViewById(R.id.edit_name);

                if(!fieldEmpty()){
                    double quantity = Double.parseDouble(itemQuantity.getText().toString());
                    String name = itemName.getText().toString();
                    String units = spinner.getSelectedItem().toString();

                    if(!isValidInput(name)){
                        Toast.makeText(getActivity(),
                                "Invalid name",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // see if there is a price saved for this item
                    DoubleValueIO.setFilename(name + units + "__grocery");
                    Double price = DoubleValueIO.readDouble(getActivity()) * quantity;
                    price = Math.round(price * 100) / 100.0;

                    if(units=="other"){
                        units = "";
                    }

                    if(itemHash.containsKey(name)){
                        // if item is already in grocery list
                        Toast.makeText(getActivity(),
                                "Already in List",
                                Toast.LENGTH_SHORT)
                                .show();
                        int index = 0;

                        for(int i=0; i<items.size();i++){
                            if (items.get(i).getName().equals(name)) {
                                //get index for item already in list
                                index = i;
                                break;
                            }
                        }
                        // dismiss AddItem and show Dialog to edit the item
                        dismiss();
                        ((MainActivity) getActivity()).showEditDialog(index, items, itemHash);
                    } else {
                        // add the item
                        itemHash.put(name, quantity);
                        items.add(new GroceryItem(quantity, units, name, false, price));
                        dismiss();
                    }
                } else{
                    // one of the fields is empty, cannot add item
                    Toast.makeText(getActivity(),
                            "Please fill all fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel = view.findViewById(R.id.btn_item_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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


    private boolean isValidInput(String string){
        // check for non alpha characters
        string = string.replaceAll("\\s+", "");
        char[] chars = string.toCharArray();

        for(char c : chars){
            if(!Character.isLetter(c)){
                return false;
            }
        }

        return true;
    }

}
