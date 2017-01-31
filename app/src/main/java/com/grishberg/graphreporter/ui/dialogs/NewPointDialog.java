package com.grishberg.graphreporter.ui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.mvp.common.BaseMvpDialogFragment;

/**
 * Created by grishberg on 28.01.17.
 * Диалог добавления формулы
 */
public class NewPointDialog extends BaseMvpDialogFragment implements View.OnClickListener {
    public static final int NEW_POINT_RESULT_CODE = 1001;
    private static final String NEW_POINT_RESULT_EXTRA = NewPointDialog.class.getSimpleName();
    private EditText pointName;
    private EditText pointPercent;

    private Spinner vertexSpinner;
    private Spinner conditionSpinner;

    public static void showDialog(final FragmentManager fm,
                                  final Fragment targetFragment) {
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

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_new_point, container, false);
        initVertexSpinner(view);
        initConditionSpinner(view);
        initButton(view);
        pointName = (EditText) view.findViewById(R.id.dialog_new_point_name);
        pointPercent = (EditText) view.findViewById(R.id.dialog_new_point_percent);

        return view;
    }

    private void initVertexSpinner(final View view) {
        vertexSpinner = (Spinner) view.findViewById(R.id.dialog_new_point_items_spinner);
        vertexSpinner.setAdapter(createVertexSpinnerAdapter());
    }

    protected BaseAdapter createVertexSpinnerAdapter() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.vertex_name_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void initConditionSpinner(final View view) {
        conditionSpinner = (Spinner) view.findViewById(R.id.dialog_new_point_condition_spinner);
        conditionSpinner.setAdapter(createConditionSpinnerAdapter());
    }

    protected BaseAdapter createConditionSpinnerAdapter() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.condition_name_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void initButton(final View view) {
        final Button okButton = (Button) view.findViewById(R.id.dialog_new_point_ok_button);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        if (TextUtils.isEmpty(pointName.getText())) {
            return;
        }
        if (TextUtils.isEmpty(pointPercent.getText())) {
            return;
        }
        final Intent data = new Intent();
        data.putExtra(NEW_POINT_RESULT_EXTRA,
                new FormulaContainer(
                        pointName.getText().toString(),
                        FormulaContainer.VertexType.values()[vertexSpinner.getSelectedItemPosition()],
                        Double.valueOf(pointPercent.getText().toString()),
                        true,
                        conditionSpinner.getSelectedItemPosition() == 0));
        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, data);
        dismiss();
    }

    @Nullable
    public static FormulaContainer getResult(final Intent intent) {
        if (intent != null) {
            return (FormulaContainer) intent.getSerializableExtra(NEW_POINT_RESULT_EXTRA);
        }
        return null;
    }
}
