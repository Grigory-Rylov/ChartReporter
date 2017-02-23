package com.grishberg.graphreporter.ui.activities.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by amitshekhar on 06/05/16.
 * memory leak solution
 */
public class DummyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> finish(), 500);
    }
}
