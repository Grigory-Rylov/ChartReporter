package com.grishberg.graphreporter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.mvp.presenter.AuthPresenter;
import com.grishberg.graphreporter.mvp.view.AuthView;

public class AuthActivity extends MvpAppCompatActivity implements AuthView, View.OnClickListener {

    @InjectPresenter
    AuthPresenter presenter;

    private Button buttonAuth;
    private EditText loginEdit;
    private EditText passwordEdit;
    private ProgressBar progressBar;

    public static void start(final Context context) {

        context.startActivity(new Intent(context, AuthActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonAuth = (Button) findViewById(R.id.auth_screen_button_enter);
        buttonAuth.setOnClickListener(this);
        loginEdit = (EditText) findViewById(R.id.auth_screen_login);
        passwordEdit = (EditText) findViewById(R.id.auth_screen_password);
        progressBar = (ProgressBar) findViewById(R.id.auth_screen_progress);
    }

    @Override
    public void showLoginEmptyError() {

    }

    @Override
    public void showPasswordEmptyError() {

    }

    @Override
    public void showProgress() {
        setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        setEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess() {
        ChartActivity.start(this);
    }

    @Override
    public void showWrongCredentials() {

    }

    @Override
    public void showFail(String message) {

    }

    @Override
    public void onClick(View view) {
        presenter.auth(loginEdit.getText().toString(), passwordEdit.getText());
    }

    private void setEnabled(final boolean enabled) {
        loginEdit.setEnabled(enabled);
        passwordEdit.setEnabled(enabled);
        buttonAuth.setEnabled(enabled);
    }
}
