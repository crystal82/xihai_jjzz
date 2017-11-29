package com.chipsea.code.listener;

/**
 * Created by lixun on 2016/6/21.
 */
public interface SelectAirRecyclerviewCallback<T> {
    /**
     * 成功后回调
     */
    public void onClickItem(T deviceInfo);

}
