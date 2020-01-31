/*
 * "GroceryItem.java"
 *
 * Data Structure for items in grocery list
 *
 * Last Modified: 01.30.2020 06:37pm
 */
package com.srg.mealmate;

public class GroceryItem {
    private Boolean isChecked;
    private int quantity;
    private String item;
/*
    public static final GroceryItem[] items = {
        new GroceryItem(2, "Avacados"),
        new GroceryItem(1, "Apple")
    };
*/
    public GroceryItem(int quantity, String item){
        this.isChecked = false;
        this.quantity = quantity;
        this.item = item;
    }

    public GroceryItem(int quantity, String item, Boolean checked){
        this.isChecked = checked;
        this.quantity = quantity;
        this.item = item;
    }


    public Boolean getChecked() {
        return isChecked;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getQuantityString(){
        return Integer.toString(quantity);
    }

    public String getItem() {
        return item;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
