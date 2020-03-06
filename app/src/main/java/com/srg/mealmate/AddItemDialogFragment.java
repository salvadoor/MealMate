package com.srg.mealmate;


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

import java.util.ArrayList;
import java.util.HashMap;


public class AddItemDialogFragment extends DialogFragment {
    private View view;
    private Spinner spinner;
    private ArrayList<GroceryItem> items;
    private HashMap<String, Double> itemMap = new HashMap<>();
    private HashMap<String, Double> itemHash;
    private static final String[] unitOptions = {"other", "oz", "tsp", "tbsp", "pinch", "lb", "cup", "loaf", "package"};


    public AddItemDialogFragment() {
        // Required empty public constructor
    }


    public static AddItemDialogFragment newInstance(HashMap hashMap,
                                                    ArrayList<GroceryItem> list,
                                                    HashMap itemHash){
        AddItemDialogFragment frag = new AddItemDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("hashMap", hashMap);
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
        itemMap = (HashMap<String, Double>) bundle.getSerializable("hashMap");
        items = (ArrayList<GroceryItem>) bundle.getSerializable("items");
        itemHash = (HashMap<String, Double>) bundle.getSerializable("hashmap");

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
                    double quantity = Double.parseDouble(itemQuantity.getText().toString());
                    String name = itemName.getText().toString();
                    // String units = itemUnits.getText().toString();
                    String units = spinner.getSelectedItem().toString();
                    if(units=="other"){
                        units = "";
                    }

                    if(itemMap.containsKey(name)){
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
                        itemHash.put(name, quantity);
                        items.add(new GroceryItem(quantity, units, name));
                        //((MainActivity) getActivity()).addItem(quantity, units, name);
                        dismiss();
                    }
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

}
