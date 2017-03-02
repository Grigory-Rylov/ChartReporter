package com.grishberg.graphreporter.data.model.stream;

import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.values.DualDateValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.grishberg.graphreporter.utils.ValuesRepository.MINUTE;
import static com.grishberg.graphreporter.utils.ValuesRepository.getMinutesValues;
import static org.junit.Assert.*;

/**
 * Created by grishberg on 27.02.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValuesStreamImplTest {

    @Test
    public void testGet3MinutesCandle() throws ValuesStream.NoMoreItemException {
        final ValuesStream<DualDateValue> stream = new ValuesStreamImpl(getMinutesValues(), 60 * 3 * 1000);
        DualDateValue value = stream.getNextElement();
        assertEquals(MINUTE * (1 + 3) / 2, value.getMidDt());
        assertEquals(10, value.getPriceOpen(), 0.001);
        assertEquals(13, value.getPriceClose(), 0.001);
        assertEquals(16, value.getPriceHigh(), 0.001);
        assertEquals(8, value.getPriceLow(), 0.001);

        value = stream.getNextElement();
        assertEquals(MINUTE * (4), value.getMidDt());
        assertEquals(20, value.getPriceOpen(), 0.001);
        assertEquals(21, value.getPriceClose(), 0.001);
        assertEquals(22, value.getPriceHigh(), 0.001);
        assertEquals(19, value.getPriceLow(), 0.001);

        value = stream.getNextElement();
        assertEquals(MINUTE * (8), value.getMidDt());
        assertEquals(21, value.getPriceOpen(), 0.001);
        assertEquals(26, value.getPriceHigh(), 0.001);
        assertEquals(18, value.getPriceLow(), 0.001);
        assertEquals(23, value.getPriceClose(), 0.001);
    }
}