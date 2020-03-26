package com.srg.mealmate.Services.FileHelpers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ArrayListStringIO {

    public static String filename = "";

    public static void setFilename(String name){
        filename = name + ".dat";
    }


    public static void writeList(ArrayList<String> items, Context context){
        Log.d(TAG, "writeList");
        Log.d(TAG, "filename="+filename);

        Log.d(TAG, "items:");
        for(int i=0; i<items.size(); i++){
            Log.d(TAG, items.get(i));
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


    public static void appendItem(String newItem, Context context){
        Log.d(TAG, "appendItem");

        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newItem);
            oos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public static ArrayList<String> readList(Context context) {
        Log.d(TAG, "readList");
        Log.d(TAG, "filename=" + filename);

        ArrayList<String> items = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!items.isEmpty()) {
            Log.d(TAG, "items:");
            for (int i = 0; i < items.size(); i++) {
                Log.d(TAG, items.get(i));
            }
        } else {
            Log.d(TAG, "No items");
        }

        return items;
    }


    public static void delete(String file, Context context){
        String filepath = "/data/user/0/com.srg.mealmate/files/" + file;
        Log.d(TAG, "deleting: "+ filepath);

            File f = new File(filepath);
            if(f.delete()) {
                Log.d(TAG, "Delete successful");
            } else{
                Log.d(TAG, "Delete Failed");
            }

    }

}