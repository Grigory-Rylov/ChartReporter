package com.grishberg.graphreporter.data.repository.settings;

/**
 * Created by grishberg on 15.02.17.
 * Хранилище настроек
 */

public interface SettingsDataSource {
    void storeChartType(int chartIndex);

    void storeShowFormulaState(boolean showFormula);

    boolean isNeedShowFormula();

    int getChartType();
}
