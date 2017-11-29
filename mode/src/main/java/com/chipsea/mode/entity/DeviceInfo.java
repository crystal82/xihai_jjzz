package com.chipsea.mode.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chipsea.configuration.Config;

/**
 * Created by lixun on 2016/6/23.
 */
public class DeviceInfo implements Parcelable {
    public long deviceId;
    public long owner;
    public String name;
    public String alias;
    public String physicalDeviceId;
    public long subDominId;
    public int status = 0;
    public int type ;  //1代表只能插座，2代表计量只能插座，3空调伴侣 4代表计量空调伴侣
    public float dayElec ;

    public float getDayElec() {
        return dayElec;
    }

    public void setDayElec(float dayElec) {
        this.dayElec = dayElec;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public String getPhysicalDeviceId() {
        return physicalDeviceId;
    }

    public void setPhysicalDeviceId(String physicalDeviceId) {
        this.physicalDeviceId = physicalDeviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSubDominId() {
        return subDominId;
    }

    public void setSubDominId(long subDominId) {
        this.subDominId = subDominId;
    }

    public int getType() {
        if(this.subDominId == Config.SMART_SOCKET){
            return 1 ;
        }else if(this.subDominId == Config.SUPER_SMART_SOCKET){
            return 2 ;
        }else if(this.subDominId == Config.AC_PARTNER){
            return 3 ;
        }else if(this.subDominId == Config.SUPER_AC_PARTNER){
            return 4;
        }else if(this.subDominId == Config.AC_PARTNER_WITHKEY){
            return 5;
        }
        return 2 ;
    }
    public boolean isAir(){
        if(this.subDominId == Config.AC_PARTNER || this.subDominId == Config.SUPER_AC_PARTNER || this.subDominId == Config.AC_PARTNER_WITHKEY){
            return true ;
        }else {
            return false ;
        }
    }
    public boolean isKeyAir(){
        if(this.subDominId == Config.AC_PARTNER_WITHKEY){
            return true ;
        }else {
            return false ;
        }
    }
    public boolean isSuperSocket(){
        if(this.subDominId == Config.SUPER_AC_PARTNER || this.subDominId == Config.SUPER_SMART_SOCKET){
            return true ;
        }else {
            return false ;
        }
    }
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.deviceId);
        dest.writeLong(this.owner);
        dest.writeString(this.name);
        dest.writeString(this.physicalDeviceId);
        dest.writeLong(this.subDominId);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
        dest.writeFloat(this.dayElec);
    }

    public DeviceInfo() {
    }

    protected DeviceInfo(Parcel in) {
        this.deviceId = in.readLong();
        this.owner = in.readLong();
        this.name = in.readString();
        this.physicalDeviceId = in.readString();
        this.subDominId = in.readLong();
        this.status = in.readInt();
        this.type = in.readInt();
        this.dayElec = in.readFloat();
    }

    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}
