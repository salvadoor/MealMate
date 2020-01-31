package com.srg.mealmate;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.opencensus.tags.Tag;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroceryListFragment extends Fragment {
    private View view;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    private Calendar c;
    private int weeksFromCurr; // 0 value is current week, -1 is week before current, 1 is week after current, etc
    private int weeksSaved;
   // String[] items = {"2 Avacados", "6 foods", "Test Item 3"};
    private ArrayList<GroceryItem> items = new ArrayList<>();


    public GroceryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grocery_list, container, false);

        if(savedInstanceState==null){
            weeksFromCurr = 0;
            setCurrentWeek();
            // If pref is 5, there are 5//2 weeks saved before and  after the current week
            weeksSaved = (new Preferences().getWeeks()) / 2;
        }

        setListeners();
        initMockItems();
        initRecyclerView();
        /*
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.itemx, items);

        ListView lv = view.findViewById(R.id.grocery_list);
        lv.setAdapter(adapter);
        */



        return view;
    }

    private void setCurrentWeek(){
        c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        setWeek(0);
    }

    private void setWeek(int offset){
        String weekText;
        TextView tv = view.findViewById(R.id.txt_current);

        c.add(Calendar.DATE, offset);
        weekText = "Week of " + dateFormat.format(c.getTime());
        tv.setText(weekText);
    }


    private void setListeners(){
        Button btn_prev, btn_next;

        btn_prev = view.findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWeekSaved(-1)){
                    setWeek(-7);
                }
            }
        });

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWeekSaved(1)){
                    setWeek(7);
                }
            }
        });
    }

    private Boolean isWeekSaved(int direction){

        if(Math.abs(weeksFromCurr+direction) > weeksSaved ){
            Toast.makeText(getActivity(), "No more Weeks Saved", Toast.LENGTH_SHORT).show();
            return false;
        }
        weeksFromCurr+= direction;

        return true;
    }

    private void initMockItems(){
        items.add(new GroceryItem(2, "Avacados"));
        items.add(new GroceryItem(5, "Apples"));
        items.add(new GroceryItem(1, "Loaf of bread"));
        items.add(new GroceryItem(2, "lemons"));
        items.add(new GroceryItem(6, "pairs", true));
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView rv = view.findViewById(R.id.grocery_list);
        GroceryItemAdapter adapter = new GroceryItemAdapter(getActivity(), items);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rv.setLayoutManager(new RelativeLayoutManager(getActivity()));
    }

}
