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

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
                          implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Fragment frag = null;
    private Fragment frag2 = null;
    private String actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // start a RecipeSearchFragment - default view to display
            frag = new RecipeSearchFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, frag);
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

        switch (id) {
            case R.id.nav_saved_recipes:
                frag = new SavedRecipesFragment();
                getSupportActionBar().setTitle(R.string.nav_saved_recipes);
                break;
            case R.id.nav_meal_plan:
                frag = new MealPlanFragment();
                getSupportActionBar().setTitle(R.string.nav_meal_plan);
                break;
            case R.id.nav_grocery_list:
                frag = new GroceryListFragment();
                getSupportActionBar().setTitle(R.string.nav_grocery_list);
                break;
            case R.id.nav_account:
                if(user!=null){
                    frag = new SettingsFragment();
                    getSupportActionBar().setTitle(R.string.nav_account);
                } else{
                    frag = new LoginFragment();
                    getSupportActionBar().setTitle(R.string.nav_login);
                }
                break;
            case R.id.nav_about:
                frag = new AboutFragment();
                getSupportActionBar().setTitle(R.string.nav_about);
                break;
            default:
                frag = new RecipeSearchFragment();
                getSupportActionBar().setTitle(R.string.nav_search);
        }

        if (frag != null) {
            // set Fragment
            setFragment(frag);
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
        } else if(frag!=null && frag2!=null) {
            // re-attach frag on back when in frag2
            attachFragment(frag, frag2);
        } else { // normal back button function
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


    public void userSignedIn() {
        // set user and navigate to Settings
        user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle(R.string.nav_account);
        setFragment(new SettingsFragment());
    }

    public void userSignedOut(){
        // Sign Out user
        FirebaseAuth.getInstance().signOut();
        // make sure user value is updated to reflect no user signed in
        user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

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

    public void showEditDialog(){
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFragment editItemDialog = new EditItemDialogFragment();
        editItemDialog.show(fm, "fragment_edit_item");
    }


    public void newItem(HashMap<String, Double> hashMap){
        // set hashMap as a bundle to pass
        Bundle bundle = new Bundle();
        bundle.putSerializable("hashMap", hashMap);
        // Detach GroceryListFragment and add new AddItemFragment
        frag2 = new AddItemFragment();
        frag2.setArguments(bundle);
        detachFragment(frag, frag2, R.string.nav_new_grocery);
    }

    public void addItem(double quantity, String units, String name){
        GroceryItem newItem = new GroceryItem(quantity, units, name); // new GroceryItem

        // save GroceryItem in Bundle as a serialized object
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", newItem);
        // pass bundle back to grocery list
        frag.setArguments(bundle);

        // Re-attach GroceryListFragment
        attachFragment(frag, frag2);
    }

    public void cancelItem(){
        // Re-attach GroceryListFragment
        attachFragment(frag, frag2);
    }


//---------------------------------------------------------------------------
// Following three Methods deal with setting and inflating a Fragment
    private void setFragment(Fragment f){
        // Inflate Fragment f, replaces current Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, f);
        ft.commit();
    }


    private void detachFragment(Fragment f1, Fragment f2, int newTitle){
        // add Fragment f2 and detach Fragment f1
        // detaching f1 instead of removing so that data is not deleted
        // allows f1 to be re-attached instead of recreating it
        // save f1's title and set f2's title
        actionBarTitle = getSupportActionBar().getTitle().toString();
        getSupportActionBar().setTitle(newTitle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, f2);
        ft.detach(f1);
        ft.commit();
    }


    private void attachFragment(Fragment f1, Fragment f2){
        // re-attach f1 and remove f2
        // set Action Bar title back to f1's title
        getSupportActionBar().setTitle(actionBarTitle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.attach(f1);
        ft.remove(f2);
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

        // create new RecipeDetails Fragment and pass bundle
        frag2 = new RecipeDetailsFragment();
        frag2.setArguments(bundle);

        // detach RecipeSearchFragment and add/inflate RecipeDetails
        detachFragment(frag, frag2, R.string.nav_recipe_details);
    }

}
