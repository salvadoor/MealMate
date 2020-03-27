package com.srg.mealmate.SecondaryScreens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.SearchResultAdapter;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.FileHelpers.ArrayListStringIO;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SavedRecipesFragment extends Fragment {
    private static final String TAG = "SavedRecipesFragment";
    private View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> folder;
    private ArrayList<Recipe> recipes;
    private SearchResultAdapter adapter;


    public SavedRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        Bundle bundle = getArguments();
        String folderName = bundle.getString("folder");

        loadSavedRecipes(folderName);

        TextView headerTV = view.findViewById(R.id.saved_recipes_header);
        headerTV.setText("Folder: " + folderName);

        return view;
    }

    private void initRecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.recipe_list);

        adapter = new SearchResultAdapter(getActivity(), recipes, true);
        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void loadSavedRecipes(String folderName){
        Log.d(TAG, "loading recipes from folder");
        ArrayListStringIO.setFilename(folderName+"_folder");
        folder = ArrayListStringIO.readList(getActivity());

        if(!folder.isEmpty()){
            recipes = new ArrayList<>();

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

                            recipes.add(newResult);
                            initRecyclerView();
                        } else{
                            Log.d(TAG, "error retrieving document");
                        }
                    }
                });
    }


}
