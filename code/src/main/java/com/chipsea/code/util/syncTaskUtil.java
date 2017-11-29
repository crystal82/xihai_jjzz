package com.chipsea.code.util;

import android.os.Handler;

/**
 * Created by Administrator on 2017/3/9.
 */

public class syncTaskUtil {
    private final Object mLock = new Object();
    private boolean mOperationFailed;
    private boolean mOperationInProgress;
    private Handler mHandler=new Handler();
    private Object mResult;

    public Object getResult() {
        return mResult;
    }

    public void operationFailed() {
        mHandler.removeCallbacks(operationTimeout);
        synchronized (this.mLock)
        {
            if (this.mOperationInProgress)
            {
                this.mOperationInProgress = false;
                this.mOperationFailed = true;
                this.mLock.notifyAll();
            }
            return;
        }
    }

    public void operationCompleted(Object result) {
        mResult=result;
        mHandler.removeCallbacks(operationTimeout);
        synchronized (this.mLock)
        {
            if (this.mOperationInProgress)
            {
                this.mOperationInProgress = false;
                this.mOperationFailed = false;
                this.mLock.notifyAll();
            }
            return;
        }
    }

    private Runnable operationTimeout=new Runnable() {
        @Override
        public void run() {
            operationFailed();
        }
    };

    public boolean startOperation(int otSeconds){
        synchronized (this.mLock)
        {
            this.mOperationInProgress = true;
            this.mOperationFailed = false;
        }

        mHandler.postDelayed(operationTimeout,otSeconds * 1000);

        synchronized (this.mLock)
        {
            if (this.mOperationInProgress)
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

        return !mOperationFailed;
    }
}
