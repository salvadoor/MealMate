package com.srg.mealmate.Services.FileHelpers;

import android.content.Context;
import android.util.Log;

import com.srg.mealmate.Services.Classes.GroceryItem;

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

    public static void writeList(ArrayList<GroceryItem> items, Context context){
        Log.d(TAG, "writeList");
        Log.d(TAG, "filename="+filename);

        for(int i=0; i<items.size(); i++){
            Log.d("item", items.get(i).getName());
        }


        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(items);
            oos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<GroceryItem> readList(Context context) {
        Log.d(TAG, "readList");
        Log.d(TAG, "filename=" + filename);

        ArrayList<GroceryItem> items = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (ArrayList<GroceryItem>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Log.d("item", items.get(i).getName());
            }
        } else {
            Log.d("item", "No items");
        }

        return items;
    }
}
