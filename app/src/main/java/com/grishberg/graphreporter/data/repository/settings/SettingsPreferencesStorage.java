package com.grishberg.graphreporter.data.repository.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by grishberg on 15.02.17.
 * Реализация хранилища настроек
 */
public class SettingsPreferencesStorage implements SettingsDataSource {
    private static final String PREF_CURRENT_CHART = "pref_current_chart";
    private static final String PREF_SHOW_FORMULA_STATE = "pref_show_formula_state";
    private static final int DEF_INT_VALUE = 0;
    private static final boolean DEF_BOOLEAN_VALUE = false;
    private final SharedPreferences preferences;

    public SettingsPreferencesStorage(final Context context) {
        preferences = context.getSharedPreferences("mainPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public void storeChartType(final int chartIndex) {
        putInt(PREF_CURRENT_CHART, chartIndex);
    }

    @Override
    public int getChartType() {
        return getInt(PREF_CURRENT_CHART);
    }

    @Override
    public void storeShowFormulaState(final boolean showFormula) {
        putBoolean(PREF_SHOW_FORMULA_STATE, showFormula);
    }

    @Override
    public boolean isNeedShowFormula() {
        return getBoolean(PREF_SHOW_FORMULA_STATE);
    }

    private void putInt(final String name, final int val) {
        final SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(name, val);
        ed.apply();
    }

    private int getInt(final String name) {
        return preferences.getInt(name, DEF_INT_VALUE);
    }

    private void putBoolean(final String name, final boolean val) {
        final SharedPreferences.Editor ed = preferences.edit();
        ed.putBoolean(name, val);
        ed.apply();
    }

    private boolean getBoolean(final String name) {
        return preferences.getBoolean(name, DEF_BOOLEAN_VALUE);
    }
}
