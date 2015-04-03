package com.frankzheng.signal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Interface used to publicly expose a signal on a class. The interface doesn't allow other classes
 * to dispatch the signal and may prevent other internal functions from being called.
 * @author Frank Zheng
 */
public class Signal<T> {
    private Map<Handler<T>, Boolean> _listeners = new WeakHashMap<Handler<T>, Boolean>();

    public Signal() {

    }

    /**
     * Method used to add a new signal handler to this signal
     *
     * @param handler
     *            The handler that will receive the signal when it is dispatched
     */
    public void add(Handler<T> handler) {
        synchronized (this) {
            _listeners.put(handler, false);
        }
    }

    /**
     * Method used to add a new signal handler to this signal that should only be dispatched once.
     * After the Signal is dispatched the first time, it is automatically removed.
     *
     * @param handler
     *            The handler that will receive the signal when it is dispatched
     */
    public void addOnce(Handler<T> handler) {
        synchronized (this) {
            _listeners.put(handler, true);
        }
    }

    /**
     * Method used to remove a signal handler from the signal
     *
     * @param id
     *            Value that was returned by the add method
     */
    public void remove(Handler<T> handler) {
        synchronized (this) {
            _listeners.remove(handler);
        }
    }

    /**
     * Method used to remove all signal handlers from the signal
     */
    public void removeAll() {
        synchronized (this) {
            _listeners.clear();
        }
    }

    /**
     * Method used to dispatch the signal and send the provided data to all of the added
     * handlers.
     * Handlers will all be called on the calling thread. This method is thread-safe.
     *
     * @param data
     *            The data object that will provided to each handler when executed.
     */
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
