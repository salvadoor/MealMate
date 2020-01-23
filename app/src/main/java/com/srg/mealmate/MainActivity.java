package com.srg.mealmate;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
                          implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // start a RecipeSearchFragment
            Fragment f = new RecipeSearchFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, f);
            ft.commit();
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        int id = item.getItemId();
        Fragment frag = null;
        Intent intent = null;

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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag);
            ft.commit();
        } else {
            // start activity
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void userSignedIn() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle(R.string.nav_account);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new SettingsFragment());
        ft.commit();
    }

    public void userSignedOut(){
        FirebaseAuth.getInstance().signOut();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle(R.string.nav_login);
        FragmentTransaction ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new LoginFragment());
        ft.commit();
    }

    private void setEmailHeader(boolean signedIn){
        TextView user_email = findViewById(R.id.header_email);

        if(signedIn) {
            user_email.setText(user.getEmail());
        } else{
            user_email.setText(R.string.email_holder);
        }
    }

}
