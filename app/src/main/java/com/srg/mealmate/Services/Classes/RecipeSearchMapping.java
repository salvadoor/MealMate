/*
 * "RecipeSearchMapping.java"
 *
 * contains name and category for a Recipe
 * used for searching recipes using substring and/or category
 *
 * Last Modified: 04.15.2020
 */
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
