package com.srg.mealmate;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RecipeSearchMapIO {
    private static final String TAG = "RecipeSearchMapIO";

    private static String filename = "RecipeSearchMap.dat";

    public static void writeList(ArrayList<RecipeSearchMapping> searchMap, Context context){
        Log.d(TAG, "writeList");

        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(searchMap);
            oos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public static ArrayList<RecipeSearchMapping> readList(Context context){
        ArrayList<RecipeSearchMapping> searchMap = new ArrayList<>();

        try{
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            searchMap = (ArrayList<RecipeSearchMapping>) ois.readObject();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        return searchMap;
    }

}
