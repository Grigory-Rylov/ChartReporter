package com.grishberg.graphreporter.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.grishberg.graphreporter.mvp.common.BaseMvpDialogFragment;

/**
 * Created by grishberg on 28.01.17.
 */
public class NewPointDialog extends BaseMvpDialogFragment {
    private static final String TAG = NewPointDialog.class.getSimpleName();
    public static final int NEW_POINT_RESULT_CODE = 1001;


    public static void showDialog(final FragmentManager fm,
                                  final Fragment targetFragment,
                                  final int lastPosition) {
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm
                .findFragmentByTag(NewPointDialog.class.getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DialogFragment newFragment = new NewPointDialog();
        final Bundle args = new Bundle();
        newFragment.setArguments(args);
        newFragment.setTargetFragment(targetFragment, NEW_POINT_RESULT_CODE);
        newFragment.show(ft, PeriodSelectDialog.class.getSimpleName());
    }


}
