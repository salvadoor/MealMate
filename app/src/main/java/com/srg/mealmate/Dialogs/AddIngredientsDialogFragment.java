package com.srg.mealmate.Dialogs;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.GroceryItemAdapter;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.FileHelpers.GroceryListIO;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AddIngredientsDialogFragment extends DialogFragment {
    private View view;
    private ArrayList<String> folders;
    private ArrayList<GroceryItem> items = new ArrayList<>();
    private ArrayList<GroceryItem> ingredients;
    private String sunday; // sunday date for meal plan/grocery list
    private HashMap<String, GroceryItem> itemHash;


    public AddIngredientsDialogFragment() {
        // Required empty public constructor
    }

    public static AddIngredientsDialogFragment newInstance(ArrayList<GroceryItem> ingredients, String sunDate){
        AddIngredientsDialogFragment frag = new AddIngredientsDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("ingredients", ingredients);
        bundle.putString("file", sunDate);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_ingredients_dialog, container, false);

        Bundle bundle = getArguments();
        ingredients = (ArrayList<GroceryItem>) bundle.getSerializable("ingredients");
        sunday = bundle.getString("file");

        init_RecyclerView();
        init_OnClickListeners();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            //dialog.getWindow().requestFeature(Window.F)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(null);
            //setStyle(STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        }
    }


    private void init_RecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.ingredients);
        GroceryItemAdapter adapter = new GroceryItemAdapter(getActivity(), getActivity(), ingredients);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void init_OnClickListeners(){
        Button btn_add, btn_continue;

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroceryListIO.setFilename(sunday);
                items = GroceryListIO.readList(getActivity());
                init_hashMap();

                for(GroceryItem grocery : ingredients){
                    if(grocery.getChecked()==true){
                        grocery.setChecked(false); // set false so item in unchecked in grocery list
                        if(itemHash.containsKey(grocery.getName())){
                            if(itemHash.get(grocery.getName()).getUnits().equals(grocery.getUnits())){
                                // if item with same name and same units is already in list:
                                // update quantity
                                grocery.setQuantity(grocery.getQuantity() + itemHash.get(grocery.getName()).getQuantity());
                                items.remove(itemHash.get(grocery.getName()));
                            }
                        }
                        items.add(grocery);
                    }
                }

                GroceryListIO.writeList(items, getActivity());
                Toast.makeText(getActivity(), "Items added", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btn_continue = view.findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void init_hashMap(){
        itemHash = new HashMap<>();

        for(int i=0; i < items.size();i++){
            Log.d(TAG,"item count" + items.size());
            itemHash.put(items.get(i).getName(), items.get(i));
        }
    }

}
