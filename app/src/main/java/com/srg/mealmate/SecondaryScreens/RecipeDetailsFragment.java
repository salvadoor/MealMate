/*
 * "RecipeDetailsFragment.java"
 * Layout:  "fragment_recipe_details.xml"
 *
 * Fragment used to display details for a recipe
 * Recipe is retrieved from bundle
 * formats recipe information and sets it as text for TextViews
 * use buttons tht allow various actions on a recipe:
 *  save recipe
 *  view nutrition detail for recipe
 *  add recipe to meal plan
 *
 * TO DO:
 *  setup buttons and listeners
 *
 * Last Modified: 02.13.2020 11:42pm
 */
package com.srg.mealmate.SecondaryScreens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.Classes.Recipe;
// import com.srg.mealmate.Services.Classes.Recipe1;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class RecipeDetailsFragment extends Fragment {
    private View view;
    private Recipe recipe; // Recipe1 recipe;
    private ArrayList<HashMap> ingredients;
    private PopupMenu popupMenu;


    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get serialized Recipe object passed from bundle
        Bundle bundle = getArguments();
        recipe = (Recipe) bundle.getSerializable("recipe");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // set TextViews and ImageView based on recipe's data
        init_layout();

        // set OnClickListener(s)
        initOnClickListeners();


        return view;
    }


    private void init_layout(){
        // set text for all the TextViews based on data from recipe
        TextView nameTV, sourceTV, ingredientsTV, instructionsTV;
        ImageView imageView;

        imageView = view.findViewById(R.id.detail_image);
        Picasso.get().load(recipe.getImageURL()).into(imageView);

        // set Recipe name and source text
        nameTV = view.findViewById(R.id.detail_title);
        nameTV.setText(recipe.getTitle());

        sourceTV = view.findViewById(R.id.detail_source);
        sourceTV.setText(("Source: " + recipe.getSource()));

        // Set text for ingredients
        ingredientsTV = view.findViewById(R.id.detail_ingredients);
        ingredientsTV.setText(getStringOfIngredients());

        // set text for instructions
        instructionsTV = view.findViewById(R.id.detail_instructions);
        instructionsTV.setText(getStringOfInstructions());
    }

    private void initOnClickListeners(){
        Button btn_save_recipe, btn_add_meal;

        btn_save_recipe = view.findViewById(R.id.btn_save_recipe);
        btn_save_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call MainActivity method to show dialogfragment
                ((MainActivity) getActivity()).saveRecipe(recipe.getId());
            }
        });

        btn_add_meal = view.findViewById(R.id.btn_add_to_plan);
        btn_add_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showAddMealDialog(recipe);
            }
        });

    }


    private String getStringOfIngredients(){
        // string with each formatted ingredient string separated by newline character
        // return the string
        StringBuilder str = new StringBuilder();
        String ingredient_string;

        ArrayList<GroceryItem> ingredients = recipe.getGroceryItems();

        for(GroceryItem item : ingredients){
            str.append(item.getGroceryDetailString());
            str.append("\n");
        }
        /*
        ingredients = recipe.getIngredients();

        for(int i=0; i<ingredients.size(); i++){
            ingredient_string = getIngredientDetailString(recipe.getIngredients().get(i));
            str.append(ingredient_string);

            if(i!=ingredients.size()-1){
                str.append("\n");
            }
        }
        */
        return str.toString();
    }


    private String getStringOfInstructions(){
        // create string with each step labels and instructions separated by newline characters
        // return the string
        ArrayList<String> instructions = recipe.getInstructions();
        StringBuilder str = new StringBuilder();

        for(int i=0; i<instructions.size();i++){
            str.append("Step ")
                    .append(i+1)
                    .append("\n")
                    .append(instructions.get(i))
                    .append("\n");
        }

        return  str.toString();
    }


    private String getIngredientDetailString(HashMap ingredient){
        // format string that gives ingredient amount, units, and name
        // return the formatted string
        StringBuilder full_details = new StringBuilder();
        String amount, units, name;

        amount = ingredient.get("amount").toString();
        Log.d(TAG, amount);
        units = ingredient.get("units").toString();
        Log.d(TAG, units);
        name = ingredient.get("name").toString();
        Log.d(TAG, name);

        // add 's' to units if they should be plural
        if(Double.valueOf(ingredient.get("amount").toString()) > 1){
            units = units + "s";
        }

        full_details.append(" * ")
                .append(amount)
                .append(" ")
                .append(units)
                .append(" - ")
                .append(name);

        return full_details.toString();
    }

}
