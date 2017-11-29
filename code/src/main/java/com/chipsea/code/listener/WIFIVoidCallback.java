package com.chipsea.code.listener;

/**
 * Created by lixun on 2016/6/21.
 */
public interface WIFIVoidCallback {
    /**
     * 成功后回调
     */
    public void onSuccess();

    /**
     * 失败回调
     * @param msg
     * @param code
     */
    public void onFailure(String msg, int code);
}
