/*
 * "SearchResultAdapter.java"
 *
 * Custom RecycleView Adapter
 * Create and bind views for recipe search results
 *
 * Last Modified: 02.12.2020 04:05pm
 */
package com.srg.mealmate.Services.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.MainScreens.MealPlanFragment;
import com.srg.mealmate.R;
import com.srg.mealmate.SecondaryScreens.SavedRecipesFragment;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.Classes.Recipe1;

import java.util.ArrayList;


public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ViewHolder>{
    private static final String TAG = "RecipeItemAdapter";
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private Boolean removable; // if the recipe is part of a users meal plan or saved recipes
    private int listIndex = -1; // used to differentiate days for MealPlanFragment
    private Context mContext;


    public RecipeItemAdapter(Context context, ArrayList<Recipe> recipes, Boolean removable) {
        this.recipes = recipes;
        this.removable = removable;
        this.mContext = context;
    }


    public RecipeItemAdapter(Context context, ArrayList<Recipe> recipes, Boolean removable, int li) {
        Log.d(TAG, "listIndex was passed, li = " + li);
        this.recipes = recipes;
        this.removable = removable;
        this.mContext = context;
        this.listIndex = li;
        Log.d(TAG, "listIndex = " + listIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // bind ViewHolder and set text and click listener based on the individual recipe result
        Log.d(TAG,"onBindViewHolder:called");

        Picasso.get().load(recipes.get(position).getImageURL()).into(holder.result_image);
        holder.result_name.setText(recipes.get(position).getTitle());
        holder.result_source.setText(recipes.get(position).getSource());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call function from MainActivity and pass the selected recipe
                ((MainActivity)view.getContext()).viewRecipeDetails(recipes.get(position));
            }
        });

        if(removable){
            // if true, adapter is being used for SavedRecipeFragment or MealPlanFragment
            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // OPEN EDIT DIALOG
                    removeDialog(position);

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void removeDialog(final int index){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Remove the recipe?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext, "Removed " + recipes.get(index).getTitle(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Removed " + recipes.get(index).getId());
                        Log.d(TAG, "listIndex = " + listIndex);
                        if(listIndex==-1){
                            SavedRecipesFragment.removal = recipes.get(index).getId();
                        } else{
                            MealPlanFragment.removal = recipes.get(index).getId();
                            MealPlanFragment.day = listIndex;

                            Log.d(TAG, "listIndex =  " + listIndex);
                        }

                        recipes.remove(index);
                        notifyItemRemoved(index);

                        for(int i = 0; i< recipes.size(); i++){
                            Log.d(TAG, recipes.get(i).getId());
                        }

                        dialog.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView result_name, result_source;
        ImageView result_image;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            result_image = itemView.findViewById(R.id.result_image);
            result_name = itemView.findViewById(R.id.result_name);
            result_source = itemView.findViewById(R.id.result_source);
            parentLayout = itemView.findViewById(R.id.search_result);
        }
    }


}
