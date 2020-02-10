/*
first version
being replaced by Recipe Class which includes all of Recipe data from firestore
 */

package com.srg.mealmate;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResult {
    private String name;
    private String source;
    private String id;
    private ArrayList<HashMap> ingredients;

    public SearchResult(String name, String source, String id, ArrayList<HashMap> ingredients) {
        this.name = name;
        this.source = source;
        this.id = id;
        this.ingredients = ingredients;
    }

    public ArrayList<HashMap> getIngredients() {
        return ingredients;
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
}
