package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {
    private View view;
    private Recipe recipe;
    private TextView name, source, ingredient_tv;

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

        name = view.findViewById(R.id.detail_title);
        name.setText("Recipe: " + recipe.getName());

        source = view.findViewById(R.id.detail_source);
        source.setText("Source: " + recipe.getSource());

        ingredient_tv = view.findViewById(R.id.detail_ingredients);
        ingredient_tv.setText(recipe.getIngredients().get(0).get("name").toString());

        return view;
    }

}
