package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.DailyDataRepository;
import com.grishberg.graphreporter.data.repository.exceptions.NetworkException;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.BaseTestCase;
import com.grishberg.graphreporter.utils.DebugLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by grishberg on 15.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CandlesChartPresenterTest extends BaseTestCase {

    public static final int PRODUCT_ID = 1;
    @Mock
    DailyDataRepository dailyDataRepository;

    @Mock
    CandlesChartView view;

    @Mock
    List<DailyValue> result;

    CandlesChartPresenter presenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        presenter = new CandlesChartPresenter();
        presenter.attachView(view);
        presenter.repository = dailyDataRepository;
        presenter.log = new DebugLogger();
    }

    @Test
    public void testRequestDailyValuesSuccess() {
        //given
        when(dailyDataRepository.getDailyValues(PRODUCT_ID)).thenReturn(Observable.just(result));
        //when
        presenter.requestDailyValues(PRODUCT_ID);
        //then
        verify(view, times(1)).showChart(any(ChartResponseContainer.class));
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
    }

    @Test
    public void testRequestDailyValuesNotSuccess() {
        //given
        when(dailyDataRepository.getDailyValues(PRODUCT_ID))
                .thenReturn(Observable.error(new NetworkException()));
        //when
        presenter.requestDailyValues(PRODUCT_ID);
        //then
        verify(view, times(0)).showChart(any(ChartResponseContainer.class));
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showFail(null);
    }
}