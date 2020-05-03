/*
 * "GroceryItem.java"
 *
 * Data Structure for items in grocery list
 *
 * Last Modified: 04.22.2020 06:15pm
 */
package com.srg.mealmate.Services.Classes;

import java.io.Serializable;
import java.util.HashMap;

public class GroceryItem implements Serializable {
    private Boolean isChecked;
    private double quantity;
    private String units;
    private String name;
    private double price = 0; // price per unit


    public GroceryItem(double quantity, String units, String name){
        this.quantity = quantity;
        this.units = units;
        this.name = name;
        this.isChecked = true;
    }

    public GroceryItem(double quantity, String units, String name, Boolean checked){
        // Constructor with Boolean passed
        this.quantity = quantity;
        this.units = units;
        this.name = name;
        this.isChecked = checked;
    }

    public GroceryItem(double quantity, String units, String name, Boolean isChecked, double price) {
        // Constructor with Boolean and Double passed
        this.isChecked = isChecked;
        this.quantity = quantity;
        this.units = units;
        this.name = name;
        this.price = price;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getQuantityString(){
        return Double.toString(quantity);
    }

    public String getUnits(){ return units;}

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setChecked(Boolean tf) {
        isChecked = tf;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnits(String units){
        this.units = units;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public HashMap getHash(){
        // get HashMap for the GroceryItem
        // Used for easier usage with firestore
        HashMap ingredient = new HashMap();

        ingredient.put("name", this.name);
        ingredient.put("amount", this.quantity);
        ingredient.put("units", this.units);

        return ingredient;
    }


    public String getGroceryDetailString(){
        // return a string of the GroceryItem's details
        StringBuilder full_details = new StringBuilder();

        if(this.quantity%1!=0){
            full_details.append(this.quantity);
        } else {
            full_details.append((int)this.quantity);
        }

        if(this.units!="") {
            full_details.append("(")
                    .append(this.units)
                    .append(")");
        }
        full_details.append("  -  ")
                .append(this.name);

        if(price>0){ // onyl show price if nonzero value
            full_details.append("\n Price: $")
                    .append(price);
        }

        return full_details.toString();
    }

}
