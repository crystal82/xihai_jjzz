package com.chipsea.mode.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xulj on 2016/5/25.
 */
public class StatEntity implements Parcelable {
    private float value  ;    //y坐标的值
    private float axis ;      //y坐标 不需要初始化
    private int xPosition ;   //x坐标位置

    public StatEntity(int xPosition, float value) {
        this.xPosition = xPosition;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getAxis() {
        return axis;
    }

    public void setAxis(float axis) {
        this.axis = axis;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.value);
        dest.writeFloat(this.axis);
        dest.writeInt(this.xPosition);
    }

    protected StatEntity(Parcel in) {
        this.value = in.readFloat();
        this.axis = in.readFloat();
        this.xPosition = in.readInt();
    }

    public static final Parcelable.Creator<StatEntity> CREATOR = new Parcelable.Creator<StatEntity>() {
        @Override
        public StatEntity createFromParcel(Parcel source) {
            return new StatEntity(source);
        }

        @Override
        public StatEntity[] newArray(int size) {
            return new StatEntity[size];
        }
    };
}
