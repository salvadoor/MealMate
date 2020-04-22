
package com.srg.mealmate.SecondaryScreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.GroceryItemAdapter;
import com.srg.mealmate.Services.Adapters.InstructionAdapter;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.util.ArrayList;
import java.util.HashMap;


public class NewRecipeFragment extends Fragment implements IOnFocusListenable {
    private static final String TAG = "NewRecipeFragment";
    private View view;
    private GroceryItemAdapter adapt_ingredients;
    private InstructionAdapter adapt_instructions;
    private ArrayList<GroceryItem> ingredients = new ArrayList<>();
    private ArrayList<String> instructions = new ArrayList<>();


    public NewRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_recipe, container, false);

        init_RecyclerViews();
        init_OnClickListeners();

        return view;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            Log.d(TAG, "has focus: true");
            // refreshing and saving data
            adapt_ingredients.notifyDataSetChanged();
            adapt_instructions.notifyDataSetChanged();

            Log.d(TAG, instructions.toString());
        }
    }

    private void init_RecyclerViews(){
        // create RecyclerView for ingredients
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.grocery_list);
        adapt_ingredients = new GroceryItemAdapter(getActivity(), getActivity(), ingredients);

        rv.setAdapter(adapt_ingredients);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // create RecyclerView for instructions
        RecyclerView rv2 = view.findViewById(R.id.instruction_list);
        adapt_instructions = new InstructionAdapter(getActivity(), getActivity(), instructions);

        rv2.setAdapter(adapt_instructions);
        rv2.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void init_OnClickListeners(){
        Button btn_add, btn_cancel;
        ImageView btn_new_ingredient, btn_new_instruction;

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_new_ingredient = view.findViewById(R.id.btn_new_ingredient);
        btn_new_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).newItem(ingredients, new HashMap());
            }
        });

        btn_new_instruction = view.findViewById(R.id.btn_new_instruction);
        btn_new_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).newInstruction(instructions);
            }
        });
    }

}
