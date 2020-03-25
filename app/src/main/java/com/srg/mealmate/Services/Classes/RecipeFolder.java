package com.srg.mealmate.Services.Classes;

import java.io.Serializable;

public class RecipeFolder implements Serializable {
    private String name;
    private String type;


    public RecipeFolder(String name, String type) {
        this.name = name;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
