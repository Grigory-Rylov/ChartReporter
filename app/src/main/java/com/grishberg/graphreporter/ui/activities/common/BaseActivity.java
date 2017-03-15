package com.grishberg.graphreporter.ui.activities.common;

import android.content.Intent;

import com.arellomobile.mvp.MvpAppCompatActivity;

/**
 * Created by grishberg on 23.02.17.
 * base activity with solving memory leak https://medium.com/@amitshekhar/android-memory-leaks-inputmethodmanager-solved-a6f2fe1d1348#.86p05h1fn
 */
public class BaseActivity extends MvpAppCompatActivity {
    @Override
    protected void onDestroy() {
        startActivity(new Intent(this, DummyActivity.class));
        super.onDestroy();
    }
}
