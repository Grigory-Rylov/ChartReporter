package com.grishberg.graphreporter.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.mvp.common.BaseMvpDialogFragment;

/**
 * Created by grishberg on 16.01.17.
 * Диалог выбора периода
 */
public class PeriodSelectDialog extends BaseMvpDialogFragment implements View.OnClickListener {
    public static final int PERIOD_SELECT_RESULT_CODE = 1000;
    public static final String ARG_LAST_POSITION = "ARG_LAST_POSITION";

    private Spinner spinner;

    public static void showDialog(final FragmentManager fm,
                                  final Fragment targetFragment,
                                  final int lastPosition) {
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm
                .findFragmentByTag(PeriodSelectDialog.class.getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DialogFragment newFragment = new PeriodSelectDialog();
        final Bundle args = new Bundle();
        args.putInt(ARG_LAST_POSITION, lastPosition);
        newFragment.setArguments(args);
        newFragment.setTargetFragment(targetFragment, PERIOD_SELECT_RESULT_CODE);
        newFragment.show(ft, PeriodSelectDialog.class.getSimpleName());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
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
        spinner.setAdapter(createSpinnerAdapter());
        if(getArguments() != null){
            spinner.setSelection(getArguments().getInt(ARG_LAST_POSITION));
        }

    }

    protected BaseAdapter createSpinnerAdapter() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void onClick(final View view) {
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                spinner.getSelectedItemPosition(),
                getActivity().getIntent());
        dismiss();
    }
}
