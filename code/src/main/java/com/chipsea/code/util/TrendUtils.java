
package com.chipsea.code.util;

import com.chipsea.code.listener.WIFICallback;
import com.chipsea.mode.entity.StatDetailEntity;
import com.chipsea.mode.entity.StatEntity;
import com.chipsea.mode.entity.XHelpEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import chipsea.wifiplug.lib.model.RunningStatus;

/**
 * Created by xulj on 2016/6/14.
 */
public class TrendUtils {


    public static StatDetailEntity ConvertRuningStatu2StateDetail(RunningStatus status){
        StatDetailEntity entity=new StatDetailEntity();
        entity.voltage= status.voltage / 10f;
        entity.electricCurrent=status.electricCurrent / 100f;
        entity.instantaneousPower=status.instantaneousPower / 10f;
        //entity.powerConsumption=Math.round ((status.voltage/10f) * (status.electricCurrent/100f));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        entity.Time = sdf.format(new Date());

        return entity;
    }

    public static List<XHelpEntity> getTrendXhelpEntityForWeek(long startTs){
        List<XHelpEntity> xHelpEntities = new ArrayList<XHelpEntity>() ;
        for (int i = 0; i <7 ; i++) {
            String dateStr = TimeUtil.getMinusDayString(startTs,i) ;
            XHelpEntity entity = new XHelpEntity(i,dateStr);
            xHelpEntities.add(entity);
        }
        return xHelpEntities ;
    }
    public static List<XHelpEntity> getTrendXhelpEntityForXSetion(long startTs,int xSesition){
        switch (xSesition){
            case 24:
                return getTrendXhelpEntityForDay() ;
            case 7:
                return getTrendXhelpEntityForWeek(startTs) ;
            case 28:
                return getTrendXhelpEntityForMonth(startTs) ;
        }
        return null;
    }

    public static void getTrendStatEntitysForXSetion(String physicalDeviceId,long subDomainId,final long startTs, long endTs, int xSesition, final WIFICallback<List<StatEntity>> callback){
        switch (xSesition){
            case 24:
               //csACUtil.QueryHourOfDayReport(physicalDeviceId, subDomainId,startTs, endTs, new PayloadCallback<List<ACObject>>() {
               //    @Override
               //    public void success(List<ACObject> acObjects) {
               //        List<StatEntity> result = new ArrayList<>() ;
               //        if(acObjects!=null) {
               //            for (ACObject object : acObjects) {
               //                int hour = object.getInt("reportHour");
               //                float pwConsumption =  (object.getInt("_sum_powerConsumption") / 1000f);
               //                BigDecimal b=new BigDecimal(pwConsumption);
               //                pwConsumption=b.setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
               //                if(pwConsumption != 0){
               //                    result.add(new StatEntity(hour, pwConsumption));
               //                }
               //            }
               //        }
               //        if(callback!=null){
               //            callback.onSuccess(result);
               //        }
               //    }

               //    @Override
               //    public void error(ACException e) {
               //        if(callback!=null){
               //            callback.onFailure(e.getMessage(),e.getErrorCode());
               //        }
               //    }
               //});
                break;
            case 7:
            case 28:
               //csACUtil.QueryDayReport(physicalDeviceId,subDomainId,startTs,endTs, new PayloadCallback<List<ACObject>>() {
               //    @Override
               //    public void success(List<ACObject> acObjects) {
               //        List<StatEntity> result = new ArrayList<>() ;
               //        if(acObjects!=null){
               //            for (ACObject object : acObjects) {
               //                String reportDay = object.getString("reportDay") + " 00:00:00";
               //                float pwConsumption =  (object.getInt("_sum_powerConsumption") / 1000f);

               //                BigDecimal b=new BigDecimal(pwConsumption);
               //                pwConsumption=b.setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
               //                int iDays=daysBetween(startTs,reportDay);
               //                result.add(new StatEntity(iDays, pwConsumption));
               //            }
               //        }
               //        if(callback!=null){
               //            callback.onSuccess(result);
               //        }
               //    }
               //    @Override
               //    public void error(ACException e) {
               //        if(callback!=null){
               //            callback.onFailure(e.getMessage(),e.getErrorCode());
               //        }
               //    }
               //});
                break;
        }
    }

    public static void queryRunningState(String physicalDeviceId,long subDomainId, final WIFICallback<List<RunningStatus>> callback){
       //csACUtil.queryRunningState(physicalDeviceId, subDomainId,new PayloadCallback<List<RunningStatus>>() {
       //    @Override
       //    public void success(List<RunningStatus> runningStatuses) {
       //        if(callback!=null){
       //            callback.onSuccess(runningStatuses);
       //        }
       //    }

       //    @Override
       //    public void error(ACException e) {
       //        if(callback!=null){
       //            callback.onFailure(e.getMessage(),e.getErrorCode());
       //        }
       //    }
       //});
    }

    private static int daysBetween(long startTs,String reportDay){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date dReport=new Date();
        try {
            dReport=sdf.parse(reportDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTs);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        long between_days=(dReport.getTime()-cal.getTimeInMillis())/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    public static List<XHelpEntity> getTrendXhelpEntityForDay(){
        return Constant.getDayXHelpEntity() ;
    }

    public static List<XHelpEntity> getTrendXhelpEntityForMonth(long startTs){
        List<XHelpEntity> xHelpEntities = new ArrayList<XHelpEntity>() ;
        String dateStr1 = TimeUtil.getMinusDayString(startTs,0) ;
        XHelpEntity entity1 = new XHelpEntity(0,dateStr1);
        xHelpEntities.add(entity1);
        String dateStr2 = TimeUtil.getMinusDayString(startTs,13) ;
        XHelpEntity entity2 = new XHelpEntity(13,dateStr2);
        xHelpEntities.add(entity2);
        String dateStr3 = TimeUtil.getMinusDayString(startTs,27) ;
        XHelpEntity entity3 = new XHelpEntity(27,dateStr3);
        xHelpEntities.add(entity3);
        return xHelpEntities ;
    }


    public static List<Long> getStartEndTimesForDays(int index,int nDays){
        long ts = System.currentTimeMillis() - (index * Constant.ONE_DAY_MS);
        List<Long> times = new ArrayList<Long>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(ts));

// 计算结束时间：明天0点
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);
        final long end = (cal.getTimeInMillis() -1000);

// 计算开始时间
        cal.add(Calendar.DAY_OF_YEAR, -nDays );
        final long start = cal.getTimeInMillis();

        times.add(start);
        times.add(end);
        return times;
    }

}
