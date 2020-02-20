/*
 * "GroceryListFragment.java"
 * Layout:  "fragment_grocery_list.xml"
 *
 * Fragment used to manage grocery lists
 * Allow navigation of grocery lists by week
 * use ArrayList of GroceryItem objects for lists
 * use Calendar to keep track of weeks
 *
 * Last Modified: 02.12.2020 05:25pm
 */
package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class GroceryListFragment extends Fragment {
    private static final String TAG = "GroceryListFragment";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
    private View view;
    private Calendar c;
    private int weeksFromCurr; // 0 value is current week, -1 is week before current, 1 is week after current, etc
    private int weeksSaved;
    private ArrayList<GroceryItem> items = new ArrayList<>();
    private Boolean dataStored = false; //initial false,  true when fragment is not destroyed and then re-inflated
    private HashMap<String, Double> itemHash;
    private GroceryItemAdapter adapter;


    public GroceryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get any arguments passed via a bundle(GroceryItem)
        Bundle bundle = getArguments();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grocery_list, container, false);

        // initialize important components
        init_weekData();
        init_NavClickListeners();
        init_OnClickListeners();
        init_FocusChangeListener();

        dataStored = true;

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save Grocery List(s)
        saveGroceryList();
    }


    private void init_FocusChangeListener(){
        // listener used to update recyclerview after an EditItem or AddItem dialog fragment is closed
        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(final boolean hasFocus) {
                if(hasFocus) {
                    Log.d(TAG, "has Focus: true");

                    Bundle bundle = getArguments();

                    if(bundle!=null && bundle.getSerializable("item")!=null){
                        items.add((GroceryItem) bundle.getSerializable("item"));
                        bundle.remove("item");
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void init_RecyclerView(){
        // create RecyclerView
        Log.d(TAG, "initRecyclerView: init rv");
        RecyclerView rv = view.findViewById(R.id.grocery_list);
        adapter = new GroceryItemAdapter(getActivity(), getActivity(), items);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void initMockItems(){
        // For initial Testing and Development, populate grocery list
        items.add(new GroceryItem(2, "whole", "Avacado"));
        items.add(new GroceryItem(5, "whole", "Apple"));
        items.add(new GroceryItem(1, "whole", "Loaf of Bread"));
        items.add(new GroceryItem(2, "whole", "Lemon"));
    }

//----------------------------------------------------------------------------------
// Methods to deal with setting the week and retrieving corresponding list
    private void init_weekData(){
        // If pref is 5, there are 5//2 weeks saved before and  after the current week
        weeksSaved = (new Preferences().getWeeks()) / 2;

        if(dataStored){
            // if dataStored == true, then variables such as the c Calendar object already have defined values
            //set week based on current c, week will be last viewed week before fragment was detached
            setWeek(0);

        } else{
            // else initialize week tracking variables and set current week
            weeksFromCurr = 0;
            setCurrentWeek();
        }
    }


    private void setCurrentWeek(){
        // set Initial Calendar and then set current week
        c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // All dates will reference a Sunday
        setWeek(0);
    }


    private void setLastViewedWeek(String date){
        // set week based on formatted date string ("MM-dd")
        try {
            c.setTime(dateFormat.parse(date));
        } catch (ParseException e){
            e.printStackTrace();
        }
        setWeek(0);
    }


    private void setWeek(int offset){
        String weekText;
        TextView tv = view.findViewById(R.id.txt_current);
        // save current Grocery List if it exists
        if(items!=null) {
            saveGroceryList();
        }
        // change week, ex: go back a week if offset = -7
        c.add(Calendar.DATE, offset);
        weekText = "Week of " + dateFormat.format(c.getTime());
        tv.setText(weekText);

        // Load corresponding Grocery List
        loadGroceryList(dateFormat.format(c.getTime()));
    }


    private Boolean isWeekSaved(int direction){
        // Check if week navigation will go out of bounds, return boolean
        // if not out of bounds, update weeksFromCurr and return true
        if(Math.abs(weeksFromCurr+direction) > weeksSaved ){
            Toast.makeText(getActivity(), "No more Weeks Saved", Toast.LENGTH_SHORT).show();
            return false;
        }
        weeksFromCurr+= direction;

        return true;
    }

//----------------------------------------------------------------------------------
// Methods to set OnClickListeners for buttons
    private void init_NavClickListeners(){
        // set OnClickListeners for prev and next buttons
        // These buttons will change the week being viewed
        Button btn_prev, btn_next;

        btn_prev = view.findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWeekSaved(-1)){
                    setWeek(-7); // Go to prev week, -7 days
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWeekSaved(1)){
                    setWeek(7); // Go to next week, +7 days
                }
            }
        });
    }


    private void init_OnClickListeners(){
        // OnClickListener for 'button' to add new item
        ImageView btn_new_item;

        btn_new_item = view.findViewById(R.id.btn_new_item);
        btn_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call newItem() from MainActivity
                ((MainActivity)getActivity()).newItem(itemHash);

            }
        });
    }


//----------------------------------------------------------------------------------
// Methods to deal with reading and writing data using the GroceryListFile class
    private void loadGroceryList(String sundayDate) {
        // get file that corresponds to current week
        GroceryListFile.setFilename(sundayDate);
        // retrieve list
        items = GroceryListFile.readList(getActivity());


        //create HashMap
        init_hashMap();
        // start RecyclerView
        init_RecyclerView();

    }


    private void saveGroceryList(){
        // save current Grocery List
        GroceryListFile.writeList(items, getActivity());
    }


    private void init_hashMap(){
        itemHash = new HashMap<>();

        for(int i=0; i < items.size();i++){
            Log.d(TAG,"item count" + items.size());
            itemHash.put(items.get(i).getName(), items.get(i).getQuantity());
        }
    }


    private void remove_hashPair(){

    }

    public void dataChanged(){
        adapter.notifyDataSetChanged();
    }

}
