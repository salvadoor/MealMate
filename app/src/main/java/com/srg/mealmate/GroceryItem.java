/*
 * "GroceryItem.java"
 *
 * Data Structure for items in grocery list
 *
 * Last Modified: 02.12.2020 08:01pm
 */
package com.srg.mealmate;

import java.io.Serializable;

public class GroceryItem implements Serializable {
    private Boolean isChecked;
    private double quantity;
    private String units;
    private String item;
/*
    public static final GroceryItem[] items = {
        new GroceryItem(2, "Avacados"),
        new GroceryItem(1, "Apple")
    };
*/
    public GroceryItem(double quantity, String units, String item){
        this.quantity = quantity;
        this.units = units;
        this.item = item;
        this.isChecked = false;
    }

    public GroceryItem(double quantity, String units, String item, Boolean checked){
        this.quantity = quantity;
        this.units = units;
        this.item = item;
        this.isChecked = checked;
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

    public String getItem() {
        return item;
    }

    public void setChecked(Boolean tf) {
        isChecked = tf;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnits(String units){
        this.units = units;
    }


    public String getGroceryDetailString(){
        StringBuilder full_details = new StringBuilder();
        String units;

        if(this.quantity > 1 && this.units.length()!=0 && this.units!="whole"){
            units = this.units + "s";
        } else{
            units = this.units;
        }

        full_details.append(this.quantity)
                .append(units)
                .append(" x ")
                .append(this.item);

        return full_details.toString();
    }

}
