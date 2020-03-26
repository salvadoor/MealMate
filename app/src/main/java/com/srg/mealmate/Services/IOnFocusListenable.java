/*
 * "IOnFocusListenable.java"
 * interface for using onWindowFocusChanged in Fragments
 *
 * implemented in fragments that use DialogFragments
 * implementations are similar and used to refresh recyclerview and save data in the respective fragment
 *
 * Last Modified: 03.26.2020 02:13pm
 */
package com.srg.mealmate.Services;

public interface IOnFocusListenable {
    public void onWindowFocusChanged(boolean hasFocus);
}
