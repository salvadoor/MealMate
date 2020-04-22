package com.srg.mealmate.MainScreens;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.RecipeItemAdapter;
import com.srg.mealmate.Services.Classes.MealPlan;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.FileHelpers.MealPlanIO;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MealPlanFragment extends Fragment implements IOnFocusListenable {
    private static final String TAG = "MealPlanFragment";
    private View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
    private Calendar c;
    private int weeksFromCurr; // 0 value is current week, -1 is week before current, 1 is week after current, etc
    private static final int weeksSaved = 1;
    private Boolean dataPreserved = false; //initial false,  true when fragment is not destroyed and then re-inflated

    private MealPlan plan = new MealPlan();
    private ArrayList<RecipeItemAdapter> adapters;
    private ArrayList<ArrayList<Recipe>> recipes;
    public static String removal = "";
    public static int day = -1;
    private String sundayDate = "";
    // private ArrayList<RecyclerView> recyclerViews = new ArrayList<>();

    //private MealAdapter adapter;


    public MealPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meal_plan, container, false);

        removal = "";
        day = -1;
        init_ArrayLists();
        init_weekData();
        init_NavClickListeners();

        // initialize the RecyclerView for each day
        for(int i=0; i<7; i++){
            initRecyclerView(i);
        }

        dataPreserved = true;

        return view;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus==true) {
            Log.d(TAG, "has focus: true");
            // refreshing and saving data
            adapters.get(day).notifyDataSetChanged();
            if (plan.getDay(day).contains(removal)) {
                plan.getDay(day).remove(removal);
                saveMealPlan();

                day = -1;
                removal = "";
            }
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();
        // save Meal Plan
        saveMealPlan();
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume");
        super.onResume();
    }


    private void init_ArrayLists(){
        // create new ArrayLists and initialize them
        recipes = new ArrayList<>();
        adapters = new ArrayList<>();

        for(int i=0;i<7;i++){
            recipes.add(new ArrayList<Recipe>());
            adapters.add(new RecipeItemAdapter(getActivity(), recipes.get(i), true, i));
        }
    }


    private void initRecyclerView(int index){
        int rvId = 0;
        int rlId = 0;
        int imgId = 0;
        // switch case for viewId to int id for proper recyclerview
        switch (index){
            case 0:
                rvId = R.id.sunday_meals;
                rlId = R.id.header_sunday;
                imgId = R.id.eye1;
                break;
            case 1:
                rvId = R.id.monday_meals;
                rlId = R.id.header_monday;
                imgId = R.id.eye2;
                break;
            case 2:
                rvId = R.id.tuesday_meals;
                rlId = R.id.header_tuesday;
                imgId = R.id.eye3;
                break;
            case 3:
                rvId = R.id.wednesday_meals;
                rlId = R.id.header_wednesday;
                imgId = R.id.eye4;
                break;
            case 4:
                rvId = R.id.thursday_meals;
                rlId = R.id.header_thursday;
                imgId = R.id.eye5;
                break;
            case 5:
                rvId = R.id.friday_meals;
                rlId = R.id.header_friday;
                imgId = R.id.eye6;
                break;
            default:
                rvId = R.id.saturday_meals;
                rlId = R.id.header_saturday;
                imgId = R.id.eye7;
                break;
        }

        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(rvId);
        //adapters.set(index, new SearchResultAdapter(getActivity(), recipes.get(index), false));
        //adapters.get(index).notifyDataSetChanged();
        init_HeaderClickListener(rlId, imgId, rv);

        rv.setAdapter(adapters.get(index));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void init_weekData(){
        // If pref is 5, there are 5//2 weeks saved before and  after the current week
        //weeksSaved = (new Preferences().getWeeks()) / 2;

        if(dataPreserved){
            // if dataStored == true, then variables such as the c Calendar object already have defined values
            //set week based on current c, week will be last viewed week before fragment was detached
            setWeek(0);

        } else{
            // else initialize week tracking variables and set current week
            weeksFromCurr = 0;
            setCurrentWeek();
        }
    }


    private void setCurrentWeek(){
        // set Initial Calendar and then set current week
        c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // All dates will reference a Sunday
        setWeek(0);
    }


    private void setWeek(int offset){
        String weekText;
        TextView tv = view.findViewById(R.id.txt_current);

        // save current Grocery List if it exists
        if(!plan.isEmpty() && dataPreserved) {
            saveMealPlan();
        }
        // change week, ex: go back a week if offset = -7
        c.add(Calendar.DATE, offset);
        weekText = "Week of " + dateFormat.format(c.getTime());
        tv.setText(weekText);

        // Load corresponding Grocery List
        clearRecipes();
        sundayDate = dateFormat.format(c.getTime());
        loadMealPlan();
    }


    private Boolean isWeekSaved(int direction){
        // Check if week navigation will go out of bounds, return boolean
        // if not out of bounds, update weeksFromCurr and return true
        if(Math.abs(weeksFromCurr+direction) > weeksSaved ){
            Toast.makeText(getActivity(), "No more Weeks Saved", Toast.LENGTH_SHORT).show();
            return false;
        }
        weeksFromCurr+= direction;

        return true;
    }


//----------------------------------------------------------------------------------
// Methods to set OnClickListeners for buttons
    private void init_NavClickListeners(){
        // set OnClickListeners for prev and next buttons
        // These buttons will change the week being viewed
        Button btn_prev, btn_next;

        btn_prev = view.findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWeekSaved(-1)){
                    setWeek(-7); // Go to prev week, -7 days
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWeekSaved(1)){
                    setWeek(7); // Go to next week, +7 days
                }
            }
        });
    }

    private void init_HeaderClickListener(int rl_id, int img_id, final RecyclerView rv){
        RelativeLayout rl = view.findViewById(rl_id);
        final ImageView iv = view.findViewById(img_id);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rv.getVisibility() == View.VISIBLE){
                    iv.setImageResource(R.drawable.visibility_off_white);
                    rv.setVisibility(View.GONE);
                } else{
                    rv.setVisibility(View.VISIBLE);
                    iv.setImageResource(R.drawable.visibility_white);
                }
            }
        });
    }


//----------------------------------------------------------------------------------
// Methods to deal with reading and writing data using the GroceryListIO class
    private void loadMealPlan(){
        Log.d(TAG, "Loading Meal Plan");
        MealPlanIO.setFilename(sundayDate);
        plan = MealPlanIO.readList(getActivity());

        for(int day=0; day<plan.size(); day++){
            if(!plan.getWeek().get(day).isEmpty()){
                // recipes.set(day, new ArrayList<Recipe>());

                Log.d(TAG, plan.getDay(day).toString());

                for(int i=0; i<plan.getDay(day).size();i++){
                    retrieveRecipe(plan.getDay(day).get(i), day);
                }
            }
        }

    }


    private void saveMealPlan(){
        // save current MealPlan
        Log.d(TAG, "Saving Meal Plan");
        MealPlanIO.writeList(plan, getActivity());
    }


    private void clearRecipes(){
        // set each recipe list to null and update adapter
        for(int i=0;i<plan.getWeek().size();i++){
            recipes.get(i).clear();
            adapters.get(i).notifyDataSetChanged();

        }
    }


    private void retrieveRecipe(String id, final int day){
        db.collection("recipes")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, document.getData().toString());

                            String name = document.getString("title");
                            String source = document.getString("source");
                            String id = document.getId();
                            String imgURL = document.getString("imageURL");
                            ArrayList<HashMap> ingredients = (ArrayList<HashMap>) document.get("ingredients");
                            String category = document.getString("category");
                            ArrayList<String> instructions = (ArrayList<String>) document.get("instructions");

                            Recipe newResult = new Recipe(name, source, id, ingredients,
                                    category, instructions, imgURL);

                            recipes.get(day).add(newResult);
                            adapters.get(day).notifyDataSetChanged();
                            // initRecyclerView(day);
                        } else{
                            Log.d(TAG, "error retrieving document");
                        }
                    }
                });
    }


}
