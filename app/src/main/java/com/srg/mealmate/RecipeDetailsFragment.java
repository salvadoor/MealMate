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
 * use StringBuilder instead of += for strings
 *
 * Last Modified: 02.12.2020 3:43pm
 */
package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

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

        // set text for TextViews based on recipe's data
        init_layout_text();
        // create PopupMenu
        init_popupMenu();
        // set OnClickListener(s)
        init_OnClickListener();

        return view;
    }


    private void init_layout_text(){
        // set text for all the TextViews based on data from recipe
        TextView nameTV, sourceTV, ingredientsTV, instructionsTV;

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
        String txt_ingredients = "";
        String ingredient_string;

        ingredients = recipe.getIngredients();

        for(int i=0; i<ingredients.size(); i++){
            ingredient_string = getIngredientDetailString(recipe.getIngredients().get(i));
            txt_ingredients += ingredient_string;

            if(i!=ingredients.size()-1){
                txt_ingredients += "\n";
            }
        }

        return txt_ingredients;
    }


    private String getStringOfInstructions(){
        // create string with each step labels and instructions separated by newline characters
        // return the string
        ArrayList<String> instructions = recipe.getInstructions();
        String txt_instructions = "";

        for(int i=0; i<instructions.size();i++){
            txt_instructions += "Step " + (i+1) + ":\n " + instructions.get(i);
            if(i!=instructions.size()-1){
                txt_instructions += "\n";
            }
        }

        return  txt_instructions;
    }


    private String getIngredientDetailString(HashMap ingredient){
        // format string that gives ingredient amount, units, and name
        // return the formatted string
        String full_details, amount, units, name;

        amount = ingredient.get("amount").toString();
        units = ingredient.get("units").toString();
        name = ingredient.get("name").toString();

        // add 's' to units if they should be plural
        if(Double.valueOf(ingredient.get("amount").toString()) > 1){
            units = units + "s";
        }

        full_details = " * " + amount + " " + units + " " + name;

        return full_details;
    }


    private void init_popupMenu(){
        // create new PopupMenu and bind to view for menu button
        popupMenu = new PopupMenu(getContext(), view.findViewById(R.id.btn_recipe_menu));
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_recipe, popupMenu.getMenu());
    }


    private void init_OnClickListener(){
        // set OnClickListeners
        // show popmenu when menu button is clicked
        Button btn_menu = view.findViewById(R.id.btn_recipe_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

    }

}
