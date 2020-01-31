package com.srg.mealmate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    private static final String TAG = "GroceryItemAdapter";

    private ArrayList<GroceryItem> items = new ArrayList<>();
    private Context mContext;

    public GroceryItemAdapter(Context context, ArrayList<GroceryItem> items) {
        this.items = items;
        this.mContext = context;
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
        Log.d(TAG,"onBindViewHolder:called");
        Boolean checked = items.get(position).getChecked();

        if(checked){
            holder.checkBox.setImageResource(R.drawable.checked_box);
        } else{
            holder.checkBox.setImageResource(R.drawable.checkbox_outline);
        }

        holder.quantity.setText(items.get(position).getQuantityString());
        holder.name.setText(items.get(position).getItem());

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick:clicked on: " + items.get(position).getItem());

                Toast.makeText(mContext, items.get(position).getItem(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView checkBox;
        TextView quantity;
        TextView name;
        LinearLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox_img);
            quantity = itemView.findViewById(R.id.item_quantity);
            name = itemView.findViewById(R.id.item_name);
            parentLayout = itemView.findViewById(R.id.grocery_item);
        }
    }
}

