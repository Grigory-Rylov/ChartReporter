package com.grishberg.graphreporter.data.storage;

import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.data.beans.FormulaContainerDao;

import java.util.List;

import rx.Observable;

/**
 * Created by grishberg on 16.02.17.
 */
public class FormulaDataSourceImpl implements FormulaDataSource {
    private final FormulaContainerDao dao;

    public FormulaDataSourceImpl(final FormulaContainerDao dao) {
        this.dao = dao;
    }

    @Override
    public void addFormula(final long productId, final FormulaContainer formulaContainer) {
        formulaContainer.setProductId(productId);
        dao.insertOrReplace(formulaContainer);
    }

    @Override
    public void updateFormulaVisibility(final long formulaId, final boolean isVisible) {
        final FormulaContainer formulaContainer = dao.load(formulaId);
        if (formulaContainer != null) {
            formulaContainer.setIsVisible(isVisible);
            updateFormula(formulaContainer);
        }
    }

    @Override
    public void clear(final long productId) {
        final List<FormulaContainer> formulaContainers = getList(productId);
        dao.deleteInTx(formulaContainers);
    }

    @Override
    public Observable<List<FormulaContainer>> getFormulas(final long productId) {
        return Observable.just(
                getList(productId));
    }

    private List<FormulaContainer> getList(final long productId) {
        return dao.queryBuilder()
                .where(FormulaContainerDao.Properties.ProductId.eq(productId))
                .orderAsc(FormulaContainerDao.Properties.Name)
                .build().list();
    }

    @Override
    public void updateFormula(final FormulaContainer container) {
        dao.insertOrReplace(container);
    }

    @Override
    public Observable<FormulaContainer> getFormula(final long formulaId) {
        return Observable.just(dao.load(formulaId));
    }

    @Override
    public void deleteFormula(final FormulaContainer formulaContainer) {
        dao.delete(formulaContainer);
    }
}
