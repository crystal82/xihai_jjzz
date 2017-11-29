package com.chipsea.mode.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/15.
 */

public class AirModel implements Parcelable {
    private int m_code ;
    private String m_label;
    private String m_search_string ;
    private int m_format_id ;
    private int m_keyfile ;

    public int getM_rank() {
        return m_rank;
    }

    public void setM_rank(int m_rank) {
        this.m_rank = m_rank;
    }

    private int m_rank ;
    private int m_key_squency ;
    private boolean isSend ;      //发送给按键只能插座的标志。

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public int getM_code() {
        return m_code;
    }

    public void setM_code(int m_code) {
        this.m_code = m_code;
    }

    public String getM_label() {
        return m_label;
    }

    public void setM_label(String m_lable) {
        this.m_label = m_lable;
    }

    public String getM_search_string() {
        return m_search_string;
    }

    public void setM_search_string(String m_search_string) {
        this.m_search_string = m_search_string;
    }

    public int getM_format_id() {
        return m_format_id;
    }

    public void setM_format_id(int m_format_id) {
        this.m_format_id = m_format_id;
    }

    public int getM_keyfile() {
        return m_keyfile;
    }

    public void setM_keyfile(int m_keyfile) {
        this.m_keyfile = m_keyfile;
    }

    public int getM_key_squency() {
        return m_key_squency;
    }

    public void setM_key_squency(int m_key_squency) {
        this.m_key_squency = m_key_squency;
    }

    public AirModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.m_code);
        dest.writeString(this.m_label);
        dest.writeString(this.m_search_string);
        dest.writeInt(this.m_format_id);
        dest.writeInt(this.m_keyfile);
        dest.writeInt(this.m_rank);
        dest.writeInt(this.m_key_squency);
        dest.writeByte(this.isSend ? (byte) 1 : (byte) 0);
    }

    protected AirModel(Parcel in) {
        this.m_code = in.readInt();
        this.m_label = in.readString();
        this.m_search_string = in.readString();
        this.m_format_id = in.readInt();
        this.m_keyfile = in.readInt();
        this.m_rank = in.readInt();
        this.m_key_squency = in.readInt();
        this.isSend = in.readByte() != 0;
    }

    public static final Creator<AirModel> CREATOR = new Creator<AirModel>() {
        @Override
        public AirModel createFromParcel(Parcel source) {
            return new AirModel(source);
        }

        @Override
        public AirModel[] newArray(int size) {
            return new AirModel[size];
        }
    };
}
