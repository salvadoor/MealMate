
package com.srg.mealmate.SecondaryScreens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.srg.mealmate.MainActivity;
import com.srg.mealmate.R;
import com.srg.mealmate.Services.Adapters.GroceryItemAdapter;
import com.srg.mealmate.Services.Adapters.InstructionAdapter;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.Classes.Recipe;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class NewRecipeFragment extends Fragment implements IOnFocusListenable {
    private static final String TAG = "NewRecipeFragment";
    private View view;
    private GroceryItemAdapter adapt_ingredients;
    private InstructionAdapter adapt_instructions;
    private ArrayList<GroceryItem> ingredients = new ArrayList<>();
    private ArrayList<String> instructions = new ArrayList<>();

    private int PICK_IMAGE_REQUEST = 111;
    private ImageView iv;
    Uri filePath;
    private String uploadPath;
    private String downloadPath;
    private String id;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public NewRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_recipe, container, false);

        iv = view.findViewById(R.id.upload_img);
        init_RecyclerViews();
        init_OnClickListeners();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                //Setting image to ImageView
                iv.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        Button btn_add, btn_choose_img;
        ImageView btn_new_ingredient, btn_new_instruction;

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

               // writeToDB();
            }
        });

        btn_choose_img = view.findViewById(R.id.btn_choose_img);
        btn_choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
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


    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){
        if(filePath==null) { // use default image if no image uploaded
            uploadPath = "image_48.png";
            getImageDownload();
          return;
        }

        uploadPath = getUploadPath();
        StorageReference uploadRef = storageRef.child(uploadPath);

        UploadTask uploadTask = uploadRef.putFile(filePath);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Image uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Image upload failed");
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                getImageDownload();
            }
        });

    }

    // set the upload path for the image
    private String getUploadPath(){
        StringBuilder refString = new StringBuilder();
        refString.append(user.getUid())
                .append("_images/")
                .append(filePath.getLastPathSegment());
        Log.d(TAG, "firebase path: " + refString.toString());

        return refString.toString();
    }


    private void writeToDB(){
        Recipe recipe = createRecipe();

        if(recipe == null){
            return;
        }
        Log.d(TAG, recipe.toString());

        db.collection("recipes")
                .document(id)
                .set(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "document successfully written");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "document wrute failed");
            }
        });

    }

    private Recipe createRecipe(){
        EditText et = view.findViewById(R.id.edit_name);
        String name = et.getText().toString();

        if(name.trim().length()==0 || name.length() > 50){
            Toast.makeText(getActivity(),
                    "Invalid name",
                    Toast.LENGTH_SHORT).show();
            return null;
        }

        ArrayList<HashMap> hashIngredients = new ArrayList<>();
        for(GroceryItem item : ingredients){
            hashIngredients.add(item.getHash());
        }

        id = db.collection("recipes").document().getId();

        Recipe recipe = new Recipe(name, user.getEmail(), id, hashIngredients, "", instructions, downloadPath);

        return recipe;
    }

    private void getImageDownload(){

        storageRef.child(uploadPath)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadPath = uri.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                writeToDB();
            }
        });

    }

}
