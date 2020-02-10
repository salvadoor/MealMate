package com.srg.mealmate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Recipe implements Serializable {
    private String name;
    private String source;
    private String id;
    private ArrayList<HashMap> ingredients;
    private String category;
    private ArrayList<String> instructions;


    public Recipe(String name, String source, String id, ArrayList<HashMap> ingredients
                    , String category, ArrayList<String> instructions) {
        this.name = name;
        this.source = source;
        this.id = id;
        this.ingredients = ingredients;
        this.category = category;
        this.instructions = instructions;
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

    public ArrayList<HashMap> getIngredients() {
        return ingredients;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }
}
