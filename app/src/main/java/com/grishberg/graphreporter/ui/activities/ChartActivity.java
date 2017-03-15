package com.grishberg.graphreporter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.ui.activities.common.BaseActivity;
import com.grishberg.graphreporter.ui.fragments.CandleFragment;
import com.grishberg.graphreporter.ui.fragments.ProductsFragment;
import com.grishberg.graphreporter.utils.MaterialDrawerHelper;

public class ChartActivity extends BaseActivity implements ProductsFragment.ProductsInteractionListener, View.OnClickListener {

    private static final String TAG = ChartActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private MaterialDrawerHelper drawerHelper;

    public static void start(final Context context) {
        final Intent intent = new Intent(context, ChartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer();

        final Button candlesButton = (Button) findViewById(R.id.drawer_candles_btn);
        final Button candlesAndLinesButton = (Button) findViewById(R.id.drawer_candles_and_lines_btn);
        final Button candlesLinesButton = (Button) findViewById(R.id.drawer_lines_btn);

        candlesButton.setOnClickListener(this);
        candlesAndLinesButton.setOnClickListener(this);
        candlesLinesButton.setOnClickListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_chart, ProductsFragment.newInstance(), ProductsFragment.class.getSimpleName())
                    .commit();
        }
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerHelper = new MaterialDrawerHelper(this, getSupportActionBar(), drawerLayout);
    }

    @Override
    public void onProductSelected(@NonNull final ProductItem productItem) {
        final Fragment fragment = CandleFragment.newInstance(productItem);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_chart, fragment, CandleFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerHelper.isDrawerOpened()) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerHelper.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerHelper.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.about) {
            AboutActivity.start(this);
        }
        // This is required to make the drawer toggle work
        return drawerHelper.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onClick(final View view) {
        drawerLayout.closeDrawers();
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(CandleFragment.class.getSimpleName());
        if (fragment instanceof CandleFragment)
            switch (view.getId()) {
                case R.id.drawer_candles_btn:
                    ((CandleFragment) fragment).onCandlesModeClicked();
                    break;
                case R.id.drawer_candles_and_lines_btn:
                    ((CandleFragment) fragment).onCandlesAndLinesMode();
                    break;
                case R.id.drawer_lines_btn:
                    ((CandleFragment) fragment).onLinesModeClicked();
                    break;
                default:
                    Log.e(TAG, "onClick: unknown item clicked");
            }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == CandleFragment.FORMULA_SETTINGS_REQUEST_CODE) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(CandleFragment.class.getSimpleName());
            if (fragment instanceof CandleFragment) {
                ((CandleFragment) fragment).onFormulaSettingsClosed();
            }
        }
    }
}
