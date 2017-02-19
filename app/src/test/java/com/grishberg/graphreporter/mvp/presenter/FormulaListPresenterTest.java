package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.data.storage.FormulaDataSource;
import com.grishberg.graphreporter.mvp.view.FormulasListView;
import com.grishberg.graphreporter.utils.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.*;

/**
 * Created by grishberg on 18.02.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FormulaListPresenterTest extends BaseTestCase {

    public static final int PRODUCT_ID = 1;
    public static final long FORMULA_ID = 2;
    @Mock
    FormulaDataSource formulaStorage;

    @Mock
    FormulasListView view;

    private FormulaListPresenter presenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        presenter = new FormulaListPresenter();
        presenter.attachView(view);
        when(formulaStorage.getFormulas(PRODUCT_ID))
                .thenReturn(Observable.just(mock(List.class)));
        presenter.formulaStorage = formulaStorage;
        presenter.onInitScreen(PRODUCT_ID); // set product id
        reset(formulaStorage);
        reset(view);
    }

    @Test
    public void testOnInitScreen() {
        //given
        final List<FormulaContainer> list = new ArrayList<>();
        when(formulaStorage.getFormulas(PRODUCT_ID))
                .thenReturn(Observable.just(list));
        //when
        presenter.onInitScreen(PRODUCT_ID);
        //then
        verify(formulaStorage).getFormulas(PRODUCT_ID);
        verify(view, times(1)).showFormulasList(list);
    }

    @Test
    public void testOnFormulaItemSelected() {
        //given
        final FormulaContainer container = mock(FormulaContainer.class);
        when(formulaStorage.getFormula(FORMULA_ID)).thenReturn(Observable.just(container));
        //when
        presenter.onFormulaItemSelected(FORMULA_ID);
        //then
        verify(view, times(1)).showFormulaDescription(container);
    }

    @Test
    public void testOnFormulaVisibleStateChanged() {
        //given
        final FormulaContainer container = mock(FormulaContainer.class);
        final List<FormulaContainer> list = new ArrayList<>();
        list.add(container);
        when(formulaStorage.getFormulas(PRODUCT_ID))
                .thenReturn(Observable.just(list));
        //when
        presenter.onFormulaVisibleStateChanged(FORMULA_ID, true);
        //then
        verify(formulaStorage, times(1)).updateFormulaVisibility(FORMULA_ID, true);
        verify(view, times(1)).showFormulasList(anyList());
    }

    @Test
    public void testSaveFormula() {
        //given
        final FormulaContainer container = mock(FormulaContainer.class);
        //when
        presenter.saveFormula(container);
        //then
        verify(formulaStorage, times(1)).updateFormula(container);
    }
}