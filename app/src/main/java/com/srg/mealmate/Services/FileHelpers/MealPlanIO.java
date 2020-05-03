/*
 * "MealPlanIO.java"
 *
 * reading/writing a MealPlan from file
 *
 * Last Modified: 04.15.2020
 */
package com.srg.mealmate.Services.FileHelpers;

import android.content.Context;
import android.util.Log;

import com.srg.mealmate.Services.Classes.MealPlan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MealPlanIO {
    private static final String TAG = "MealPlanIO";
    public static String filename = "";

    public static void setFilename(String sundayDate){
        filename = sundayDate + "_plan.dat";
    }

    public static void writeList(MealPlan plan, Context context){
        Log.d(TAG, "writeList");
        Log.d(TAG, "filename="+filename);

        Log.d(TAG, "Meal Plan: ");
        for(int i=0;i<plan.getWeek().size();i++){
            Log.d(TAG, "Week " + i );
            for(String recipe : plan.getDay(i)){
                Log.d(TAG, recipe);
            }
        }


        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(plan.getWeek());
            oos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static MealPlan readList(Context context) {
        Log.d(TAG, "readList");
        Log.d(TAG, "filename=" + filename);

        MealPlan plan = new MealPlan();


        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            plan.setWeek((ArrayList<ArrayList<String>>) ois.readObject());
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Meal Plan: ");
        for(int i=0;i<plan.getWeek().size();i++){
            Log.d(TAG, "Week " + i );
            for(String recipe : plan.getDay(i)){
                Log.d(TAG, recipe);
            }
        }
        /* debugging
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Log.d("item", items.get(i).getName());
            }
        } else {
            Log.d("item", "No items");
        }
        */

        return plan;
    }
}
