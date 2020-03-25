package com.srg.mealmate.MainScreens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.Adapters.RecipeFolderAdapter;
import com.srg.mealmate.Services.FileHelpers.ArrayListStringIO;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SavedFoldersFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> folders = new ArrayList<>();
    private ArrayList<String> folder = new ArrayList<>();
    private ArrayList<Recipe> results;
    private View view;
    private RecipeFolderAdapter adapter;


    public SavedFoldersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_folders, container, false);

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
        final Button newFolder_btn;
        newFolder_btn = view.findViewById(R.id.btn_new_folder);

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

        adapter = new RecipeFolderAdapter(folders, getActivity());
        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void initFocusChangeListener(){
        // listener used to update recyclerview after an EditItem or AddItem dialog fragment is closed
        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(final boolean hasFocus) {
                if(hasFocus) {
                    Log.d(TAG, "has Focus: true");

                    adapter.notifyDataSetChanged();
                    saveFolders();
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

}
