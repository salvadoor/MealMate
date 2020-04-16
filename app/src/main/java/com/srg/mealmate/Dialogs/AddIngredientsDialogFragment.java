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

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AddIngredientsDialogFragment extends DialogFragment {
    private View view;
    private ArrayList<String> folders;
    private ArrayList<GroceryItem> ingredients;


    public AddIngredientsDialogFragment() {
        // Required empty public constructor
    }

    public static AddIngredientsDialogFragment newInstance(ArrayList<GroceryItem> ingredients){
        AddIngredientsDialogFragment frag = new AddIngredientsDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("ingredients", ingredients);
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
                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();

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

}
