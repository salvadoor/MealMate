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
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SavedRecipesFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> folders = new ArrayList<>();
    private ArrayList<String> folder = new ArrayList<>();
    private ArrayList<Recipe> results;
    private View view;
    private RecipeFolderAdapter folderAdapter;
    private SearchResultAdapter recipeAdapter;
    private int rvType = 0; // 0 for folderAdapter, 1 for recipeAdapter


    public SavedRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        // TESTING-------------------------------------
        if(folders.isEmpty()){
            folders.add("ALL");
            folders.add("MY RECIPES");
        }

        loadFolders();
        init_click_listeners();
        initFocusChangeListener();
        //-----------------------------------------------

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save Folders
        Log.d(TAG, "onPause");
        saveFolders();
    }


    private void init_click_listeners(){
        final Button saved_btn, myRecipes_btn, newFolder_btn;

        saved_btn = view.findViewById(R.id.btn_saved);
        myRecipes_btn = view.findViewById(R.id.btn_myRecipes);
        newFolder_btn = view.findViewById(R.id.btn_new_folder);

        saved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved_btn.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
                myRecipes_btn.setBackground(getResources().getDrawable(R.color.colorPrimary));

            }
        });

        myRecipes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecipes_btn.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
                saved_btn.setBackground(getResources().getDrawable(R.color.colorPrimary));

            }
        });

        newFolder_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ((MainActivity) getActivity()).newFolder(folders);
            }
        });

    }


    private void initRecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.folder_list);

        if(rvType == 0){
            folderAdapter = new RecipeFolderAdapter(folders, getActivity());
            rv.setAdapter(folderAdapter);
        } else{
            recipeAdapter = new SearchResultAdapter(getActivity(), results);
            rv.setAdapter(recipeAdapter);
        }

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void initFocusChangeListener(){
        // listener used to update recyclerview after an EditItem or AddItem dialog fragment is closed
        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(final boolean hasFocus) {
                if(hasFocus) {
                    Log.d(TAG, "has Focus: true");

                    folderAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void loadFolders(){
        Log.d(TAG, "Loading folders");

        ArrayListStringIO.setFilename("recipe_folders");
        folders = ArrayListStringIO.readList(getActivity());

        initRecyclerView();
    }


    private void saveFolders(){
        Log.d(TAG, "saving folders");
        ArrayListStringIO.writeList(folders, getActivity());
    }


    private void loadSavedRecipes(String folderName){
        rvType = 1;

        ArrayListStringIO.setFilename(folderName+"_folder");
        folder = ArrayListStringIO.readList(getActivity());

        if(!folder.isEmpty()){
            results = new ArrayList<>();

            for(int i=0; i<folder.size();i++){
                retrieveRecipe(folder.get(i));
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
                        initRecyclerView();
                    }
                });


    }

}
