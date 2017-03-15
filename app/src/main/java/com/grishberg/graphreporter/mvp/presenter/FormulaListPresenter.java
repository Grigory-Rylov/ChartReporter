package com.grishberg.graphreporter.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.data.storage.FormulaDataSource;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.common.BasePresenter;
import com.grishberg.graphreporter.mvp.view.FormulasListView;

import javax.inject.Inject;

/**
 * Created by grishberg on 18.02.17.
 */
@InjectViewState
public class FormulaListPresenter extends BasePresenter<FormulasListView> {

    @Inject
    FormulaDataSource formulaStorage;

    private long productId;

    public FormulaListPresenter() {
        DiManager.getAppComponent().inject(this);
    }

    public void onInitScreen(final long productId) {
        this.productId = productId;
        formulaStorage.getFormulas(this.productId)
                .subscribe(formulaList -> getViewState().showFormulasList(formulaList));
    }

    public void onFormulaVisibleStateChanged(final long formulaId, final boolean isVisible) {
        formulaStorage.updateFormulaVisibility(formulaId, isVisible);
        onInitScreen(this.productId);
    }

    public void onFormulaItemSelected(final FormulaContainer formulaContainer) {
        getViewState().showFormulaDescription(formulaContainer);
    }

    public void saveFormula(final FormulaContainer formulaContainer) {
        formulaStorage.updateFormula(formulaContainer);
    }

    public void onFormulaDeleteClicked(final FormulaContainer formulaContainer) {
        formulaStorage.deleteFormula(formulaContainer);
        onInitScreen(this.productId);
    }

    public void addNewFormula(final FormulaContainer formulaContainer) {
        formulaStorage.addFormula(productId, formulaContainer);
    }
}
