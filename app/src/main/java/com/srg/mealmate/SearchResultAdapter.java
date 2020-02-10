package com.srg.mealmate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{
    private static final String TAG = "SearchResultAdapter";

    // private ArrayList<SearchResult> results = new ArrayList<>();
    private ArrayList<Recipe> results = new ArrayList<>();
    private Context mContext;

    public SearchResultAdapter(Context context, ArrayList<Recipe> results) {
        this.results = results;
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
        Log.d(TAG,"onBindViewHolder:called");

        holder.result_name.setText(results.get(position).getName());
        holder.result_source.setText(results.get(position).getSource());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(mContext, results.get(position).getName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, results.get(position).getIngredients().get(0).get("name").toString(), Toast.LENGTH_SHORT).show();
                ((MainActivity)view.getContext()).viewRecipeDetails(results.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView result_name, result_source;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            result_name = itemView.findViewById(R.id.result_name);
            result_source = itemView.findViewById(R.id.result_source);
            parentLayout = itemView.findViewById(R.id.search_result);
        }


    }

}
