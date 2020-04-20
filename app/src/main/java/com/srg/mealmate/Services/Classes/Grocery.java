package com.srg.mealmate.Services.Classes;

import java.io.Serializable;

public class Grocery implements Serializable {
    private String name;
    private String units;
    private double price = 0;

    public Grocery(String name, String units) {
        this.name = name;
        this.units = units;
    }

    public Grocery(String name, String units, double price) {
        this.name = name;
        this.units = units;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
