package com.grishberg.graphreporter.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.mvp.common.BaseMvpDialogFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by grishberg on 16.01.17.
 * Диалог выбора периода
 */
public class PeriodSelectDialog extends BaseMvpDialogFragment implements View.OnClickListener {
    public static final int PERIOD_SELECT_RESULT_CODE = 1000;
    public static final int PERIOD_DAILY = 0;
    public static final int PERIOD_WEEK = 1;
    public static final int PERIOD_HALF_YEAR = 2;
    public static final int PERIOD_YEAR = 3;
    private Spinner spinner;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            PERIOD_DAILY,
            PERIOD_WEEK,
            PERIOD_HALF_YEAR,
            PERIOD_YEAR
    })
    public @interface Period {
    }

    public static void showDialog(final FragmentManager fm, final Fragment targetFragment) {
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm
                .findFragmentByTag(PeriodSelectDialog.class.getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        final DialogFragment newFragment = new PeriodSelectDialog();
        newFragment.setTargetFragment(targetFragment, PERIOD_SELECT_RESULT_CODE);
        newFragment.show(ft, PeriodSelectDialog.class.getSimpleName());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.dialog_select_daily_period, container, false);
        initSpinner(view);
        initButton(view);
        return view;
    }

    private void initButton(final View view) {
        final Button okButton = (Button) view.findViewById(R.id.dialog_select_daily_period_items_ok_button);
        okButton.setOnClickListener(this);
    }

    private void initSpinner(final View view) {
        spinner = (Spinner) view.findViewById(R.id.dialog_select_daily_period_items_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(final View view) {
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                spinner.getSelectedItemPosition(),
                getActivity().getIntent());
        dismiss();
    }
}
