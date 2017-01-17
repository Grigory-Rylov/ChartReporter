package com.grishberg.graphreporter.mvp.common;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.arellomobile.mvp.MvpDelegate;

/**
 * Created by grishberg on 16.01.17.
 */
public class BaseMvpDialogFragment extends DialogFragment {
    private MvpDelegate<? extends BaseMvpDialogFragment> mMvpDelegate;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        getMvpDelegate().onAttach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getMvpDelegate().onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getMvpDelegate().onDestroy();
    }

    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        getMvpDelegate().onSaveInstanceState(outState);
    }

    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }

        return mMvpDelegate;
    }
}
