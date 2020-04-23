package com.srg.mealmate.Services.Classes;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Recipe1 implements Serializable {
    private String name;
    private String source;
    private String id;
    //private ArrayList<HashMap> ingredients;
    private ArrayList<GroceryItem> ingredients;
    private String category;
    private ArrayList<String> instructions;
    private String imgURL;


    public Recipe1(){
        // empty Constructor
    }

    public Recipe1(String name, String source, String id, ArrayList<HashMap> ingredients,
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

    public Recipe1(QueryDocumentSnapshot recipe_doc){
        this.name = recipe_doc.getString("title");
        this.source = recipe_doc.getString("source");
        this.id = recipe_doc.getId();
        this.category = recipe_doc.getString("category");
        this.instructions = (ArrayList<String>) recipe_doc.get("instructions");
        this.imgURL = recipe_doc.getString("imageURL");

        adaptIngredients((ArrayList<HashMap>) recipe_doc.get("ingredients"));
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
