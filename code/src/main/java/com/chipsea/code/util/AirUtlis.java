package com.chipsea.code.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.chipsea.code.db.FormatsTable;
import com.chipsea.code.db.ModelTable;
import com.chipsea.mode.entity.AirControlEntity;
import com.chipsea.mode.entity.AirModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * Created by Administrator on 2017/3/3.
 */

public class AirUtlis {
    private static final String TAG = "AirUtlis";
    public static String baseCodesPath = "http://chips-cloud.tookok.cn/IRCodes/AC/" ;
    public static long dowloadText(Context context, String textName) {
        String downloadUrl = baseCodesPath + textName;
        // 创建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
/*
 * 设置在通知栏是否显示下载通知(下载进度), 有 3 个值可选:
 *    VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
 *    VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
 *    VISIBILITY_HIDDEN:                    始终不显示通知
 */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
// 设置通知的标题和描述
        request.setTitle("通知标题XXX");
        request.setDescription("下载空调文件中~");
/*
 * 设置允许使用的网络类型, 可选值:
 *     NETWORK_MOBILE:      移动网络
 *     NETWORK_WIFI:        WIFI网络
 *     NETWORK_BLUETOOTH:   蓝牙网络
 * 默认为所有网络都允许
 */
// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
// 添加请求头
// request.addRequestHeader("User-Agent", "Chrome Mozilla/5.0");
        // 设置下载文件的保存位置
        File f = new File(FileUtil.PATH_CODES);
        if(!f.exists())
        {
            f.mkdir();
        }
        File saveFile = new File(FileUtil.PATH_CODES, textName);
        if(saveFile.exists()){
            saveFile.delete() ;
        }
        request.setDestinationUri(Uri.fromFile(saveFile));
/*
 * 2. 获取下载管理器服务的实例, 添加下载任务
 */
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    // 将下载请求加入下载队列, 返回一个下载ID
        long downloadId = manager.enqueue(request);
// 如果中途想取消下载, 可以调用remove方法, 根据返回的下载ID取消下载, 取消下载后下载保存的文件将被删除
// manager.remove(downloadId);
        return downloadId;
    }


    public static void writeDb(Context context){
        String DB_PATH = "/data/data/"  + context.getPackageName() + "/databases/";
        String DB_NAME = "1.db";

        // 检查 SQLite 数据库文件是否存在
        if (!(new File(DB_PATH + DB_NAME)).exists()) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                InputStream is = context.getAssets().open(DB_NAME);
                // 输出流,在指定路径下生成db文件
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

                // 文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                // 关闭文件流
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] getSendInstruct(byte conoff,byte cmode ,byte ctemp,byte cwind ,byte cwinddir, String index){

        String[] strSplit = index.split(",") ;
        byte[] result = new byte[strSplit.length+5] ;
        result[0] = conoff;
        result[1] = cmode;
        result[2] = ctemp;
        result[3] = cwind;
        result[4] = cwinddir;
        if(strSplit.length >0) {
            for (int i = 0; i <strSplit.length ; i++) {
                String hexStr = strSplit[i] ;
                result[i+5]=(byte)Integer.parseInt(hexStr, 16);
            }
        }
        return result ;
    }

//    //智能匹配java源程序
//   public static  String getID(Context context,String dumps){
//        if(TextUtils.isEmpty(dumps)) return "" ;
//       String dump = dumps.replace(",","");
//       String[][] rds;
//       String bstr= hex2binString(dump);
//
//        rds = FormatsTable.getInstance(context).getAllMaches();
//
//        int rcnt = rds.length;
//        String rowstring="";
//        for(int i=0; i<rds.length; i++){
//            rowstring= rds[i][1];
//
//            int score = 0;
//            int lens = rowstring.length() ;
//            int lens1 = bstr.length();
//            if (lens == lens1)
//                score+=5000;
//
//            if(lens > lens1){
//                lens = lens1;
//            }
//
//            for(int j=0;j<lens;j++)
//            {
//                if (rowstring.substring(j, j + 1)==bstr.substring(j, j + 1))
//                    score+=100;
//            }
//            rds[i][2] = score + "";
//        }
//
//        for(int i=0; i< rcnt-1; i++){
//            for(int j=i+1; j< rcnt; j++){
//                if(Integer.parseInt(rds[i][2]) < Integer.parseInt(rds[j][2])){
//                    String tmp = rds[i][0];
//                    String tmp1 = rds[i][1];
//                    String tmp2=rds[i][2];
//
//                    rds[i][0] = rds[j][0];
//                    rds[i][1] = rds[j][1];
//                    rds[i][2] = rds[j][2];
//
//                    rds[j][0] = tmp;
//                    rds[j][1] = tmp1;
//                    rds[j][2] = tmp2;
//                }
//            }
//        }
//        String rets = rds[0][0]  +  "," + rds[1][0]   +  "," + rds[2][0]  +  "," + rds[3][0] +  "," + rds[4][0];
//        Log.d("abc","getid rets = " + rets);
//        return rets;
//
//    }
   public static  List<String> getID(Context context,String dumps){
        if(TextUtils.isEmpty(dumps)) return null ;
       String dump = dumps.replace(",","");
       String[][] rds;
       String dumpMatch= hex2binString(dump);

        rds = FormatsTable.getInstance(context).getAllMaches();

       int rcnt = rds.length;
       String tempMacth="";
       for(int i=0; i<rcnt; i++){
           if (!TextUtils.isEmpty(rds[i][0])){
               tempMacth= rds[i][0];
               int score = 0;
               int lens = tempMacth.length();
               int lens1 = dumpMatch.length();
               if (lens == lens1)
                   score+=5000;
               if(lens > lens1){
                   lens = lens1;
               }
               for (int j = 0; j < lens; j++) {
                   if (tempMacth.substring(j, j + 1) == dumpMatch.substring(j, j + 1))
                       score += 100;
               }
               rds[i][1]= score + "";
           }
       }
       int[][] rds1 = ModelTable.getInstance(context).getAllBrandRand();
       for (int i = 0; i < rds1.length; i++) {
           int fid = rds1[i][0] ;
           rds1[i][1]=rds1[i][1]+ Integer.parseInt(rds[fid][1]);
       }
       rcnt = rds1.length ;
       for(int i=0; i< rcnt-1; i++){
           for(int j=i+1; j< rcnt; j++){
               if(rds1[i][1] < rds1[j][1]){
                   int tmp = rds1[i][0];
                   int tmp1 = rds1[i][1];

                   rds1[i][0] = rds1[j][0];
                   rds1[i][1]  = rds1[j][1];

                   rds1[j][0]  =tmp;
                   rds1[j][1]  =tmp1;
               }
           }
       }
       List<String> result = new ArrayList<String>() ;
       for (int i=0 ;i<5 ;i++){
           String tempResult =  rds1[i][0] + "" ;
           if(!result.contains(tempResult)){
               result.add(tempResult);
           }
       }
        Log.d("abc","result = " + result.toString());
        return result;

    }
    static String hex2binString(String hexString)
    {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++)
        {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
    public static int getIndex(AirControlEntity entity,AirModel model, int key){
        if(model.getM_key_squency() == 15000){
            return getIndexBy15000(entity,key) ;
        }else if(model.getM_key_squency() == 3000){
            return getIndexBy3000(entity) ;
        }
        return 0 ;
    }
    public static int getIndexBy15000(AirControlEntity entity,int key){
        int index = entity.getConoff() * 7500 + entity.getCmode() * 1500 + entity.getCtemp() * 100 + entity.getCwind() * 25 + entity.getCwinddir() * 5 + key + 1 ;
         return index ;
    }
    public static int getIndexBy3000(AirControlEntity entity){
        int index = entity.getConoff() * 1500 + entity.getCmode() *300 + entity.getCtemp() * 20 + entity.getCwind() * 5 + entity.getCwinddir()  + 1 ;
        return index ;
    }
    public static byte[] getControlResult(AirControlEntity entity,AirModel model, int key){
        byte[] pre = getPreviousByte(entity) ;
        byte[] text = getTextLineByte(model.getM_keyfile(),getIndex(entity,model,key)) ;
        byte[] result = concat(pre,text) ;
        return  result;
    }



    public static <T> byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    public static byte[] getPreviousByte(AirControlEntity entity){
        byte[] previous = new byte[5] ;
        previous[0] = entity.getConoff() ;
        previous[1] = entity.getCmode() ;
        previous[2] = entity.getCtemp() ;
        previous[3] = entity.getCwind() ;
        previous[4] = entity.getCwinddir() ;
        return previous ;
    }
    public static byte[] getTextLineByte(int keyfile, int index){
        Log.i(TAG, "index: " + index);
        File file = new File(FileUtil.PATH_CODES, keyfile + ".txt") ;
        if(!file.exists()){
            return  new byte[]{} ;
        }
        try {
            InputStream is=new FileInputStream(file);
            InputStreamReader isr=new InputStreamReader(is,"UTF-8");
            BufferedReader bfr=new BufferedReader(isr);
            String sRaw = "";
            int tempIndex = 1 ;
            while ((sRaw = bfr.readLine()) != null) {
                if(tempIndex == index){
                    break;
                }
                tempIndex ++ ;
            }
            byte[] bRet=String2ByteArray(sRaw) ;
            LogUtil.i(TAG,"Output:" + BytesUtil.bytesToHexString(bRet));
            return bRet;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return   new byte[]{};
    }
    private static byte[] String2ByteArray(String sRaw){
        sRaw=sRaw.replace(",,",",");
        String[] sArray=sRaw.split(",");
        byte[] bRet=new byte[sArray.length];
        for(int i=0;i<sArray.length;i++){
            bRet[i]=(byte)Integer.parseInt(sArray[i],16);
        }
        return bRet;
    }
}
