package com.frankzheng.signal;

/**
 * Interface for a generic signal handler. Implementers will implement the onDispatch method in
 * order to execute code when the signal that this handler is added to is dispatched. Users
 * should take care to maintain a reference to their handlers since Signals do not guarantee the
 * use of strong references.
 * @author Frank Zheng
 */
public interface Handler<T> {
    public void onDispatch(T data);
}