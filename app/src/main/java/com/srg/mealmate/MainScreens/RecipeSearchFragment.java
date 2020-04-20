package com.srg.mealmate.MainScreens;


import android.net.ParseException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.RecipeItemAdapter;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.Classes.RecipeSearchMapping;
import com.srg.mealmate.Services.FileHelpers.RecipeSearchMapIO;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class RecipeSearchFragment extends Fragment {
    private static final String TAG = "RecipeFragmentSearch";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Recipe> results = new ArrayList<>();
    private ArrayList<RecipeSearchMapping> searchMap = new ArrayList<>();
    private Boolean dataPreserved = false;
    private RecipeItemAdapter adapter;
    private View view;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    private final Date date = new Date();
    private String[] categories;
    private Spinner spin_categories;


    public RecipeSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        initRecyclerView();
        initSpinner();
        if(!dataPreserved){
            loadSearchMap();
            searchRecipes("");
        }

        init_clickListener();

        dataPreserved = true;

        return view;
    }


    private void loadSearchMap(){
        searchMap = RecipeSearchMapIO.readList(getActivity());

        if(searchMap.isEmpty()){
            Log.d(TAG, "searchMap is empty");
            searchMap.add(new RecipeSearchMapping("LAST-UPDATE", dateFormat.format(date)));

            refreshSearchMap();
        } else{ // check if searchMap has been updated within the past day, if not, update
            try {
                Date lastUpdated = dateFormat.parse(searchMap.get(0).getCategory());

                long diffInMS = Math.abs(date.getTime() - lastUpdated.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMS, TimeUnit.MILLISECONDS);

                if (diffInDays >= 1) {
                    Log.d(TAG, "Time to update: days since last update: " + diffInDays);

                    searchMap.clear();
                    searchMap.add(new RecipeSearchMapping("LAST-UPDATE", dateFormat.format(date)));

                    refreshSearchMap();
                }
            } catch (java.text.ParseException e){
                Log.d(TAG, "ParseException");
            }
        }

    }


    private void refreshSearchMap(){
        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                String rName = document.getString("title");
                                String rCategory = document.getString("category");

                                searchMap.add(new RecipeSearchMapping(rName, rCategory));
                                RecipeSearchMapIO.writeList(searchMap, getActivity());
                                retrieveRecipe(rName);
                            }
                        } else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void initRecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.results_list);
        adapter = new RecipeItemAdapter(getActivity(), results, false);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void initSpinner(){
        // set spinner and adapter for categories
        categories =  getContext().getResources().getStringArray(R.array.categories);

        spin_categories = view.findViewById(R.id.spin_category);
        ArrayAdapter<String> adapter_categories = new ArrayAdapter<String>(getContext(),
                R.layout.unit_item, categories);
        adapter_categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_categories.setAdapter(adapter_categories);
    }


    private void init_clickListener(){
        Button search_btn = view.findViewById(R.id.searchBtn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchET = view.findViewById(R.id.searchTxt);
                String searchString = searchET.getText().toString().toLowerCase();
                searchRecipes(searchString);

            }
        });

    }


    private void searchRecipes(String searchString){
        Log.d(TAG, "Searching for: '" + searchString + "'");
        results.removeAll(results);

        String searchCategory = spin_categories.getSelectedItem().toString();
        searchString = searchString.toLowerCase();

        for(int i=1; i < searchMap.size(); i++){
            Log.d("searchRecipes", "Category: " + searchCategory);
            Log.d("searchMap", "name: " + searchMap.get(i).getName() + ", category: " + searchMap.get(i).getCategory());
            String recipeName = searchMap.get(i).getName().toLowerCase(); // get lower-cased recipe name

            if(recipeName.contains(searchString)){
                if(searchCategory.equals("all")){
                    Log.d(TAG, searchMap.get(i).getName());
                    retrieveRecipe(searchMap.get(i).getName());
                } else if(searchMap.get(i).getCategory().equals(searchCategory)){
                    Log.d(TAG, searchMap.get(i).getName());
                    retrieveRecipe(searchMap.get(i).getName());
                }

            }
        }
    }


    private void retrieveRecipe(String name){
        Log.d("retrieveRecipe v2", name);

        db.collection("recipes")
                .whereEqualTo("title", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Log.d(TAG, doc.toString());
                                results.add(new Recipe(doc));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
