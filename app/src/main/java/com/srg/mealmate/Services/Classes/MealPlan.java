/*
 * "MealPlan.java"
 *
 *
 * Last Modified: 04.11.2020 09:00pm
 */
package com.srg.mealmate.Services.Classes;

import java.util.ArrayList;

public class MealPlan {
    private ArrayList<ArrayList<String>> week;
    // ArrayList of ArrayLists
    // list has list of recipe ids for each day in a meal plan
    //      week.get(0) retrieves list of meals for Sunday,
    //      week.get(6) retrieves list of meals on Saturday

    public MealPlan(){
        this.week = new ArrayList<>();
        for(int i=0;i<7;i++){
            this.week.add(new ArrayList<String>());
        }
    }

    public MealPlan(ArrayList<ArrayList<String>> week) {
        this.week = week;
    }

    public ArrayList<ArrayList<String>> getWeek() {
        return week;
    }

    public void setWeek(ArrayList<ArrayList<String>> week) {
        this.week = week;
    }

    public ArrayList<String> getDay(int dayIndex){
        return week.get(dayIndex);
    }

    public void setDay(int dayIndex, ArrayList<String> day){
        this.week.set(dayIndex, day);
    }

    public boolean isEmpty(){
        if(this.week.isEmpty()){
            return true;
        }

        return false;
    }

    public int size(){
        return this.week.size(); // should be 0 or 7
    }


    public void addRecipe(String day, String recipeID){
        int dayIndex;

        switch(day){
            case "Sunday":
                dayIndex = 0;
                break;
            case "Monday":
                dayIndex = 1;
                break;
            case "Tuesday":
                dayIndex = 2;
                break;
            case "Wednesday":
                dayIndex = 3;
                break;
            case "Thursday":
                dayIndex = 4;
                break;
            case "Friday":
                dayIndex = 5;
                break;
            case "Saturday":
                dayIndex = 6;
                break;
            default:
                dayIndex = 0;
                break;
        }

        this.week.get(dayIndex).add(recipeID);
    }


}
