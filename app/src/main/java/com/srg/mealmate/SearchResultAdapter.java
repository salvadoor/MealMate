/*
 * "SearchResultAdapter.java"
 *
 * Custom RecycleView Adapter
 * Create and bind views for recipe search results
 *
 * Last Modified: 02.12.2020 04:05pm
 */
package com.srg.mealmate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{
    private static final String TAG = "SearchResultAdapter";
    private ArrayList<Recipe> results = new ArrayList<>();
    private Boolean isSaved;
    private Context mContext;


    public SearchResultAdapter(Context context, ArrayList<Recipe> results, Boolean isSaved) {
        this.results = results;
        this.isSaved = isSaved;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // bind ViewHolder and set text and click listener based on the individual recipe result
        Log.d(TAG,"onBindViewHolder:called");

        Picasso.get().load(results.get(position).getImgURL()).into(holder.result_image);
        holder.result_name.setText(results.get(position).getName());
        holder.result_source.setText(results.get(position).getSource());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call function from MainActivity and pass the selected recipe
                ((MainActivity)view.getContext()).viewRecipeDetails(results.get(position));
            }
        });

        if(isSaved){
            // if true, adapter is being used for SavedRecipeFragment
            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // OPEN EDIT DIALOG

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
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
