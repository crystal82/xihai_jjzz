package com.chipsea.mode.entity;

/**
 * Created by Administrator on 2017/1/20.
 */

public class DeviceBind {
    public String mac;
    public String did;
    public String productKey;
    public String productSecret;

    public DeviceBind(String mac, String did, String productKey, String productSecret) {
        this.mac = mac;
        this.did = did;
        this.productKey = productKey;
        this.productSecret = productSecret;
    }
}
