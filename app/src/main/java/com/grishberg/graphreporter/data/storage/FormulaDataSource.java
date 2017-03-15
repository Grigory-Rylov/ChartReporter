package com.grishberg.graphreporter.data.storage;

import com.grishberg.graphreporter.data.beans.FormulaContainer;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 15.02.17.
 * Хранилище формул
 */

public interface FormulaDataSource {
    void addFormula(long productId, FormulaContainer formulaContainer);

    void clear(long productId);

    Observable<List<FormulaContainer>> getFormulas(long productId);

    void updateFormulaVisibility(long formulaId, boolean isVisible);

    void updateFormula(FormulaContainer container);

    Observable<FormulaContainer> getFormula(long formulaId);

    void deleteFormula(FormulaContainer formulaContainer);
}
