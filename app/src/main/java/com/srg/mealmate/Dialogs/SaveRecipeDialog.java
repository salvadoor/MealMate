/*
 * "SaveRecipeDialog.java"
 * Layout:  "fragment_save_recipe_dialog.xml"
 *
 * DialogFragment used to save a recipe to a user folder
 *
 */
package com.srg.mealmate.Dialogs;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.srg.mealmate.R;
import com.srg.mealmate.Services.FileHelpers.ArrayListStringIO;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SaveRecipeDialog extends DialogFragment {
    private View view;
    private String recipeID;
    private Spinner spinner;
    private ArrayList<String> folder = new ArrayList<>();


    public SaveRecipeDialog() {
        // Required empty public constructor
    }

    public static SaveRecipeDialog newInstance(String id){
        SaveRecipeDialog frag = new SaveRecipeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("recipeID", id);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_save_recipe_dialog, container, false);

        Bundle bundle = getArguments();
        recipeID = bundle.getString("recipeID");

        initSpinner();
        initOnClickListeners();

        return view;
    }


    private void initSpinner(){
        // set spinner to show user's recipe folders
        spinner = view.findViewById(R.id.spinner_folders);

        ArrayListStringIO.setFilename("recipe_folders");
        ArrayList<String> folders = ArrayListStringIO.readList(getActivity());
        folders.remove("MY RECIPES"); // user cannot save recipes to ths folder

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.unit_item, folders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    private void initOnClickListeners(){
        Button btn_save, btn_cancel;

        // svae the recipe to the selected folder
        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String folderName = spinner.getSelectedItem().toString();

                ArrayListStringIO.setFilename(folderName + "_folder");
                ArrayList<String> recipes = ArrayListStringIO.readList(getActivity());
                recipes.add(recipeID);
                ArrayListStringIO.writeList(recipes, getActivity());


                // TESTING
                ArrayList<String> items = ArrayListStringIO.readList(getActivity());

                if(!items.isEmpty()){
                    Log.d(TAG, "recipe id's");
                    for(int i=0;i<items.size();i++){
                        Log.d(TAG, items.get(i));
                    }

                }
                // end testing

                dismiss();
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
