/*
 * "AboutFragment.java"
 * Layout:  "fragment_about.xml"
 *
 * Fragment used to show about info for app
 *
 */
package com.srg.mealmate.MainScreens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srg.mealmate.R;


public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
