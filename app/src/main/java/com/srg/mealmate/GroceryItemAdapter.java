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
package com.srg.mealmate;

import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;


public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {
    private static final String TAG = "GroceryItemAdapter";
    private ArrayList<GroceryItem> items = new ArrayList<>();
    private Context mContext;
    private Activity activity;
    private HashMap<String, Double> itemHash;


    public GroceryItemAdapter(Context context,
                              Activity activity,
                              ArrayList<GroceryItem> items,
                              HashMap itemHash) {
        this.items = items;
        this.mContext = context;
        this.activity = activity;
        this.itemHash = itemHash;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grocery_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder:called");
        Boolean checked = items.get(position).getChecked();

        if (checked) {
            holder.checkBox.setImageResource(R.drawable.checked_box);
        } else {
            holder.checkBox.setImageResource(R.drawable.checkbox_outline);
        }

        holder.details.setText(items.get(position).getGroceryDetailString());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check/uncheck selected grocery item
                Log.d(TAG, "onClick:clicked on: " + items.get(position).getName());
                if (items.get(position).getChecked()) {
                    items.get(position).setChecked(false);
                } else {
                    items.get(position).setChecked(true);
                }
                //notifyDataSetChanged();
                notifyItemChanged(position);
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // show deletion confirmation prompt on item long click

                //do_Dialog(position); // deletion dialog
                ((MainActivity) activity).showEditDialog(position, items, itemHash);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView checkBox;
        TextView details;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox_img);
            details = itemView.findViewById(R.id.grocery_item_details);
            parentLayout = itemView.findViewById(R.id.grocery_item);
        }
    }

}

