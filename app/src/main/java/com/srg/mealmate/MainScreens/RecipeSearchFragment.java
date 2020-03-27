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
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.Classes.RecipeSearchMapping;
import com.srg.mealmate.Services.Adapters.SearchResultAdapter;
import com.srg.mealmate.Services.FileHelpers.RecipeSearchMapIO;

import java.util.ArrayList;
import java.util.HashMap;


public class RecipeSearchFragment extends Fragment {
    private static final String TAG = "RecipeFragmentSearch";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Recipe> results = new ArrayList<>();
    private ArrayList<RecipeSearchMapping> searchMap = new ArrayList<>();
    private Boolean dataPreserved = false;
    private SearchResultAdapter adapter;
    private View view;


    public RecipeSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        initRecyclerView();
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
            refreshSearchMap();
            searchMap = RecipeSearchMapIO.readList(getActivity());
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
                                String name = document.getString("title").toLowerCase();
                                String id = document.getId();

                                searchMap.add(new RecipeSearchMapping(name, id));
                                RecipeSearchMapIO.writeList(searchMap, getActivity());
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
        adapter = new SearchResultAdapter(getActivity(), results, false);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        for(int i=0; i < searchMap.size(); i++){
            if(searchMap.get(i).getName().contains(searchString)){
                retrieveRecipe(searchMap.get(i).getId());
            }
        }
    }

    private void retrieveRecipe(String id){

        db.collection("recipes")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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

                        results.add(newResult);
                        adapter.notifyDataSetChanged();
                        //initRecyclerView();
                    }
                });


    }

}
