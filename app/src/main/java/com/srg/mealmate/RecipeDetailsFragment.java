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
package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class RecipeDetailsFragment extends Fragment {
    private View view;
    private Recipe recipe;
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
        /*
        // create PopupMenu
        init_popupMenu();
        // set OnClickListener(s)
        init_OnClickListener();

         */

        return view;
    }


    private void init_layout(){
        // set text for all the TextViews based on data from recipe
        TextView nameTV, sourceTV, ingredientsTV, instructionsTV;
        ImageView imageView;

        imageView = view.findViewById(R.id.detail_image);
        Picasso.get().load(recipe.getImgURL()).into(imageView);

        // set Recipe name and source text
        nameTV = view.findViewById(R.id.detail_title);
        nameTV.setText(recipe.getName());

        sourceTV = view.findViewById(R.id.detail_source);
        sourceTV.setText(("Source: " + recipe.getSource()));

        // Set text for ingredients
        ingredientsTV = view.findViewById(R.id.detail_ingredients);
        ingredientsTV.setText(getStringOfIngredients());

        // set text for instructions
        instructionsTV = view.findViewById(R.id.detail_instructions);
        instructionsTV.setText(getStringOfInstructions());
    }


    private String getStringOfIngredients(){
        // string with each formatted ingredient string separated by newline character
        // return the string
        StringBuilder str = new StringBuilder();
        String ingredient_string;

        ingredients = recipe.getIngredients();

        for(int i=0; i<ingredients.size(); i++){
            ingredient_string = getIngredientDetailString(recipe.getIngredients().get(i));
            str.append(ingredient_string);

            if(i!=ingredients.size()-1){
                str.append("\n");
            }
        }

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
        units = ingredient.get("units").toString();
        name = ingredient.get("name").toString();

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
