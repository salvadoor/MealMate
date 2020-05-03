/*
 * "SavedFoldersFragment.java"
 * Layout:  "fragment_saved_folders.xml"
 *
 * Fragment used to show user's saved folders for organizing recipes
 */
package com.srg.mealmate.MainScreens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.RecipeFolderAdapter;
import com.srg.mealmate.Services.FileHelpers.ArrayListStringIO;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.util.ArrayList;


public class SavedFoldersFragment extends Fragment implements IOnFocusListenable {
    private static final String TAG = "SavedFoldersFragment";
    private ArrayList<String> folders = new ArrayList<>();
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


        ArrayListStringIO.setFilename("recipe_folders");

        loadFolders();
        if(folders.isEmpty()){
            initFolders();
        }

        initRecyclerView();

        init_click_listeners();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save Folders
        Log.d(TAG, "onPause");
        saveFolders();
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "has focus");
        // refreshing and saving data
        adapter.notifyDataSetChanged();
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


    private void loadFolders(){
        Log.d(TAG, "Loading folders");
        folders = ArrayListStringIO.readList(getActivity());
    }

    private void initFolders(){
        Log.d(TAG, "Empty Folder");
        folders.add("MY RECIPES");
    }


    private void saveFolders(){
        Log.d(TAG, "saving folders");
        ArrayListStringIO.writeList(folders, getActivity());
    }

    public void updateFolders(){
        adapter.notifyDataSetChanged();
        saveFolders();
    }

}
