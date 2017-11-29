package com.chipsea.mode.entity;

import com.chipsea.mode.R;

/**
 * Created by Administrator on 2017/3/14.
 */

public class AirControlEntity {
    private byte conoff ;   //开关
    private byte ctemp ;    //温度
    private byte cwind ;    //风速
    private byte cmode ;    //模式
    private byte cwinddir ; // 风向

    public AirModel getModel() {
        return model;
    }

    private AirModel model ;
    public AirControlEntity(byte conoff, byte cmode, byte ctemp, byte cwind, byte cwinddir) {
        this.conoff = conoff;
        this.ctemp = ctemp;
        this.cwind = cwind;
        setCmode(cmode);
        this.cwinddir = cwinddir;
    }
    public static AirControlEntity getDefalutEntity(){
        return new AirControlEntity((byte)1,(byte)0,(byte)10,(byte)0,(byte)0) ;
    }
    public byte getConoff() {
        return conoff;
    }

    public void setConoff(byte conoff) {
        this.conoff = conoff;
    }

    public byte getCtemp() {
        return ctemp;
    }

    public void setCtemp(byte ctemp) {
        this.ctemp = ctemp;
    }

    public byte getCwind() {
        return cwind;
    }

    public void setCwind(byte cwind) {
        this.cwind = cwind;
    }

    public byte getCmode() {
        return cmode;
    }

    public void setCmode(byte cmode) {
        this.cmode = cmode;
        if(cmode == 0){
            this.model = AirModel.AUTO ;
        }else if(cmode == 1){
            this.model = AirModel.COLD ;
        }else if(cmode == 2){
            this.model = AirModel.WET ;
        }else if(cmode == 3){
            this.model = AirModel.WIND ;
        }else if(cmode == 4){
            this.model = AirModel.HOT ;
        }
    }

    public byte getCwinddir() {
        return cwinddir;
    }

    public void setCwinddir(byte cwinddir) {
        this.cwinddir = cwinddir;
    }

    public void changeOffOrOn(){
        setConoff((byte) ((conoff + 1) % 2));
    }
    public int getIntTemp(){
        return ctemp + 16 ;
    }

    public void subtractTemp(){
        byte number = (byte) (ctemp -1);
        if(number < 0) {
            number = 0 ;
        }
        setCtemp(number);
    }
    public void addTemp(){
        byte number = (byte) (ctemp +1);
        if(number > 14) {
            number = 14 ;
        }
        setCtemp(number);
    }
    public void changeModel(){
        setCmode((byte) ((cmode + 1) % 5));
    }
    public void changeWindSpeed(){
        setCwind((byte) ((cwind + 1) % 4));
    }
    public enum AirModel {
        AUTO(R.color.air_state_auto, R.mipmap.state_auto, R.mipmap.air_auto_icon,R.string.air_model_auto),
        COLD(R.color.air_state_cold, R.mipmap.state_cold, R.mipmap.air_cold_icon,R.string.air_model_cold),
        WET(R.color.air_state_wet, R.mipmap.state_wet, R.mipmap.air_wet_icon,R.string.air_model_wet),
        WIND(R.color.air_state_wind, R.mipmap.state_wind, R.mipmap.air_wind_icon,R.string.air_model_wind) ,
        HOT(R.color.air_state_hot, R.mipmap.state_hot, R.mipmap.air_hot_icon,R.string.air_model_hot);


        int color;
        int imgRes;
        int modelDrawable;
        int modelName;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }

        public int getModelDrawable() {
            return modelDrawable;
        }

        public void setModelDrawable(int modelDrawable) {
            this.modelDrawable = modelDrawable;
        }

        public int getModelName() {
            return modelName;
        }

        public void setModelName(int modelName) {
            this.modelName = modelName;
        }

        AirModel(int color, int imgRes, int modelDrawable, int model) {
            this.color = color;
            this.imgRes = imgRes;
            this.modelDrawable = modelDrawable;
            this.modelName = model ;
        }
    }
}
