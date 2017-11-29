package com.chipsea.code.listener;


/**
 * Created by hfei on 2015/10/29.
 */
public interface HttpCallback {

    /**
     * 成功后回调
     * @param data 数据
     */
    public void onSuccess(Object data);

    /**
     * 失败回调
     * @param msg
     * @param code
     */
    public void onFailure(String msg, int code);
}
