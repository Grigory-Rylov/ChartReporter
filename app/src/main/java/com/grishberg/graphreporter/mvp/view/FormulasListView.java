package com.grishberg.graphreporter.mvp.view;

import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.mvp.common.BaseView;

import java.util.List;

/**
 * Created by grishberg on 18.02.17.
 * Представление для списка формул
 */

public interface FormulasListView extends BaseView {
    void showFormulasList(List<FormulaContainer> formulaContainers);

    void showFormulaDescription(FormulaContainer formulaContainer);
}
