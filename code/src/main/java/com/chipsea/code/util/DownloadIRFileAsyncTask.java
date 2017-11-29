package com.chipsea.code.util;

import android.content.Context;
import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * Created by Administrator on 2017/3/9.
 */

public class DownloadIRFileAsyncTask extends AsyncTask<Void,Integer,Integer> implements ContinuousTCP.TCPConnectionListener{
    private final int mPackageSize=1024;
    private String mFileName;
    private String mPhysicalDeviceId;
    private long mSubDomainId;
    private Context mConext;
    private syncTaskUtil mSyncTask;
    private int mFormatId;
    private int mTotalRow;
    private ContinuousTCP mTcpClient;
    private String mSrvIp;
    private int mSrvPort;

    private byte[] getDataByPackageIndex(byte[] rom,int packIndex,int packageSize){
        int iIndex=packIndex* packageSize;
        if((iIndex+packageSize)>rom.length){
            return BytesUtil.subBytes(rom,iIndex,rom.length-iIndex);
        }else{
            return BytesUtil.subBytes(rom,iIndex,packageSize);
        }
    }

    private void putInt(byte[] bdest, int x, int index) {
        bdest[index + 0] = (byte) (x >> 16);
        bdest[index + 1] = (byte) (x >> 8);
        bdest[index + 2] = (byte) (x >> 0);
    }

    private byte[] String2ByteArray(String sRaw){
        sRaw=sRaw.replace(",,",",");
        String[] sArray=sRaw.split(",");
        byte[] bRet=new byte[sArray.length];
        for(int i=0;i<sArray.length;i++){
            bRet[i]=(byte)Integer.parseInt(sArray[i],16);
        }
        return bRet;
    }

    private void SendPackage(byte[] data){
       //csACUtil.IRControl(mPhysicalDeviceId, mSubDomainId, data, new VoidCallback() {
       //    @Override
       //    public void success() {
       //        mSyncTask.operationCompleted("");
       //    }

       //    @Override
       //    public void error(ACException e) {
       //        mSyncTask.operationFailed();
       //    }
       //});
    }


    /**
     * @Description 异步发送红外码库给WIFI插座
     * @param context	上下文
     * @param fileName 	红外码库名称
     * @param fid    红外码库唯一ID
     * @param physicalDeviceId 智能插座物理ID
     * @param subDomainId	 智能插座子域
     */
    public DownloadIRFileAsyncTask(Context context,String fileName,int fid,String physicalDeviceId,long subDomainId,String SrvInfo) {
        mConext=context;
        mFileName=fileName;
        mFormatId=fid;
        mPhysicalDeviceId=physicalDeviceId;
        mSubDomainId=subDomainId;
        mSyncTask=new syncTaskUtil();

        if(SrvInfo.length()>0){
            String[] arySrv=SrvInfo.split(";");
            if(arySrv.length==2){
                mTcpClient=new ContinuousTCP(this);
                mSrvIp=arySrv[0];
                mSrvPort=Integer.parseInt(arySrv[1]);
                mTcpClient.start();
            }
        }

    }

    private byte[] getDataBuffer() {
        ArrayList<byte[]> lstRaw=new ArrayList<byte[]>();

        int totalbyte=0;
        try {
            //InputStream is=mConext.getResources().getAssets().open(mFileName);
            InputStream is=new FileInputStream(mFileName);
            InputStreamReader isr=new InputStreamReader(is,"UTF-8");
            BufferedReader bfr=new BufferedReader(isr);
            String sRaw = "";

            while ((sRaw = bfr.readLine()) != null) {
                if(sRaw.trim().length()>0){
                    byte[] bRaw=String2ByteArray(sRaw.trim());
                    lstRaw.add(bRaw);
                    totalbyte+=bRaw.length;
                }
            }
            bfr.close();
            isr.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        mTotalRow=lstRaw.size();
        byte[] bRet=new byte[lstRaw.size()*3 + totalbyte];
        int iStart=0;
        int iIndex=0;
        for(int i=0;i<lstRaw.size();i++){
            putInt(bRet,iStart,iIndex);
            iIndex+=3;
            iStart+=lstRaw.get(i).length;
        }

        for(int i=0;i<lstRaw.size();i++){
            int iLen=lstRaw.get(i).length;
            System.arraycopy(lstRaw.get(i),0,bRet,iIndex,iLen);
            iIndex+=iLen;
        }

        return bRet;
    }

    private byte[] getSendIRRequst(int totalPackage){
        byte[] bRet=new byte[6];
        bRet[0]=0x04;
        if(mTotalRow==14){
            bRet[1]=0x02;
        }else if(mTotalRow==3000){
            bRet[1]=0x01;
        }else if(mTotalRow==15000){
            bRet[1]=0x00;
        }

        byte[] bTmp=BytesUtil.shortToByteArray((short)mFormatId);
        bRet[2]=bTmp[0];
        bRet[3]=bTmp[1];

        bTmp=BytesUtil.shortToByteArray((short)totalPackage);
        bRet[4]=bTmp[0];
        bRet[5]=bTmp[1];

        return bRet;
    }

    @Override
    //返回值 0--成功 1--失败
    protected Integer doInBackground(Void... params) {

        if(mSrvPort>0){
            boolean bRet=mTcpClient.connect(mSrvIp,mSrvPort);
            if(!bRet){
                return 1;
            }
        }

        //获取完整的码库二进制数组
        byte[] dataBuffer= getDataBuffer();
        int length=dataBuffer.length;
        int totalPack= length / mPackageSize;
        int lastPackLen= length % mPackageSize;
        if(lastPackLen>0) { totalPack+=1;}

        //发送请求
        byte[] bRequest=getSendIRRequst(totalPack);
        SendPackage(bRequest);
        if(!mSyncTask.startOperation(5)){
            mTcpClient.disconnect();
            mTcpClient.stop();
            return 1;
        }


        int iResult=0;
        for(int i=0;i<totalPack;i++){
            byte[] bData=getDataByPackageIndex(dataBuffer,i,mPackageSize);
            byte[] bRaw=new byte[3+bData.length];
            bRaw[0]=0x05;
            byte[] bTmp=BytesUtil.shortToByteArray((short)i);
            bRaw[1]=bTmp[0];
            bRaw[2]=bTmp[1];
            System.arraycopy(bData,0,bRaw,3,bData.length);

            float fProgess=((i+1) * 1.0f)/(totalPack * 1.0f);
            int iProgress=(int)(fProgess * 100.0f);

            boolean bSendSuccess=false;
            if(mSrvPort>0){
                for(int iLoop=0;iLoop<3;iLoop++){
                    boolean bRet=mTcpClient.send(bRaw);
                    LogUtil.i("TEST","===tcp send:" + i + " ret:" + bRet);
                    if(bRet){
                        if(!mSyncTask.startOperation(5)){
                            bSendSuccess=false;
                        }else{
                            bSendSuccess=true;
                            publishProgress(iProgress);
                            break;
                        }
                    }
                }
            }else{
                for(int iLoop=0;iLoop<3;iLoop++){
                    SendPackage(bRaw);
                    if(!mSyncTask.startOperation(5)){
                        bSendSuccess=false;
                    }else {
                        bSendSuccess=true;
                        publishProgress(iProgress);
                        break;
                    }
                }
            }
            if(!bSendSuccess){
                //超时
                iResult=1;
                break;
            }
        }

        if(mSrvPort>0){
            mTcpClient.disconnect();
            mTcpClient.stop();
        }
        return iResult;
    }


    @Override
    public void onDisconnected() {

    }

    @Override
    public void onDataReceived(byte[] buffer, String ip) {
        LogUtil.i("TEST","Received:" + BytesUtil.bytesToPrintString(buffer));
        mSyncTask.operationCompleted("");
    }


}
