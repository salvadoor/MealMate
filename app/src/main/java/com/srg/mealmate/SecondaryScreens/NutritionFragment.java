/*
 * "NutritionFragment.java"
 * Layout:  "fragment_nutrition.xml"
 *
 * Fragment used to display basic nutrition for a recipe
 *
 * Last Modified: 04.17.2020
 */
package com.srg.mealmate.SecondaryScreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srg.mealmate.R;

import java.util.HashMap;


public class NutritionFragment extends Fragment {
    private View view;
    private HashMap nutrition;
    private String[] nutritionKeys;
    private String[] nutritionUnits;


    public NutritionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        Bundle bundle = getArguments();
        nutrition = (HashMap) bundle.getSerializable("nutrition");

        setNutrition();

        return view;
    }


    private void setNutrition(){
        TextView tv = view.findViewById(R.id.nutrition);

        StringBuilder basic_nutrition = new StringBuilder();

        if(nutrition.size()<1){
            // if no nutrition data for this recipe
            basic_nutrition.append("No Nutrition Details Available");
        } else{
            nutritionKeys = getResources().getStringArray(R.array.nutrition);
            nutritionUnits = getResources().getStringArray(R.array.nutrition_units);

            for(int i=0;i<nutritionKeys.length;i++){
                String key = nutritionKeys[i];
                String unit = nutritionUnits[i];
                if(nutrition.containsKey(key)) {
                    basic_nutrition.append("\n")
                            .append(key)
                            .append(": ")
                            .append(nutrition.get(key))
                            .append(unit)
                            .append("\n");
                }
            }
        }

        tv.setText(basic_nutrition.toString());

    }
}
