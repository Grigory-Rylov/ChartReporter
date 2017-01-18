package com.grishberg.graphreporter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.grishberg.graphreporter.BuildConfig;
import com.grishberg.graphreporter.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final TextView aboutTextView = (TextView) findViewById(R.id.about_screen_text);
        initWithText(aboutTextView);
    }

    private void initWithText(final TextView aboutTextView) {
        aboutTextView.setText(String.format(getString(R.string.about_text), BuildConfig.VERSION_NAME));
    }

    public static void start(final Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }
}
