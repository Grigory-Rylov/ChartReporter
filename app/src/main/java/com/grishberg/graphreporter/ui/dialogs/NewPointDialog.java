package com.grishberg.graphreporter.ui.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.mvp.common.BaseMvpDialogFragment;
import com.grishberg.graphreporter.ui.view.color.HSVColorWheel;
import com.grishberg.graphreporter.utils.ColorUtil;

/**
 * Created by grishberg on 28.01.17.
 * Диалог добавления формулы
 */
public class NewPointDialog extends BaseMvpDialogFragment implements View.OnClickListener {
    public static final int POINT_UPDATE_RESULT_CODE = 1001;
    public static final int POINT_NEW_RESULT_CODE = 1002;
    public static final String ARG_FORMULA_CONTAINER = "ARG_FORMULA_CONTAINER";
    private static final String NEW_POINT_RESULT_EXTRA = NewPointDialog.class.getSimpleName();
    private EditText pointName;
    private EditText pointGrow;
    private EditText pointFall;

    private Spinner vertexSpinner;
    private CheckBox isGrowPercent;
    private CheckBox isFallPercent;
    private HSVColorWheel growColorPicker;
    private HSVColorWheel fallColorPicker;

    private Switch growColorSwitch;
    private Switch fallColorSwitch;

    private FrameLayout growColorPreview;
    private FrameLayout fallColorPreview;
    @Nullable
    private FormulaContainer formulaContainer;

    public static void showDialog(final FragmentManager fm,
                                  final Fragment targetFragment) {
        showEditDialog(null, fm, targetFragment);
    }

    public static void showEditDialog(final FormulaContainer formulaContainer,
                                      final FragmentManager fm,
                                      final Fragment targetFragment) {
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm.findFragmentByTag(NewPointDialog.class.getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DialogFragment newFragment = new NewPointDialog();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_FORMULA_CONTAINER, formulaContainer);
        newFragment.setArguments(args);
        newFragment.setTargetFragment(targetFragment,
                formulaContainer == null ? POINT_NEW_RESULT_CODE : POINT_UPDATE_RESULT_CODE);
        newFragment.show(ft, PeriodSelectDialog.class.getSimpleName());
    }

    @Nullable
    public static FormulaContainer getResult(final Intent intent) {
        if (intent != null) {
            return (FormulaContainer) intent.getSerializableExtra(NEW_POINT_RESULT_EXTRA);
        }
        return null;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.formulaContainer = (FormulaContainer) getArguments().getSerializable(ARG_FORMULA_CONTAINER);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_new_point, container, false);
        getDialog().setTitle(getString(R.string.dialog_new_point_title));
        getDialog().setCanceledOnTouchOutside(true);
        initVertexSpinner(view);
        initButton(view);
        pointName = (EditText) view.findViewById(R.id.dialog_new_point_name);
        pointGrow = (EditText) view.findViewById(R.id.dialog_new_point_grow_value);
        pointFall = (EditText) view.findViewById(R.id.dialog_new_point_fall_value);

        isGrowPercent = (CheckBox) view.findViewById(R.id.dialog_new_point_grow_percent);
        isFallPercent = (CheckBox) view.findViewById(R.id.dialog_new_point_fall_percent);

        growColorPreview = (FrameLayout) view.findViewById(R.id.dialog_new_point_grow_color);
        fallColorPreview = (FrameLayout) view.findViewById(R.id.dialog_new_point_fall_color);

        initColorPickers(view);

        initSwitchers(view);

        if (formulaContainer != null) {
            populateFormula(formulaContainer);
        }
        return view;
    }

    private void populateFormula(@NonNull final FormulaContainer formulaContainer) {
        initColors(formulaContainer.getGrowColor(), formulaContainer.getFallColor());
        pointName.setText(formulaContainer.getName());
        pointGrow.setText(String.valueOf(formulaContainer.getGrowValue()));
        pointFall.setText(String.valueOf(formulaContainer.getFallValue()));
        isGrowPercent.setChecked(formulaContainer.getIsGrowPercent());
        isFallPercent.setChecked(formulaContainer.getIsFallPercent());
    }

    private void initColorPickers(final View view) {
        growColorPicker = (HSVColorWheel) view.findViewById(R.id.dialog_new_point_grow_color_picker);
        fallColorPicker = (HSVColorWheel) view.findViewById(R.id.dialog_new_point_fall_color_picker);

        growColorPicker.setColorSelectedListener(color -> growColorPreview.setBackgroundColor(color));
        fallColorPicker.setColorSelectedListener(color -> fallColorPreview.setBackgroundColor(color));

        initColors(ColorUtil.getColor(getContext(), R.color.formula_grow_color),
                ColorUtil.getColor(getContext(), R.color.formula_fall_color));
    }

    private void initColors(final int growColor, final int fallColor) {
        growColorPicker.setColor(growColor);
        fallColorPicker.setColor(fallColor);

        growColorPreview.setBackgroundColor(growColor);
        fallColorPreview.setBackgroundColor(fallColor);
    }

    private void initSwitchers(final View view) {
        growColorSwitch = (Switch) view.findViewById(R.id.dialog_new_point_grow_color_switcher);
        fallColorSwitch = (Switch) view.findViewById(R.id.dialog_new_point_fall_color_switcher);
        growColorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked) {
                growColorPicker.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
        });

        fallColorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked) {
                fallColorPicker.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
        });
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

    private void initButton(final View view) {
        final Button okButton = (Button) view.findViewById(R.id.dialog_new_point_ok_button);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onClick(final View view) {
        if (TextUtils.isEmpty(pointName.getText())) {
            Toast.makeText(getContext(), R.string.dialog_new_point_empty_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pointGrow.getText())) {
            Toast.makeText(getContext(), R.string.dialog_new_point_empty_grow, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pointFall.getText())) {
            Toast.makeText(getContext(), R.string.dialog_new_point_empty_fall, Toast.LENGTH_SHORT).show();
            return;
        }
        final FormulaContainer newFormula = new FormulaContainer(
                pointName.getText().toString(),
                vertexSpinner.getSelectedItemPosition(),
                Double.valueOf(pointGrow.getText().toString()),
                isGrowPercent.isChecked(),
                growColorPicker.getSelectedColor(),
                Double.valueOf(pointFall.getText().toString()),
                isFallPercent.isChecked(),
                fallColorPicker.getSelectedColor());
        newFormula.setId(formulaContainer != null ? formulaContainer.getId() : null);
        newFormula.setProductId(formulaContainer != null ? formulaContainer.getProductId() : 0);

        final Intent data = new Intent();
        data.putExtra(NEW_POINT_RESULT_EXTRA,
                newFormula);
        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, data);
        dismiss();
    }
}
