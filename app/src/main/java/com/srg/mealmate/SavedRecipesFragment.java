package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedRecipesFragment extends Fragment {
    private View view;


    public SavedRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        init_click_listeners();

        return view;
    }


    private void init_click_listeners(){
        final Button saved_btn, myRecipes_btn;

        saved_btn = view.findViewById(R.id.btn_saved);
        myRecipes_btn = view.findViewById(R.id.btn_myRecipes);

        saved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved_btn.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
                myRecipes_btn.setBackground(getResources().getDrawable(R.color.colorPrimary));

            }
        });

        myRecipes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecipes_btn.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
                saved_btn.setBackground(getResources().getDrawable(R.color.colorPrimary));

            }
        });
    }

}
