package com.grishberg.graphreporter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.datafacade.OnItemSelectedListener;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.model.ProductItem;
import com.grishberg.graphreporter.mvp.presenter.ProductsPresenter;
import com.grishberg.graphreporter.mvp.view.ProductsView;
import com.grishberg.graphreporter.ui.adapters.ProductsListAdapter;
import com.grishberg.graphreporter.ui.fragments.CandleFragment;
import com.grishberg.graphreporter.utils.MaterialDrawerHelper;

public class ChartActivity extends MvpAppCompatActivity implements ProductsView, OnItemSelectedListener<ProductItem> {

    @InjectPresenter
    ProductsPresenter presenter;
    private ProductsListAdapter adapter;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();

        initDrawer();

        presenter.requestProducts();
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerHelper = new MaterialDrawerHelper(this, getSupportActionBar(), drawerLayout);
    }

    private void initRecyclerView() {
        adapter = new ProductsListAdapter(this);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_chart_nav_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.recycle();
    }

    @Override
    public void showProducts(final ListResultCloseable<ProductItem> productItems) {
        adapter.update(productItems);
    }

    @Override
    public void showProgress() {
        //Do nothing
    }

    @Override
    public void hideProgress() {
        //Do nothing
    }

    @Override
    public void showFail(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(final ProductItem item, final int position) {
        final Fragment fragment = CandleFragment.newInstance(item.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_chart, fragment)
                .commit();
        drawerLayout.closeDrawers();
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
        if (item.getItemId() == R.id.about){
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
}
