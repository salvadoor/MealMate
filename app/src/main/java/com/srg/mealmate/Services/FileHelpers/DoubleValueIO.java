/*
 * "DoubleValueIO.java"
 *
 * reading/writing a double value from a file
 *
 * Last Modified: 04.15.2020
 */
package com.srg.mealmate.Services.FileHelpers;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class DoubleValueIO {
    private static final String TAG = "DoubleValueIO";
    public static String filename = "";


    public static void setFilename(String fName){
        filename = fName + ".dat";
    }


    public static void writeDouble(Double dVal, Context context){
        Log.d(TAG, "writeList");
        Log.d(TAG, "filename="+filename);

        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeDouble(dVal);
            oos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public static Double readDouble(Context context) {
        Log.d(TAG, "readList");
        Log.d(TAG, "filename=" + filename);

        Double dVal = 0.0;

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dVal = ois.readDouble();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dVal;
    }
}
