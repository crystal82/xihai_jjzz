package com.chipsea.code.listener;

/**
 * Created by lixun on 2016/6/21.
 */
public interface WIFICallback<T> {
    /**
     * 成功后回调
     * @param data 数据
     */
    public void onSuccess(T data);

    /**
     * 失败回调
     * @param msg
     * @param code
     */
    public void onFailure(String msg, int code);
}
