package com.grishberg.graphreporter.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.ui.dialogs.NewPointDialog;
import com.grishberg.graphreporter.ui.fragments.FormulasListFragment;

public class FormulaSettingsActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";

    public static void startForResult(final Activity activity, final long productId, final int requestCode) {
        final Intent intent = new Intent(activity, FormulaSettingsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula_settings);

        final long productId = getIntent().getLongExtra(EXTRA_PRODUCT_ID, 0);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_formula_settings, FormulasListFragment.newInstance(productId))
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
