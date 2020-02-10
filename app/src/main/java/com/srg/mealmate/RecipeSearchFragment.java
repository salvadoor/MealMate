package com.srg.mealmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeSearchFragment extends Fragment {
    private static final String TAG = "RecipeFragmentSearch";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private ArrayList<SearchResult> results = new ArrayList<>();
    private ArrayList<Recipe> results = new ArrayList<>();
    private View view;
    // SearchResultAdapter adapter;


    public RecipeSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_search, container, false);


       // results = new ArrayList<>();
        //initMockItems();
        initRecyclerView();

        init_empty_search();




        return view;
    }

    private void initMockItems(){
        // For initial Testing and Development, populate grocery list
      //  results.add(new SearchResult("1", "Avacados", "wdcvfv"));
      //  results.add(new SearchResult("1", "Avacados", "wdcvfv"));
      //  results.add(new SearchResult("1", "Avacados", "wdcvfv"));

    }
    private void initRecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.results_list);
        SearchResultAdapter adapter = new SearchResultAdapter(getActivity(), results);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void init_empty_search(){
        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            results = new ArrayList<>();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.getString("title");
                                String source = document.getString("source");
                                String id = document.getId();

                                //----
                                ArrayList<HashMap> ingredients = (ArrayList<HashMap>) document.get("ingredients");
                                String category = document.getString("category");
                                ArrayList<String> instructions = (ArrayList<String>) document.get("instructions");
                              //  Toast.makeText(getActivity(), ingredients.get(0).get("name").toString(), Toast.LENGTH_SHORT);
                                //-----
                                //SearchResult sr = new SearchResult(name, source, id, ingredients);
                                //results.add(sr);
                                Recipe newResult = new Recipe(name, source, id, ingredients,
                                        category, instructions);
                                results.add(newResult);

                                initRecyclerView();
                            }
                        } else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}
