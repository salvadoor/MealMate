package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {
    private View view;
    private Recipe recipe;
    private TextView name_tv, source_tv, ingredient_tv;
    private ArrayList<HashMap> ingredients;
    private ArrayList<String> instructions;
   // private ArrayList<String> ingredientDetails = new ArrayList<>();

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        recipe = (Recipe) bundle.getSerializable("recipe");

        name_tv = view.findViewById(R.id.detail_title);
        name_tv.setText(recipe.getName());

        source_tv = view.findViewById(R.id.detail_source);
        source_tv.setText(("Source: " + recipe.getSource()));

        // ingredients
        String ingredient_string;
        ingredients = recipe.getIngredients();
        String[] ingredientDetails = new String[ingredients.size()];

        for(int i=0; i<ingredients.size(); i++){
            ingredient_string = getIngredientDetailString(recipe.getIngredients().get(i));
            ingredientDetails[i] = (ingredient_string);
        }

        // adapter for ListView containing ingredients
        ArrayAdapter ingredients_adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.ingredient_with_details,
                ingredientDetails);
        ListView lv = view.findViewById(R.id.detail_ingredients);
        lv.setAdapter(ingredients_adapter);

        // instructions
        instructions = recipe.getInstructions();
        String[] steps = new String[instructions.size()];
        for(int i=0; i<instructions.size();i++){
            steps[i] = "Step " + (i+1) + ":\n " + instructions.get(i);
        }

        ArrayAdapter instructions_adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.ingredient_with_details,
                steps);
        ListView lv2 = view.findViewById(R.id.detail_instructions);
        lv2.setAdapter(instructions_adapter);

        return view;
    }

    private String getIngredientDetailString(HashMap ingredient){
        String full_details;
        String amount, units, name;

        amount = ingredient.get("amount").toString();
        units = ingredient.get("units").toString();
        name = ingredient.get("name").toString();

        if(Double.valueOf(ingredient.get("amount").toString()) > 1){
            units = units + "s";
        }

        full_details = " * " + amount + " " + units + " " + name;

        return full_details;
    }

}
