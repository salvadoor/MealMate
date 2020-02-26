package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class EditItemDialogFragment extends DialogFragment {
    private View view;
    private TextView nameTV, unitsTV;
    private EditText quantityET;
    private GroceryItem item;
    private int itemIndex;
    private ArrayList<GroceryItem> items;
    private HashMap<String, Double> itemHash;


    public EditItemDialogFragment() {
        // Required empty public constructor
    }

    public static EditItemDialogFragment newInstance(int itemIndex,
                                                     ArrayList<GroceryItem> items,
                                                     HashMap itemHash){
        EditItemDialogFragment frag = new EditItemDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("itemIndex", itemIndex);
        bundle.putSerializable("items", items);
        bundle.putSerializable("hashmap", itemHash);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_item_dialog, container, false);

        Bundle bundle = getArguments();
        itemIndex = bundle.getInt("itemIndex");
        items = (ArrayList<GroceryItem>) bundle.getSerializable("items");
        itemHash = (HashMap<String, Double>) bundle.getSerializable("hashmap");

        item = items.get(itemIndex);

        init_fields();
        init_OnClickListeners();

        return view;
    }


    private void init_fields(){
        String itemUnits = item.getUnits();

        StringBuilder itemName = new StringBuilder();

        itemName.append(getActivity().getString(R.string.edit_label))
                .append(" \"")
                .append(item.getName())
                .append("\"");

        nameTV = view.findViewById(R.id.item_name);
        nameTV.setText(itemName);

        unitsTV = view.findViewById(R.id.item_units);
        unitsTV.setText(itemUnits);

        quantityET = view.findViewById(R.id.edit_quantity);
        quantityET.setHint(item.getQuantityString());
    }


    private void init_OnClickListeners(){
        // set OnClick for save and delete buttons
        Button btn_save, btn_delete;

        btn_save = view.findViewById(R.id.btn_save_changes);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double newQuantity;
                EditText quantityET = view.findViewById(R.id.edit_quantity);

                try{
                    newQuantity = Double.parseDouble(quantityET.getText().toString());
                    item.setQuantity(newQuantity);
                    Toast.makeText(getActivity(), "Item edited", Toast.LENGTH_SHORT).show();
                    dismiss();
                } catch (Exception e){
                    Toast.makeText(getActivity(), "Error: Changes not saved", Toast.LENGTH_SHORT).show();
                    // do a toast
                }
            }
        });

        btn_delete = view.findViewById(R.id.btn_delete_item);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(itemIndex);
                itemHash.remove(item.getName());

                Toast.makeText(getActivity(), "Item removed", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

}
