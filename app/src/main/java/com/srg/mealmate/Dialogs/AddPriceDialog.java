package com.srg.mealmate.Dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.Grocery;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.FileHelpers.DoubleValueIO;


public class AddPriceDialog extends DialogFragment {
    private static final String TAG = "AddPriceDialog";
    private GroceryItem item;
    private View view;

    public AddPriceDialog() {
        // Required empty public constructor
    }

    public static AddPriceDialog newInstance(GroceryItem groceryItem){
        AddPriceDialog frag = new AddPriceDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable("groceryItem", groceryItem);

        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dialog_add_price, container, false);

        Bundle bundle = getArguments();
        item = (GroceryItem) bundle.getSerializable("groceryItem");

        // init_getGrocery();
        init_fields();
        init_OnClickListeners();

        return view;
    }


    private void init_getGrocery(){
        DoubleValueIO.setFilename(item.getName() + item.getUnits() + "__grocery");
        // grocery = DoubleValueIO.readDouble(getActivity());
    }


    private void setGroceryPrice(Double price){ // price per unit
        DoubleValueIO.setFilename(item.getName() + item.getUnits() + "__grocery");
        DoubleValueIO.writeDouble(price, getActivity());
    }


    private void init_fields(){
        TextView unitsTV = view.findViewById(R.id.item_units);
        unitsTV.setText(item.getUnits());

        EditText quantityET = view.findViewById(R.id.edit_quantity);
        quantityET.setHint(item.getQuantityString());
    }


    private void init_OnClickListeners(){
        // set OnClick for save and delete buttons
        Button btn_add, btn_cancel;

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double totalPrice, actualQuantity;
                EditText priceET, quantityET;

                priceET = view.findViewById(R.id.edit_cost);
                quantityET = view.findViewById(R.id.edit_quantity);

                try{
                    totalPrice = Double.parseDouble(priceET.getText().toString());
                    actualQuantity = Double.parseDouble(quantityET.getText().toString());

                    Double unitPrice = totalPrice / actualQuantity; //price per unit of measure
                    setGroceryPrice(unitPrice);
                    Double itemPrice = unitPrice * item.getQuantity();
                    itemPrice = Math.round(itemPrice * 100) / 100.0;
                    item.setPrice(itemPrice);
                    Toast.makeText(getActivity(), "New price entered", Toast.LENGTH_SHORT).show();
                    dismiss();
                } catch (Exception e){
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    // do a toast
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
