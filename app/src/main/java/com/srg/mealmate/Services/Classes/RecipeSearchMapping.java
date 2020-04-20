package com.srg.mealmate.Services.Classes;

import java.io.Serializable;

public class RecipeSearchMapping implements Serializable {
    private String name;
    private String category;

    public RecipeSearchMapping(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String id) {
        this.category = id;
    }
}
