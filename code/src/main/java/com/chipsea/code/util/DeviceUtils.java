//package com.chipsea.code.util;
//
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.accloud.cloudservice.AC;
//import com.accloud.cloudservice.PayloadCallback;
//import com.accloud.cloudservice.VoidCallback;
//import com.accloud.service.ACDeviceBind;
//import com.accloud.service.ACDeviceFind;
//import com.accloud.service.ACDeviceUser;
//import com.accloud.service.ACException;
//import com.accloud.service.ACObject;
//import com.accloud.service.ACPushReceive;
//import com.accloud.service.ACPushTable;
//import com.accloud.service.ACUserDevice;
//import com.accloud.utils.ACUtils;
//import com.chipsea.code.listener.WIFICallback;
//import com.chipsea.code.listener.WIFIVoidCallback;
//import com.chipsea.mode.entity.AccountInfo;
//import com.chipsea.mode.entity.DeviceBind;
//import com.chipsea.mode.entity.DeviceInfo;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.TimeZone;
//
//import chipsea.wifiplug.lib.model.RunningStatus;
//import chipsea.wifiplug.lib.model.TimerSetting;
//import chipsea.wifiplug.lib.util.BytesUtil;
//import chipsea.wifiplug.lib.util.csACUtil;
//
//public class DeviceUtils {
//    //    private static DeviceUtils instance;
//    //    private OnDeviceStatusListener deviceStatusListener=null;
//
//    //    private Context context;
//    //    private String physicalDeviceId;
//    //    private int mRefreshInterval=8000;
//    //    private Handler handler=new Handler();
//
//    public static int OFFLINE        = 0; //不在线
//    public static int NETWORK_ONLINE = 1; //云端在线
//    public static int LOCAL_ONLINE   = 2; //局域网在线
//    public static int BOTH_ONLINE    = 3; //云端与局域网在线
//
//    public static void getDeviceWithStatus(final DeviceInfo localDevice, final WIFICallback<DeviceInfo> callback) {
//        csACUtil.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
//            @Override
//            public void success(List<ACUserDevice> acUserDevices) {
//                if (acUserDevices != null) {
//                    if (acUserDevices.size() > 0) {
//                        String physicalDeviceId = "";
//                        if (localDevice != null) physicalDeviceId = localDevice.physicalDeviceId;
//                        ACUserDevice foundDevice = acUserDevices.get(0);
//                        for (ACUserDevice curDevice : acUserDevices) {
//                            if (curDevice.physicalDeviceId == physicalDeviceId) {
//                                foundDevice = curDevice;
//                                break;
//                            }
//                        }
//
//                        DeviceInfo deviceInfo = new DeviceInfo();
//                        deviceInfo.deviceId = foundDevice.deviceId;
//                        deviceInfo.physicalDeviceId = foundDevice.physicalDeviceId;
//                        deviceInfo.name = foundDevice.name;
//                        deviceInfo.owner = foundDevice.owner;
//                        deviceInfo.status = foundDevice.status;
//                        deviceInfo.subDominId = foundDevice.subDomainId;
//
//                        if (callback != null) {
//                            callback.onSuccess(deviceInfo);
//                        }
//                    } else {
//                        if (callback != null) {
//                            callback.onSuccess(null);
//                        }
//                    }
//                } else {
//                    if (callback != null) {
//                        callback.onSuccess(null);
//                    }
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void listDevicesWithStatus(final WIFICallback<List<DeviceInfo>> callback) {
//        csACUtil.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
//            @Override
//            public void success(List<ACUserDevice> acUserDevices) {
//                if (acUserDevices != null) {
//                    if (acUserDevices.size() > 0) {
//                        List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
//                        for (ACUserDevice foundDevice : acUserDevices) {
//                            DeviceInfo deviceInfo = new DeviceInfo();
//                            deviceInfo.deviceId = foundDevice.deviceId;
//                            deviceInfo.physicalDeviceId = foundDevice.physicalDeviceId;
//                            deviceInfo.name = foundDevice.name;
//                            deviceInfo.owner = foundDevice.owner;
//                            deviceInfo.status = foundDevice.status;
//                            deviceInfo.subDominId = foundDevice.subDomainId;
//                            deviceInfos.add(deviceInfo);
//                        }
//                        updateDeviceState(deviceInfos, callback);
//                    } else {
//                        if (callback != null) {
//                            callback.onSuccess(null);
//                        }
//                    }
//                } else {
//                    if (callback != null) {
//                        callback.onSuccess(null);
//                    }
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void updateDeviceState(final List<DeviceInfo> deviceInfos, final WIFICallback<List<DeviceInfo>>
//            callback) {
//        if (ACUtils.isWifiConnected()) {
//            AC.findLocalDevice(2000, new PayloadCallback<List<ACDeviceFind>>() {
//                @Override
//                public void error(ACException e) {
//                    if (callback != null) {
//                        callback.onSuccess(deviceInfos);
//                    }
//                }
//
//                @Override
//                public void success(List<ACDeviceFind> deviceFinds) {
//                    if (callback != null) {
//                        if (deviceFinds.size() != 0) {
//                            for (DeviceInfo deviceInfo : deviceInfos) {
//                                boolean isFind = false;
//                                for (ACDeviceFind deviceFind : deviceFinds) {
//                                    if (deviceFind.getPhysicalDeviceId().equalsIgnoreCase(deviceInfo
//                                                                                                  .getPhysicalDeviceId())) {
//                                        isFind = true;
//                                        break;
//                                    }
//                                }
//                                resetInfoState(isFind, deviceInfo);
//                            }
//                        }
//                        calculateElec(deviceInfos, callback);
//                    }
//                }
//
//            });
//        } else {
//            if (callback != null) {
//                callback.onSuccess(deviceInfos);
//            }
//        }
//    }
//
//    public static void resetInfoState(boolean isFind, DeviceInfo deviceInfo) {
//        if (isFind) {
//            if (deviceInfo.getStatus() == OFFLINE) {
//                deviceInfo.setStatus(LOCAL_ONLINE);
//            } else if (deviceInfo.getStatus() == NETWORK_ONLINE) {
//                deviceInfo.setStatus(BOTH_ONLINE);
//            }
//        } else {
//            if (deviceInfo.getStatus() == LOCAL_ONLINE) {
//                deviceInfo.setStatus(OFFLINE);
//            } else if (deviceInfo.getStatus() == BOTH_ONLINE) {
//                deviceInfo.setStatus(NETWORK_ONLINE);
//            }
//        }
//    }
//
//    public static void calculateElec(final List<DeviceInfo> deviceInfos, final WIFICallback<List<DeviceInfo>>
//            callback) {
//        final int   count  = deviceInfos.size();
//        final int[] number = {0};
//        List<Long>  lstTs  = TrendUtils.getStartEndTimesForDays(0, 1);
//        for (int i = 0; i < deviceInfos.size(); i++) {
//            final DeviceInfo tempInfo = deviceInfos.get(i);
//            csACUtil.QueryHourOfDayReport(tempInfo.getPhysicalDeviceId(), tempInfo.subDominId, lstTs.get(0), lstTs
//                    .get(1), new PayloadCallback<List<ACObject>>() {
//                @Override
//                public void success(List<ACObject> acObjects) {
//                    float countNumber = 0;
//                    number[0] += 1;
//                    if (acObjects != null) {
//                        for (ACObject object : acObjects) {
//                            float pwConsumption = (object.getInt("_sum_powerConsumption") / 1000f);
//                            countNumber += pwConsumption;
//                        }
//                        BigDecimal b = new BigDecimal(countNumber);
//                        countNumber = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
//                        tempInfo.setDayElec(countNumber);
//                    }
//                    if (callback != null && count == number[0]) {
//                        callback.onSuccess(deviceInfos);
//                    }
//                }
//
//                @Override
//                public void error(ACException e) {
//                    if (callback != null) {
//                        callback.onFailure(e.getMessage(), e.getErrorCode());
//                    }
//                }
//            });
//        }
//    }
//
//    public static void openOrClosePlug(String physicalDeviceId, long subDomainId, byte port, boolean isOpen, int
//            status, final WIFIVoidCallback callback) {
//        csACUtil.openOrClosePlug(physicalDeviceId, subDomainId, port, isOpen, status, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void subscribe(String physicalDeviceId, long port, final WIFICallback<ACPushTable> callback) {
//        csACUtil.subscribe(physicalDeviceId, port, new PayloadCallback<ACPushTable>() {
//            @Override
//            public void success(ACPushTable acPushTable) {
//                if (callback != null) {
//                    callback.onSuccess(acPushTable);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void subscribeTimer(String physicalDeviceId, final WIFICallback<ACPushTable> callback) {
//        csACUtil.subscribeTimer(physicalDeviceId, new PayloadCallback<ACPushTable>() {
//            @Override
//            public void success(ACPushTable acPushTable) {
//                if (callback != null) {
//                    callback.onSuccess(acPushTable);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void subscribeAir(String physicalDeviceId, final WIFICallback<ACPushTable> callback) {
//        csACUtil.subscribeAir(physicalDeviceId, new PayloadCallback<ACPushTable>() {
//            @Override
//            public void success(ACPushTable acPushTable) {
//                if (callback != null) {
//                    callback.onSuccess(acPushTable);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void receive(final WIFICallback<ACPushReceive> callback) {
//        csACUtil.receiveRunningState(new PayloadCallback<ACPushReceive>() {
//            @Override
//            public void success(ACPushReceive acPushReceive) {
//                if (callback != null) {
//                    callback.onSuccess(acPushReceive);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void unSubscribe(ACPushTable acTable, final WIFIVoidCallback callback) {
//        csACUtil.unSubscribe(acTable, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void queryDeviceStatus(String PhysicalDeviceId, final long subDomainId, final WIFICallback<Integer>
//            callback) {
//        csACUtil.queryDeviceStatus(PhysicalDeviceId, subDomainId, new PayloadCallback<Integer>() {
//            @Override
//            public void success(Integer integer) {
//                if (callback != null) {
//                    callback.onSuccess(integer);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void queryRunningState(String PhysicalDeviceId, long subDomainId, final WIFICallback<RunningStatus>
//            callback) {
//        csACUtil.queryRunningState(PhysicalDeviceId, subDomainId, new PayloadCallback<List<RunningStatus>>() {
//            @Override
//            public void success(List<RunningStatus> runningStatuses) {
//                if (runningStatuses != null) {
//                    if (runningStatuses.size() > 0) {
//                        if (callback != null) {
//                            callback.onSuccess(runningStatuses.get(0));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void queryTimer(String PhysicalDeviceId, long subDomainId, byte indexId, final
//    WIFICallback<List<TimerSetting>> callback) {
//
//        csACUtil.queryTimer(PhysicalDeviceId, subDomainId, indexId, new PayloadCallback<List<TimerSetting>>() {
//            @Override
//            public void success(List<TimerSetting> timerSettings) {
//                if (callback != null) {
//                    callback.onSuccess(timerSettings);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void openOrCloseTimer(String physicalDeviceId, long subDomainId, byte indexId, byte action, final
//    WIFIVoidCallback callback) {
//        csACUtil.openOrCloseTimer(physicalDeviceId, subDomainId, indexId, action, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void airMatch(String physicalDeviceId, long subDomainId, final int data, final WIFICallback<String>
//            callback) {
//        byte[] tag = new byte[1];
//        tag[0] = (byte) data;
//        csACUtil.matchAir(physicalDeviceId, subDomainId, tag, new PayloadCallback<byte[]>() {
//            @Override
//            public void success(byte[] resp) {
//                Log.i("DeviceUtils", "success: data=" + data);
//                if (data == 2) {
//                    if (resp.length > 1) {
//                        byte[] bIRcodes = BytesUtil.subBytes(resp, 1, resp.length - 1);
//                        String sRet     = BytesUtil.bytesToHexString(bIRcodes);
//                        Log.i("DeviceUtils", "sRet=" + sRet);
//                        if (callback != null) {
//                            callback.onSuccess(sRet);
//                        }
//                    } else {
//                        callback.onFailure("Resp error", 0);
//                    }
//                } else if (data == 7) {
//                    if (resp.length > 1) {
//                        String sIpAddr = (resp[1] & 0xff) + "." + (resp[2] & 0xff) + "." + (resp[3] & 0xff) + "." +
//                                (resp[4] & 0xff);
//                        byte[] bPort   = new byte[2];
//                        bPort[0] = resp[6];
//                        bPort[1] = resp[5];
//                        int    iPort = BytesUtil.bytesToInt(bPort);
//                        String sRet  = sIpAddr + ";" + iPort;
//                        if (callback != null) {
//                            callback.onSuccess(sRet);
//                        }
//                    } else {
//                        callback.onFailure("Resp error", 0);
//                    }
//                } else {
//                    callback.onSuccess("");
//                }
//
//            }
//
//            @Override
//            public void error(ACException e) {
//                Log.e("DeviceUtils", "error: code=" + e.getErrorCode() + " msg:" + e.getMessage());
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void airControl(String physicalDeviceId, long subDomainId, byte[] data, final VoidCallback callback) {
//        csACUtil.airControl(physicalDeviceId, subDomainId, data, new VoidCallback() {
//            @Override
//            public void success() {
//                callback.success();
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.error(e);
//                }
//            }
//        });
//    }
//
//    public static void removeTimer(String PhysicalDeviceId, long subDomainId, byte indexId, final WIFIVoidCallback
//            callback) {
//        csACUtil.removeTimer(PhysicalDeviceId, subDomainId, indexId, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void setTimer(String PhysicalDeviceId, long subDomainId, TimerSetting timer, final WIFIVoidCallback
//            callback) {
//        csACUtil.setTimer(PhysicalDeviceId, subDomainId, timer, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void csBindDevice(String PhysicalDeviceId, final long subDomainId, final WIFICallback<Long>
//            callback) {
//        csACUtil.acBindDevice(PhysicalDeviceId, subDomainId, new PayloadCallback<Long>() {
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//
//            @Override
//            public void success(Long deviceId) {
//                Calendar cal = Calendar.getInstance();
//                ;
//                TimeZone timeZone = cal.getTimeZone();
//                csACUtil.csRegBindDevice(deviceId, subDomainId, timeZone.getRawOffset(), null);
//                if (callback != null) {
//                    callback.onSuccess(deviceId);
//                }
//            }
//        });
//    }
//
//    public static void csUnbindDeviceNoForce(String PhysicalDeviceId, long subDomainId, final WIFIVoidCallback
//            callback) {
//        csACUtil.csUnbindDevice(PhysicalDeviceId, subDomainId, 0, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void getShareCode(long deviceId, long subDomainId, final WIFICallback<String> callback) {
//        csACUtil.getShareCode(deviceId, subDomainId, new PayloadCallback<String>() {
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//
//            @Override
//            public void success(String o) {
//                if (!TextUtils.isEmpty(o)) {
//                    if (callback != null) {
//                        callback.onSuccess(o);
//                    }
//                }
//            }
//        });
//    }
//
//    public static void csUnbindDevice(String PhysicalDeviceId, long subDomainId, final WIFIVoidCallback callback) {
//        csACUtil.csUnbindDevice(PhysicalDeviceId, subDomainId, 1, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (e.getErrorCode() == 10003 || e.getErrorCode() == 3812) {
//                    if (callback != null) {
//                        callback.onSuccess();
//                    }
//                } else {
//                    if (callback != null) {
//                        callback.onFailure(e.getMessage(), e.getErrorCode());
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * @param shareCode 分享码
//     * @param callback  结果回调
//     * @Description 通过分享码绑定设备
//     */
//    public static void bindDeviceWithShareCode(String shareCode, final WIFIVoidCallback callback) {
//        csACUtil.bindDeviceWithShareCode(shareCode, new PayloadCallback<ACUserDevice>() {
//            @Override
//            public void success(ACUserDevice acUserDevice) {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void changeDeviceName(long deviceId, long subDomainId, String newName, final WIFIVoidCallback
//            callback) {
//        csACUtil.changeName(deviceId, subDomainId, newName, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void nonAdminUnbindDevice(long deviceId, long subDomainId, final WIFIVoidCallback callback) {
//        csACUtil.acUnbindDevice(deviceId, subDomainId, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//
//    public static void unbindDeviceWithUser(long deviceId, long subDomainId, long userid, final WIFIVoidCallback
//            callback) {
//        csACUtil.unbindDeviceWithUser(deviceId, subDomainId, userid, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static String getSSID() {
//        return csACUtil.getSSID();
//    }
//
//    public static void listUsersByDevice(long deviceId, long subDomainId, final WIFICallback<List<AccountInfo>>
//            callback) {
//        csACUtil.listUsers(deviceId, subDomainId, new PayloadCallback<List<ACDeviceUser>>() {
//            @Override
//            public void success(List<ACDeviceUser> acBindings) {
//                ArrayList<AccountInfo> lstRet = new ArrayList<AccountInfo>();
//                for (int i = 0; i < acBindings.size(); i++) {
//                    ACDeviceUser deviceUser = acBindings.get(i);
//                    //设备绑定的用户类型：0普通用户，1管理员
//                    if (deviceUser.getUserType() == 0) {
//                        AccountInfo accountInfo = new AccountInfo();
//                        //accountInfo.setId(deviceUser.getUserId());
//                        //accountInfo.setNickName(deviceUser.getName());
//                        //accountInfo.setPhone(deviceUser.getPhone());
//                        lstRet.add(accountInfo);
//                    }
//                }
//                if (callback != null) {
//                    callback.onSuccess(lstRet);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    //    public void setDeviceStatusListener(OnDeviceStatusListener listener){
//    //        deviceStatusListener=listener;
//    //    }
//
//    //    public DeviceUtils(Context context,String physicalDeviceId) {
//    //        this.context = context;
//    //        this.physicalDeviceId=physicalDeviceId;
//    //    }
//    //
//    //    public static DeviceUtils getInstance(Context context,String physicalDeviceId) {
//    //        if (instance == null) {
//    //            instance = new DeviceUtils(context,physicalDeviceId);
//    //        }else{
//    //            instance.context=context;
//    //            instance.physicalDeviceId=physicalDeviceId;
//    //        }
//    //        return instance;
//    //    }
//    //
//    //    public void startScan(){
//    //        handler.post(scanRunable);
//    //    }
//    //
//    //    public void stopScan(){
//    //        handler.removeCallbacks(scanRunable);
//    //    }
//    //
//    //    private Runnable scanRunable=new Runnable() {
//    //        @Override
//    //        public void run() {
//    //            csACUtil.queryDeviceStatus(physicalDeviceId, new PayloadCallback<Integer>() {
//    //                @Override
//    //                public void success(Integer status) {
//    //                    if(deviceStatusListener!=null){
//    //                        deviceStatusListener.statusChange(status);
//    //                    }
//    //                    handler.postDelayed(scanRunable,mRefreshInterval);
//    //                }
//    //
//    //                @Override
//    //                public void error(ACException e) {
//    //                    handler.postDelayed(scanRunable,mRefreshInterval);
//    //                }
//    //            });
//    //        }
//    //    };
//    //
//    //    public interface OnDeviceStatusListener{
//    //        void statusChange(int status);
//    //    }
//
//
//}
