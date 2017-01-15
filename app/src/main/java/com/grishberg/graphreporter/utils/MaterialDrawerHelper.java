package com.grishberg.graphreporter.utils;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.grishberg.graphreporter.R;

/**
 * Created by grishberg on 16.01.17.
 */
public class MaterialDrawerHelper {
    private static final String TAG = MaterialDrawerHelper.class.getSimpleName();

    private final ActionBarDrawerToggle mDrawerToggle;

    public MaterialDrawerHelper(final Activity activity, final DrawerLayout drawerLayout) {

        mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(final View view) {
                final AppCompatActivity actionBarActivity = (AppCompatActivity) view.getContext();
                //actionBarActivity.getSupportActionBar().setTitle(mTitle);
                actionBarActivity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(final View drawerView) {
                final AppCompatActivity actionBarActivity = (AppCompatActivity) drawerView.getContext();
                //actionBarActivity.getSupportActionBar().setTitle(mDrawerTitle);
                actionBarActivity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
    }
}
