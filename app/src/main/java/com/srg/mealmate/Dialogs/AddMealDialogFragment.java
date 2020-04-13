package com.srg.mealmate.Dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.MealPlan;
import com.srg.mealmate.Services.FileHelpers.MealPlanIO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddMealDialogFragment extends DialogFragment {
    private static final String TAG = "AddMealPlanDialog";
    private static final String[] days =
            {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private ArrayList<String> filenames = new ArrayList<>();
    private Calendar c;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
    private Spinner spinner_plans;
    private Spinner spinner_days;
    private String recipeID;
    private View view;


    public AddMealDialogFragment() {
        // Required empty public constructor
    }


    public static AddMealDialogFragment newInstance(String recipeID){
        AddMealDialogFragment frag = new AddMealDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("recipeID", recipeID);
        frag.setArguments(bundle);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_meal_dialog, container, false);

        Bundle bundle = getArguments();
        recipeID = bundle.getString("recipeID");

        getSundays();
        initSpinners();
        initOnClickListeners();

        return view;
    }

    public void initSpinners(){
        // set spinner and adapter for Meal Plans
        spinner_plans = view.findViewById(R.id.spinner_plans);
        ArrayAdapter<String> adapter_plans = new ArrayAdapter<String>(getContext(),
                R.layout.unit_item, filenames);
        adapter_plans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_plans.setAdapter(adapter_plans);

        //set spinner and adapter for days in Meal Plan
        spinner_days = view.findViewById(R.id.spinner_days);
        ArrayAdapter<String> adapter_days = new ArrayAdapter<String>(getContext(),
                R.layout.unit_item, days);
        adapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_days.setAdapter(adapter_days);
    }


    public void initOnClickListeners(){
        Button btn_add, btn_cancel;

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected Meal Plan and load it
                String planDate = spinner_plans.getSelectedItem()
                        .toString()
                        .replaceAll("\\s+", "");

                MealPlanIO.setFilename(planDate);
                MealPlan plan = MealPlanIO.readList(getActivity());

                // get selected day
                String selected_day = spinner_days.getSelectedItem()
                        .toString()
                        .replaceAll("\\s+", "");

                // add recipe to meal plan
                plan.addRecipe(selected_day, recipeID);
                Log.d(TAG, plan.getDay(0).toString());

                //save the meal plan
                MealPlanIO.writeList(plan, getActivity());

                dismiss();
            }
        });

        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void getSundays(){
        // get filenames for where the mealplans are stored
        // filenames used for spinner_plans selection

        // get Sunday date of current week
        c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        filenames.add(dateFormat.format(c.getTime()));

        // get Sunday date of previous week
        c.add(Calendar.DATE, -7);
        filenames.add(0, dateFormat.format(c.getTime()));

        // get Sunday date of week after current week
        c.add(Calendar.DATE, 14);
        filenames.add(dateFormat.format(c.getTime()));

        Log.d(TAG, filenames.toString());
    }


}
