package com.grishberg.graphreporter.data.beans.stream;

import com.grishberg.graphreporter.data.beans.values.DualDateValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
        final ValuesStream<DualDateValue> stream = new ValuesStreamImpl(getMinutesValues(), MINUTE * 3);
        DualDateValue value = stream.getNextElement();
        assertEquals(150, calcMid(value));
        assertEquals(10, value.getPriceOpen(), 0.001);
        assertEquals(13, value.getPriceClose(), 0.001);
        assertEquals(16, value.getPriceHigh(), 0.001);
        assertEquals(8, value.getPriceLow(), 0.001);

        value = stream.getNextElement();
        assertEquals(180, calcMid(value));
        assertEquals(20, value.getPriceOpen(), 0.001);
        assertEquals(21, value.getPriceClose(), 0.001);
        assertEquals(22, value.getPriceHigh(), 0.001);
        assertEquals(19, value.getPriceLow(), 0.001);

        value = stream.getNextElement();
        assertEquals(450, calcMid(value));
        assertEquals(21, value.getPriceOpen(), 0.001);
        assertEquals(26, value.getPriceHigh(), 0.001);
        assertEquals(18, value.getPriceLow(), 0.001);
        assertEquals(23, value.getPriceClose(), 0.001);
    }

    private long calcMid(DualDateValue value) {
        return (Math.abs(value.getDtStart()) + Math.abs(value.getDtEnd())) / 2;
    }
}