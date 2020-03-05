package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedRecipesFragment extends Fragment {
    private ArrayList<RecipeFolder> folders = new ArrayList<>();
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

        // TESTING-------------------------------------
        if(folders.isEmpty()){
            for(int i=0; i < 20; i++) {
                folders.add(new RecipeFolder("Desserts", "saved"));
            }
        }

        initRecyclerView();
        //-----------------------------------------------

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


    private void initRecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.folder_list);
        RecipeFolderAdapter adapter = new RecipeFolderAdapter(folders, getActivity());

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}
