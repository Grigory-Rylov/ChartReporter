package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.graphreporter.data.beans.ChartResponseContainer;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.utils.BaseTestCase;
import com.grishberg.graphreporter.utils.DebugLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by grishberg on 15.01.17.
 * Тестирование презентера графика со свечами
 */
@RunWith(MockitoJUnitRunner.class)
public class CandlesChartPresenterTest extends BaseTestCase {

    private static final int PRODUCT_ID = 1;
    @Mock
    DailyDataRepository dailyDataRepository;

    @Mock
    CandlesChartView view;

    @Mock
    List<DailyValue> result;

    private ChartResponseContainer responseContainer;

    private CandlesChartPresenter presenter;

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
        //when(dailyDataRepository.getValues(PRODUCT_ID)).thenReturn(Observable.just(result));
        //when
        //presenter.requestDailyValues(PRODUCT_ID);
        //then
        //verify(view, times(1)).showCandleChart(any(ChartResponseContainer.class));
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
    }

    @Test
    public void testRequestDailyValuesNotSuccess() {
        //given
        //when(dailyDataRepository.getValues(PRODUCT_ID))
        //        .thenReturn(Observable.error(new NetworkException()));
        //when
        //presenter.requestDailyValues(PRODUCT_ID);
        //then
        //verify(view, times(0)).showCandleChart(any(ChartResponseContainer.class));
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).showFail(null);
    }

    @Test
    public void testRecalculatePeriodSuccess() {
        //given
        final List<DailyValue> values = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            final DailyValue value = mock(DailyValue.class);
            when(value.getPriceLow()).thenReturn(1.0D);
            when(value.getPriceHigh()).thenReturn(3.0D);
            when(value.getPriceOpen()).thenReturn(1.0D);
            when(value.getPriceClose()).thenReturn(2.0D);
            when(value.getDt()).thenReturn(i);
            values.add(value);
        }
/*
        when(dailyDataRepository.getValues(PRODUCT_ID))
                .thenReturn(Observable.just(values));

        doAnswer(new Answer<ChartResponseContainer>() {
            @Override
            public ChartResponseContainer answer(final InvocationOnMock invocation) throws Throwable {
                final Object[] args = invocation.getArguments();
                responseContainer = (ChartResponseContainer) args[0];
                return responseContainer;
            }
        }).when(view).showCandleChart(any(ChartResponseContainer.class));

        //when
        presenter.onSelectedProduct(PRODUCT_ID, ChartPeriod.WEEK);
        //then
        verify(view, times(1)).showProgress();
        verify(view, times(1)).hideProgress();
        verify(view, times(0)).showFail(null);
        final List<Long> dates = responseContainer.getDates();
        final List<CandleEntry> entries = responseContainer.getEntries();
        assertEquals(2, dates.size());
        assertEquals(2, entries.size());
        assertEquals(Long.valueOf(21000), dates.get(0));
        assertEquals(Long.valueOf(45000), dates.get(1));
        final CandleEntry entry1 = entries.get(0);
        assertEquals(3.0F, entry1.getHigh(), 0.001);
        assertEquals(1.0F, entry1.getLow(), 0.001);
        */
    }
}