/*
 * "GroceryItemAdapter.java"
 *
 * Custom RecycleView Adapter
 * Create and bind views for grocery items
 *
 * TO DO:
 *  change dialog strings from hard-coded strings to resource strings
 *
 * Last Modified: 02.12.2020 04:11pm
 */
package com.srg.mealmate.Services.Adapters;

import android.app.Activity;
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

import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Classes.GroceryItem;

import java.util.ArrayList;
import java.util.HashMap;


public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {
    private static final String TAG = "InstructionsAdapter";
    private Context mContext;
    private Activity activity;
    private ArrayList<String> instructions = new ArrayList<>();


    public InstructionAdapter(Context context,
                              Activity activity,
                              ArrayList<String> steps) {
        this.mContext = context;
        this.activity = activity;
        this.instructions = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instruction_step, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder:called");

        holder.parentLayout.setText(instructions.get(position));
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.instruction_step);
        }
    }

}

