/*
 * "MainActivity.java"
 *
 * Main Activity that sets the Navigation Drawer
 * draws all other screens for application via Fragments
 * 
 *
 * Last Modified: 02.12.2020 05:25pm
 */
package com.srg.mealmate;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.srg.mealmate.Dialogs.AddIngredientsDialog;
import com.srg.mealmate.Dialogs.AddInstructionDialog;
import com.srg.mealmate.Dialogs.AddPriceDialog;
import com.srg.mealmate.Dialogs.ReauthenticateDialog;
import com.srg.mealmate.MainScreens.AboutFragment;
import com.srg.mealmate.Dialogs.AddFolderDialog;
import com.srg.mealmate.Dialogs.AddItemDialog;
import com.srg.mealmate.Dialogs.AddMealDialog;
import com.srg.mealmate.Dialogs.EditFolderDialog;
import com.srg.mealmate.Dialogs.EditItemDialog;
import com.srg.mealmate.Dialogs.SaveRecipeDialog;
import com.srg.mealmate.MainScreens.GroceryListFragment;
import com.srg.mealmate.MainScreens.LoginFragment;
import com.srg.mealmate.MainScreens.MealPlanFragment;
import com.srg.mealmate.MainScreens.RecipeSearchFragment;
import com.srg.mealmate.MainScreens.SavedFoldersFragment;
import com.srg.mealmate.MainScreens.SettingsFragment;
import com.srg.mealmate.SecondaryScreens.NewRecipeFragment;
import com.srg.mealmate.SecondaryScreens.NutritionFragment;
import com.srg.mealmate.SecondaryScreens.RecipeDetailsFragment;
import com.srg.mealmate.SecondaryScreens.SavedRecipesFragment;
import com.srg.mealmate.Services.Classes.GroceryItem;
import com.srg.mealmate.Services.Classes.Recipe;
// import com.srg.mealmate.Services.Classes.Recipe1;
import com.srg.mealmate.Services.IOnFocusListenable;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity
                          implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Fragment> frags = new ArrayList<>();
    private Fragment frag = null;
    private Fragment frag2 = null;
    private Fragment frag3 = null;
    private String actionBarTitle;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // start a RecipeSearchFragment - default view to display
            //------------------------------------------------frag = new RecipeSearchFragment();
            frags.add(new RecipeSearchFragment());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, frags.get(0));
            ft.commit();
        }

        setContentView(R.layout.activity_main);

        // set Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.nav_search);

        // set drawer toggle and register activity as a listener for navigation view
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer){
                    public void onDrawerOpened(View drawerView){
                        super.onDrawerOpened(drawerView);
                        setEmailHeader(user!=null);
                    }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // change Action Bar title, and navigate to new Fragment on item press
        int id = item.getItemId();
        // Intent intent = null;
        frags = new ArrayList<>();

        switch (id) {
            case R.id.nav_saved_recipes:
                //--------------------frag = new SavedFoldersFragment();
                frags.add(new SavedFoldersFragment());
                getSupportActionBar().setTitle(R.string.nav_saved_recipes);
                break;
            case R.id.nav_meal_plan:
                //-------------frag = new MealPlanFragment();
                frags.add(new MealPlanFragment());
                getSupportActionBar().setTitle(R.string.nav_meal_plan);
                break;
            case R.id.nav_grocery_list:
                //-------------frag = new GroceryListFragment();
                frags.add(new GroceryListFragment());
                getSupportActionBar().setTitle(R.string.nav_grocery_list);
                break;
            case R.id.nav_account:
                if(user!=null){
                    //-------------frag = new SettingsFragment();
                    frags.add(new SettingsFragment());
                    getSupportActionBar().setTitle(R.string.nav_account);
                } else{
                    //----------------frag = new LoginFragment();
                    frags.add(new LoginFragment());
                    getSupportActionBar().setTitle(R.string.nav_login);
                }
                break;
            case R.id.nav_about:
                //-----------frag = new AboutFragment();
                frags.add(new AboutFragment());
                getSupportActionBar().setTitle(R.string.nav_about);
                break;
            default:
                //--------------frag = new RecipeSearchFragment();
                frags.add(new RecipeSearchFragment());
                getSupportActionBar().setTitle(R.string.nav_search);
        }

        if (frags.get(0) != null) {
            // set Fragment
            setFragment(frags.get(0));
            //frag2 = null;
            //frag3 = null;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // close nav drawer on back
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(frags.size()>1){
            int last = frags.size() - 1;
            attachFragment();
        }
        /*
        else if(frag!=null && frag2!=null) {
            // re-attach frag on back when in frag2
            attachFragment(frag, frag2);
            if(frag3!=null){
                // if there are 3 fragments, set current Fragment
                    // as frag2 and frag as frag3 then discard frag3
                frag2 = frag;
                frag = frag3;
                frag3 = null;
            } else{
                frag2 = null;
            }

        }
        */
        else { // normal back button function
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        // Override method to auto-hide soft keyboard
        // auto-hide when focus is shifted away from an EditText
        // if screen touch/click
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            // if an EditText is already in focus
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*
        if(frag2!=null){
            if(frag2 instanceof IOnFocusListenable) {
                ((IOnFocusListenable) frag2).onWindowFocusChanged(hasFocus);
            }
        } else if(frag instanceof IOnFocusListenable) {
            ((IOnFocusListenable) frag).onWindowFocusChanged(hasFocus);
        }

         */
        if(frags.size()>0) {
            if (frags.get(frags.size() - 1) instanceof IOnFocusListenable) {
                ((IOnFocusListenable) frags.get(frags.size() - 1)).onWindowFocusChanged(hasFocus);
            }
        }
    }


    public void userSignedIn(){
        // set user and navigate to Settings
        user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle(R.string.nav_account);
        setFragment(new SettingsFragment());
    }


    public void userSignedOut(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            // Sign Out user
            FirebaseAuth.getInstance().signOut();
            // make sure user value is updated to reflect no user signed in
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        // navigate back to Login
        getSupportActionBar().setTitle(R.string.nav_login);
        setFragment(new LoginFragment());
    }


    private void setEmailHeader(boolean signedIn){
        // Set Email header for user
        TextView user_email = findViewById(R.id.header_email);

        if(signedIn) { // set user's email as header
            user_email.setText(user.getEmail());
        } else{ // set login/register instructions as header if no user
            user_email.setText(R.string.email_holder);
        }
    }

// create instance of EditItemDialogFragment and show it
    public void showEditDialog(int position, ArrayList<GroceryItem> items, HashMap itemHash){
        // create new EditItemDialogFragment and pass data, show DialogFragment
        // pass item position in arraylist and arraylist of items
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editItemDialog = EditItemDialog.newInstance(position, items, itemHash);


        editItemDialog.show(fm, "fragment_edit_item_dialog");
    }

    // create instance of AddItemDialogFragment and show it
    public void newItem(/*HashMap<String, Double> hashMap, */ ArrayList<GroceryItem> items, HashMap itemHash){
        // create new AddItemDialogFragment and pass data, show DialogFragment
        // pass hashmap of items in grocery list
        FragmentManager fm = getSupportFragmentManager();
        AddItemDialog addItemDialog = AddItemDialog.newInstance(/*hashMap,*/ items, itemHash);

        addItemDialog.show(fm, "fragment_add_item_dialog");
    }

    // create instance of AddFolderDialogFragment and show it
    public void newFolder(ArrayList<String> folders){
        FragmentManager fm = getSupportFragmentManager();
        AddFolderDialog addFolderDialog = AddFolderDialog.newInstance(folders);

        addFolderDialog.show(fm, "fragment_add_folder_dialog");
    }

    // create instance of SaveRecipeDialogFragment and show it
    public void saveRecipe(String id){
        FragmentManager fm = getSupportFragmentManager();
        SaveRecipeDialog saveRecipeDialog = SaveRecipeDialog.newInstance(id);

        saveRecipeDialog.show(fm, "fragment_save_recipe_dialog");
    }

// create instance of EditFolderDialogFragment and show it
    public void showEditFolderDialog(int index, ArrayList<String> folders){
        FragmentManager fm = getSupportFragmentManager();
        EditFolderDialog editFolderDialog = EditFolderDialog.newInstance(index, folders);

        editFolderDialog.show(fm, "fragment_edit_folder_dialog");
    }

// create instance of EditFolderDialogFragment and show it
    public void showAddMealDialog(Recipe recipe){
        FragmentManager fm = getSupportFragmentManager();
        AddMealDialog addMealDialog = AddMealDialog.newInstance(recipe);

        addMealDialog.show(fm, "fragment_add_meal_dialog");
    }

// create instance of AddIngredientsDialogFragment and show it
    public void addIngredients(ArrayList<GroceryItem> ingredients, String sunDate){
        FragmentManager fm = getSupportFragmentManager();
        AddIngredientsDialog addIngredientsDialog;
        addIngredientsDialog = AddIngredientsDialog.newInstance(ingredients, sunDate);

        addIngredientsDialog.show(fm, "fragment_add_ingredients_dialog");
    }

// create instance of AddIngredientsDialogFragment and show it
    public void userDelete(){
        FragmentManager fm = getSupportFragmentManager();
        ReauthenticateDialog reauthDialog = new ReauthenticateDialog();

        reauthDialog.show(fm, "fragment_reauth_dialog");
    }

// create instance of AddPriceDialog and show it
    public void addGroceryPrice(GroceryItem grocery){
        FragmentManager fm = getSupportFragmentManager();
        AddPriceDialog addPriceDialog = AddPriceDialog.newInstance(grocery);

        addPriceDialog.show(fm, "fragment_add_price_dialog");
    }

// create instance of AddInstructionDialog and show it
    public void newInstruction(ArrayList<String> instructions){
        // create new AddInstructionDialog and pass data, show the DialogFragment
        FragmentManager fm = getSupportFragmentManager();
        AddInstructionDialog addInstructionDialog = AddInstructionDialog.newInstance(instructions);

        addInstructionDialog.show(fm, "fragment_add_instruction_dialog");
    }

//---------------------------------------------------------------------------
// Following three Methods deal with setting and inflating a Fragment
    private void setFragment(Fragment f){
        // Inflate Fragment f, replaces current Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, f);

        ft.commit();
    }


    private void detachFragment(int newTitle){
        Log.d(TAG, "detaching fragment");
        // add Fragment f2 and detach Fragment f1
        // detaching f1 instead of removing so that data is not deleted
        // allows f1 to be re-attached instead of recreating it
        // save f1's title and set f2's title
        actionBarTitle = getSupportActionBar().getTitle().toString();
        getSupportActionBar().setTitle(newTitle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, frags.get(frags.size()-1));
        ft.detach(frags.get(frags.size()-2));
        ft.commit();
    }


    public void attachFragment(){
        Log.d(TAG, "re-attaching fragment");
        // re-attach f1 and remove f2
        // set Action Bar title back to f1's title
        getSupportActionBar().setTitle(actionBarTitle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.attach(frags.get(frags.size()-2));
        ft.remove(frags.get(frags.size()-1));
        frags.remove(frags.size()-1);
        ft.commit();
    }
//---------------------------------------------------------------------------


    public void viewRecipeDetails(Recipe recipe){
        /* pass Recipe to RecipeDetailsFragment and inflate
           Method is called by several Fragments:
                RecipeSearch
                SavedRecipes
                MealPlan
         */
        // put recipe in a bundle as a serialized object
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);

        frags.add(new RecipeDetailsFragment());
        frags.get(frags.size()-1).setArguments(bundle);
        // detach RecipeSearchFragment and add/inflate RecipeDetails
        detachFragment(R.string.nav_recipe_details);
    }


    public void viewSavedRecipes(String folderName){
        // put folderName in a bundle
        Bundle bundle = new Bundle();
        bundle.putString("folder", folderName);

        frags.add(new SavedRecipesFragment());
        frags.get(frags.size()-1).setArguments(bundle);

        //detach SavedFoldersFragment and inflate SavedRecipesFragment
        detachFragment(R.string.nav_view_recipes);
    }


    public void newRecipe(){
        frags.add(new NewRecipeFragment());

        detachFragment(R.string.btn_new_recipe);
    }


    public void viewNutrition(HashMap<String, Double> nutrition){
        Bundle bundle = new Bundle();
        bundle.putSerializable("nutrition", nutrition);

        frags.add(new NutritionFragment());
        frags.get(frags.size()-1).setArguments(bundle);

        detachFragment(R.string.nav_nutrition);
    }

}


