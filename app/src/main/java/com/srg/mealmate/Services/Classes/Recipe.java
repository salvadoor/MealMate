package com.srg.mealmate.Services.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Recipe implements Serializable {
    private String name;
    private String source;
    private String id;
    //private ArrayList<HashMap> ingredients;
    private ArrayList<GroceryItem> ingredients;
    private String category;
    private ArrayList<String> instructions;
    private String imgURL;


    public Recipe(String name, String source, String id, ArrayList<HashMap> ingredients,
                  String category, ArrayList<String> instructions, String imgURL) {
        this.name = name;
        this.source = source;
        this.id = id;
        //this.ingredients = ingredients;
        this.category = category;
        this.instructions = instructions;
        this.imgURL = imgURL;

        //this.ingredients = adaptIngredients(ingredients);
        adaptIngredients(ingredients);
    }


    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public ArrayList<GroceryItem> getIngredients() {
        return ingredients;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public String getImgURL() {
        return imgURL;
    }

    private void adaptIngredients(ArrayList<HashMap> iMap){
        this.ingredients = new ArrayList<>();

        for(HashMap item : iMap){

            String name = item.get("name").toString();
            Double amount = Double.parseDouble(item.get("amount").toString());
            String units = item.get("units").toString();

            this.ingredients.add(new GroceryItem(amount, units, name));
        }

    }
}
