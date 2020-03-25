package com.srg.mealmate.Services.Adapters;

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

import java.util.ArrayList;

public class RecipeFolderAdapter extends RecyclerView.Adapter<RecipeFolderAdapter.ViewHolder> {
    private static final String TAG = "RecipeFolderAdapter";
    private ArrayList<String> folders;
    private Context mContext;


    public RecipeFolderAdapter(ArrayList<String> folders, Context mContext) {
        this.folders = folders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_folder, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder:called");

        holder.folderName.setText(folders.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick:clicked on: " + items.get(position).getName());

                // go to view of all recipe in folder
                // loadSavedRecipes(folders.get(position));
                ((MainActivity) mContext).viewSavedRecipes(folders.get(position));
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ((MainActivity)mContext).showEditFolderDialog(position, folders);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView folderIcon;
        TextView folderName;
        LinearLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);

            folderIcon = itemView.findViewById(R.id.folder_icon);
            folderName = itemView.findViewById(R.id.folder_name);
            parentLayout = itemView.findViewById(R.id.recipe_folder);
        }
    }
}