package com.grishberg.graphreporter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.mvp.presenter.AuthPresenter;
import com.grishberg.graphreporter.mvp.view.AuthView;

public class AuthActivity extends MvpAppCompatActivity implements AuthView, View.OnClickListener, TextView.OnEditorActionListener {

    @InjectPresenter
    AuthPresenter presenter;

    private Button buttonAuth;
    private EditText loginEdit;
    private EditText passwordEdit;
    private ProgressBar progressBar;

    public static void start(final Context context) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonAuth = (Button) findViewById(R.id.auth_screen_button_enter);
        buttonAuth.setOnClickListener(this);
        loginEdit = (EditText) findViewById(R.id.auth_screen_login);
        passwordEdit = (EditText) findViewById(R.id.auth_screen_password);
        progressBar = (ProgressBar) findViewById(R.id.auth_screen_progress);
        passwordEdit.setOnEditorActionListener(this);
        setEnabled(true);
    }

    @Override
    public void showLoginEmptyError() {
        Toast.makeText(this, R.string.auth_empty_login, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordEmptyError() {
        Toast.makeText(this, R.string.auth_empty_password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess() {
        ChartActivity.start(this);
        finish();
    }

    @Override
    public void showWrongCredentials() {
        Toast.makeText(this, R.string.auth_wrong_credentials, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFail(final String message) {
        Toast.makeText(this, R.string.auth_network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(final View view) {
        presenter.auth(loginEdit.getText().toString(), passwordEdit.getText());
    }

    private void setEnabled(final boolean enabled) {
        loginEdit.setEnabled(enabled);
        passwordEdit.setEnabled(enabled);
        buttonAuth.setEnabled(enabled);
    }

    @Override
    public boolean onEditorAction(final TextView textView, final int i, final KeyEvent keyEvent) {
        onClick(textView);
        return false;
    }
}
