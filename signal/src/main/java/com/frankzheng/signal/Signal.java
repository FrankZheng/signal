package com.frankzheng.signal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class Signal<T> {
    private Map<Handler<T>, Boolean> _listeners = new WeakHashMap<Handler<T>, Boolean>();

    public Signal() {

    }

    public void add(Handler<T> handler) {
        synchronized (this) {
            _listeners.put(handler, false);
        }
    }

    public void addOnce(Handler<T> handler) {
        synchronized (this) {
            _listeners.put(handler, true);
        }
    }

    public void remove(Handler<T> handler) {
        synchronized (this) {
            _listeners.remove(handler);
        }
    }

    public void removeAll() {
        synchronized (this) {
            _listeners.clear();
        }
    }

    public void dispatch(T data) {
        List<Handler<T>> listenersClone = new ArrayList<Handler<T>>();
        synchronized (this) {
            Set<Map.Entry<Handler<T>, Boolean>> entrySet = _listeners.entrySet();
            Iterator<Map.Entry<Handler<T>, Boolean>> it = entrySet.iterator();
            while (it.hasNext()) {
                Map.Entry<Handler<T>, Boolean> entry = it.next();
                // Add listeners to our List with a strong reference
                listenersClone.add(entry.getKey());

                // remove the listener if it was added with addOnce
                if (entry.getValue()) {
                    it.remove();
                }
            }
        }

        // Dispatch all of the signals using the cloned list so modifications won't cause issues
        for (Handler<T> handler : listenersClone) {
            handler.onDispatch(data);
        }
    }
}
