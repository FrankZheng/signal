package com.frankzheng.signal;

/**
 * Created by zhengxiaoqiang on 15/4/1.
 */
public interface Handler<T> {
    public void onDispatch(T data);
}
