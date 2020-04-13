/*
 * "RecipeSimple.java"
 *
 * Simplified version of Recipe class
 * only stores recipe name, id, and image url
 * used in leu of Recipe for Meal Plan
 *
 * Last Modified: 03.11.2020 08:25pm
 */
package com.srg.mealmate.Services.Classes;

import java.io.Serializable;

public class RecipeSimple implements Serializable {
    private String name;
    private String id;
    private String imgURL;


    public RecipeSimple(String name, String id, String imgURL) {
        this.name = name;
        this.id = id;
        this.imgURL = imgURL;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImgURL() {
        return imgURL;
    }
}
