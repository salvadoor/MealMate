package com.srg.mealmate.MainScreens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.SearchResultAdapter;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MealPlanFragment extends Fragment implements IOnFocusListenable {
    private static final String TAG = "MealPlanFragment";
    private View view;
    private Boolean dataPreserved = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
    private Calendar c;
    private int weeksFromCurr;
    private int weeksSaved;
    private ArrayList<Recipe> recipes;
    private SearchResultAdapter adapter;


    public MealPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meal_plan, container, false);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save Meal Plan(s)
        //saveGroceryList();
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "has focus: true");
        // refreshing and saving data
        adapter.notifyDataSetChanged();
        //saveGroceryList();
    }


    private void loadMealPlan(String sundayDate){
        Log.d(TAG, "Loading Meal Plan");

    }


    private void saveMealPlan(){
        Log.d(TAG, "Saving Meal Plan");

        // ----

    }




}
