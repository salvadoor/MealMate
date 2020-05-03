/*
 * "SavedRecipesFragment.java"
 * Layout:  "fragment_saved_recipes.xml"
 *
 * Fragment used to display saved recipes in a folder
 *
 * Last Modified: 04.14.2020
 */
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.RecipeItemAdapter;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.FileHelpers.ArrayListStringIO;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.util.ArrayList;


public class SavedRecipesFragment extends Fragment implements IOnFocusListenable {
    private static final String TAG = "SavedRecipesFragment";
    public static String removal = "";
    private View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> folder; // list of recipe ids for saved recipes
    private ArrayList<Recipe> recipes; //recipes with ids corresponding to ids from folder
    private RecipeItemAdapter adapter;
    private String folderName;


    public SavedRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        Bundle bundle = getArguments();
        folderName = bundle.getString("folder");

        removal = "";
        loadSavedRecipes();
        init_OnClickListener();

        TextView headerTV = view.findViewById(R.id.saved_recipes_header);
        headerTV.setText("Folder: " + folderName);

        return view;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus==true) {
            Log.d(TAG, "has focus: true");
            // refreshing and saving data
            //adapter.notifyDataSetChanged();
            // also remove the item from folder
            if (folder.contains(removal)) {
                saveRecipeList();
            }
        }
    }


    private void init_OnClickListener(){
        Button btn_new = view.findViewById(R.id.btn_new_recipe);

        //btn_new only visible if in the MY RECIPES folder
        if(!folderName.equals("MY RECIPES")){
            btn_new.setVisibility(View.GONE);
            return;
        }

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null){
                    Toast.makeText(getActivity(),
                            "Must be Signed in",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity) getActivity()).newRecipe();
            }
        });
    }


    private void initRecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.recipe_list);

        adapter = new RecipeItemAdapter(getActivity(), recipes, true);
        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void loadSavedRecipes(){
        // load Recipes that are saved to the folder
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
        // retrieve recipe by id
        db.collection("recipes")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, document.getData().toString());

                            Recipe newResult = new Recipe(document);

                            recipes.add(newResult);
                            initRecyclerView();
                        } else{
                            Log.d(TAG, "error retrieving document");
                        }
                    }
                });
    }


    private void saveRecipeList(){
            // remove recipe id matching removal from folder
            ArrayListStringIO.setFilename(folderName + "_folder");
            folder.remove(removal);
            ArrayListStringIO.writeList(folder, getActivity());
            // reset removal value
            removal = "";
    }


}
