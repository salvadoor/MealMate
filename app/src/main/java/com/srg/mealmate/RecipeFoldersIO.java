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

public class RecipeFoldersIO {
    private static final String TAG = "RecipeFoldersIO";

    public static String filename = "";

    public static void setFilename(String type){
        filename = type + "_recipe_folders.dat";
    }

    public static void writeList(ArrayList<RecipeFolder> folders, Context context){
        Log.d(TAG, "writeList");
        Log.d(TAG, "filename="+filename);

        for(int i=0; i<folders.size(); i++){
            Log.d("item", folders.get(i).getName());
        }


        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(folders);
            oos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<RecipeFolder> readList(Context context) {
        Log.d(TAG, "readList");
        Log.d(TAG, "filename=" + filename);

        ArrayList<RecipeFolder> folders = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            folders = (ArrayList<RecipeFolder>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!folders.isEmpty()) {
            for (int i = 0; i < folders.size(); i++) {
                Log.d("item", folders.get(i).getName());
            }
        } else {
            Log.d("item", "No items");
        }

        return folders;
    }

}
