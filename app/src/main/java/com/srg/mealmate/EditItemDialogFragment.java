package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class EditItemDialogFragment extends DialogFragment {
    private View view;
    private TextView nameTV, unitsTV;
    private EditText quantityET;


    public EditItemDialogFragment() {
        // Required empty public constructor
    }

    public static EditItemDialogFragment newInstance(GroceryItem item){
        EditItemDialogFragment frag = new EditItemDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        Bundle bundle = getArguments();
        GroceryItem item = (GroceryItem) bundle.getSerializable("item");

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

        return view;
    }

}
