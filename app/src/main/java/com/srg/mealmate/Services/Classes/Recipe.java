package com.srg.mealmate.Services.Classes;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Recipe implements Serializable {
    private String title;
    private String source;
    private String id;
    private ArrayList<HashMap> ingredients;
    private String category;
    private ArrayList<String> instructions;
    private String imageURL;


    public Recipe(){
        // empty Constructor
    }

    public Recipe(String title, String source, String id, ArrayList<HashMap> ingredients,
                  String category, ArrayList<String> instructions, String imageURL) {
        this.title = title;
        this.source = source;
        this.id = id;
        //this.ingredients = ingredients;
        this.category = category;
        this.instructions = instructions;
        this.imageURL = imageURL;
        this.ingredients = ingredients;

        //this.ingredients = adaptIngredients(ingredients);
        //adaptIngredients(ingredients);
    }

    public Recipe(QueryDocumentSnapshot recipe_doc){
        this.title = recipe_doc.getString("title");
        this.source = recipe_doc.getString("source");
        this.id = recipe_doc.getId();
        this.category = recipe_doc.getString("category");
        this.instructions = (ArrayList<String>) recipe_doc.get("instructions");
        this.imageURL = recipe_doc.getString("imageURL");
        this.ingredients = (ArrayList<HashMap>) recipe_doc.get("ingredients");

        //adaptIngredients((ArrayList<HashMap>) recipe_doc.get("ingredients"));
    }


    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public ArrayList<HashMap> getIngredients() {
        return ingredients;
    }

    public ArrayList<GroceryItem> toGroceryItems(){
        ArrayList<GroceryItem> items = new ArrayList<>();

        for(HashMap hash : this.ingredients){
            items.add(new GroceryItem(
                    Double.parseDouble(hash.get("amount").toString()),
                    hash.get("units").toString(),
                    hash.get("name").toString()));
        }

        return items;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public String getImageURL() {
        return imageURL;
    }

    private void adaptIngredients(ArrayList<HashMap> iMap){
        this.ingredients = new ArrayList<>();

        for(HashMap item : iMap){

            String name = item.get("name").toString();
            Double amount = Double.parseDouble(item.get("amount").toString());
            String units = item.get("units").toString();

            //this.ingredients.add(new GroceryItem(amount, units, name));
        }

    }
}
