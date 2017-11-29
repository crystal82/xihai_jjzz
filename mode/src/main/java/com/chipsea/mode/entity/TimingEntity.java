package com.chipsea.mode.entity;

/**
 * Created by xulj on 2016/6/24.
 */
public class TimingEntity {
    private String time ;
    private String weeks;

    public TimingEntity(String time, String weeks, boolean status) {
        this.time = time;
        this.weeks = weeks;
        this.status = status;
    }

    private boolean status ;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
