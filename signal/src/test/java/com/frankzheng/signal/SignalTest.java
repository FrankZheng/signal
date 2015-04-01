package com.frankzheng.signal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

import static org.junit.Assert.assertEquals;




/**
 * Created by zhengxiaoqiang on 15/4/1.
 */
public class SignalTest {
    Signal<String> _signal;
    CountingHandler _handler;

    private static class CountingHandler implements Handler<String> {
        public int dispatchCount;
        public String data;
        @Override
        public void onDispatch(String data) {
            dispatchCount++;
            this.data = data;
        }
    }

    @Before
    public void setup() {
        _signal = new Signal<String>();
        _handler = new CountingHandler();
    }
    @After
    public void tearDown() {
        _signal = null;
        _handler = null;
    }

    @Test
    public void testAddOnce() {
        String data = "testAddOnce";

        _signal.addOnce(_handler);
        assertEquals(0, _handler.dispatchCount);

        _signal.dispatch(data);
        assertEquals(1, _handler.dispatchCount);
        assertEquals(data, _handler.data);

        _signal.dispatch(data);
        assertEquals(1, _handler.dispatchCount);
    }

    @Test
    public void testAdd() {
        String data = "testAdd";

        _signal.add(_handler);
        _signal.dispatch(data);
        assertEquals(1,  _handler.dispatchCount);
        assertEquals(data, _handler.data);

        data = "testAdd2";
        _signal.dispatch(data);
        assertEquals(2,  _handler.dispatchCount);
        assertEquals(data, _handler.data);
    }

    @Test
    public void testRemove() {
        String data = "testRemove";

        _signal.add(_handler);
        _signal.remove(_handler);
        _signal.dispatch(data);
        assertEquals(0, _handler.dispatchCount);

        _signal.add(_handler);
        _signal.dispatch(data);
        assertEquals(1, _handler.dispatchCount);
        assertEquals(data, _handler.data);

        _signal.remove(_handler);
        _signal.dispatch(data);
        assertEquals(1, _handler.dispatchCount);
    }

    @Test
    public void testMultipleAdd() {
        List<CountingHandler> handlerList = new ArrayList<CountingHandler>();
        for(int i = 0 ; i < 10 ; i++) {
            CountingHandler handler = new CountingHandler();
            if( i % 2 == 0) {
                _signal.add(handler);
            } else {
                _signal.addOnce(handler);
            }
            handlerList.add(handler);
        }

        String data = "for test multiple add";

        _signal.dispatch(data);
        _signal.dispatch(data);

        for(int i = 0 ; i < handlerList.size() ; i++) {
            CountingHandler handler = handlerList.get(i);
            if( i % 2 == 0) {
                assertEquals(2, handler.dispatchCount);
            } else {
                assertEquals(1, handler.dispatchCount);
            }
        }
    }



    @Test
    public void testRemoveAll() {
        List<CountingHandler> handlerList = new ArrayList<CountingHandler>();
        for (int i = 0; i < 10; i++) {
            CountingHandler handler = new CountingHandler();
            if (i % 2 == 0) {
                _signal.add(handler);
            } else {
                _signal.addOnce(handler);
            }
            handlerList.add(handler);
        }

        String data = "for test multiple add";

        _signal.dispatch(data);
        _signal.removeAll();

        _signal.dispatch(data);

        for (int i = 0; i < handlerList.size(); i++) {
            CountingHandler handler = handlerList.get(i);
            assertEquals(1, handler.dispatchCount);
        }
    }


}