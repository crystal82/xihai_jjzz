package com.chipsea.code.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ContinuousTCP {
    public static final String TAG = "TEST";

    private Socket s = null;

    private String ip = "192.168.1.1";
    private int port = 2000;

    private boolean isRunning = false;
    private TCPService service = null;
    private InetAddress inetAddress = null;

    private boolean isConnected = false;

    private TCPConnectionListener mTCPConnectionListener = null;

    public ContinuousTCP( TCPConnectionListener listener) {
        this.mTCPConnectionListener = listener;
    }

    @SuppressLint("NewApi")
    public void start() {
        if(!this.isRunning) {
            this.service = new TCPService();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                this.service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
            else
                this.service.execute((Void[])null);
            this.isRunning = true;
        }
    }

    public void stop() {
        if(this.isRunning) {
            this.service.killTask();
            this.isRunning = false;
        }
        disconnect();
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public int getPort() {
        return this.port;
    }

    public InetAddress getTargetInetAddress() {
        return this.inetAddress;
    }

    @SuppressLint("NewApi")
    private class TCPService extends AsyncTask<Void, Void, Void> {
        Boolean TASK_STATE = true;

        public TCPService() {
            TASK_STATE = true;
        }

        public void killTask() {
            TASK_STATE = false;
        }

        protected Void doInBackground(Void... params) {
            s = null;
            while (TASK_STATE) {
                while (isConnected && s != null) {
                    try {
                        s.setSoTimeout(1000);
                        //BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        byte[] ch_array = new byte[32];
                        int len = s.getInputStream().read(ch_array, 0, 32);
                        Log.i(TAG, "read socket:" + len);

                        if (len > 0) {
                            final byte[] bBuffer = new byte[len];
                            System.arraycopy(ch_array, 0, bBuffer, 0, len);

                            if (mTCPConnectionListener != null) {
                                final String hostAdderss = inetAddress.getHostAddress();
                                mTCPConnectionListener.onDataReceived(bBuffer, hostAdderss);
                            }
                        } else if (len < 0) {
                            if (mTCPConnectionListener != null) {
                                mTCPConnectionListener.onDisconnected();
                            }
                            isConnected=false;
                            s.close();
                            s = null;
                        }

                    } catch (NullPointerException e) {
                        if (mTCPConnectionListener != null) {
                            mTCPConnectionListener.onDisconnected();
                        }
                        isConnected = false;
                    } catch (SocketTimeoutException e) {
                        //e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    public interface TCPConnectionListener {
        public void onDisconnected();
        public void onDataReceived(byte[] buffer, String ip);
    }

    /***********************************************************************************************/

    public int getTargetPort() {
        return this.port;
    }

    public String getTargetIP() {
        return this.ip;
    }

//    @SuppressLint({ "NewApi" })
//    public void connect(String ip, int port) {
//        this.ip = ip;
//        this.port = port;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//            new TCPConnection(ip, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
//        else
//            new TCPConnection(ip, null).execute((Void[])null);
//    }

    @SuppressLint("NewApi")
    public void connect(String ip, int port, ConnectionCallback callback) {
        this.ip = ip;
        this.port = port;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new TCPConnection(ip, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
        else
            new TCPConnection(ip, callback).execute((Void[])null);
    }

    public boolean connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
        boolean bRet=false;
        try {
            s = new Socket();
            s.connect((new InetSocketAddress(InetAddress.getByName(ip), port)), 5000);

            inetAddress = s.getInetAddress();

            final String hostName = inetAddress.getHostName();
            final String hostAdderss = inetAddress.getHostAddress();
            isConnected = true;

            bRet=true;
        } catch (final UnknownHostException e) {
            bRet=false;
        } catch (final IOException e) {
            bRet=false;
        }

        return bRet;
    }

    @SuppressLint("NewApi")
    private class TCPConnection extends AsyncTask<Void, Void, Void> {
        private ConnectionCallback callback = null;
        private String ip = null;

        public TCPConnection(String ip, ConnectionCallback callback) {
            this.callback = callback;
            this.ip = ip;
        }

        protected Void doInBackground(Void... params) {
            try {
                s = new Socket();
                s.connect((new InetSocketAddress(InetAddress.getByName(ip), port)), 5000);

                inetAddress = s.getInetAddress();

                final String hostName = inetAddress.getHostName();
                final String hostAdderss = inetAddress.getHostAddress();
                isConnected = true;

                if(callback != null) {
                    callback.onConnected(hostName, hostAdderss);
                }
            } catch (final UnknownHostException e) {
                if(callback != null) {
                    callback.onConnectionFailed(ip, e);
                }
            } catch (final IOException e) {
                if(callback != null) {
                    callback.onConnectionFailed(ip, e);
                }
            }
            return null;
        }
    }

    public void disconnect() {
        if(this.s != null) {
            try {
                this.s.close();
            } catch (IOException e) { }
            this.s = null;
        }
    }

//    public void send(byte[] data) {
//        if(this.s != null)
//            new TCPSend(data, null).execute();
//    }

    public void send(byte[] data, SendCallback callback) {
        if(this.s != null)
            new TCPSend(data, callback).execute();
    }

    public boolean send(byte[] data) {
        boolean bRet=false;
        if(this.s == null) return bRet;

        try {
            s.getOutputStream().write(data);
            s.getOutputStream().flush();
            bRet=true;
        } catch (final IOException e) {
            bRet=false;
        }

        return bRet;
    }

    @SuppressLint("NewApi")
    private class TCPSend extends AsyncTask<Void, Void, Void> {
        private byte[] sendData = null;
        private SendCallback callback = null;

        public TCPSend(byte[] data, SendCallback callback) {
            this.sendData = data;
            this.callback = callback;
        }

        protected Void doInBackground(Void... params) {
            try {
//                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//                out.write(message);
//                out.flush();
                LogUtil.i("TEST","begin write");
                s.getOutputStream().write(sendData);
                s.getOutputStream().flush();
                LogUtil.i("TEST","end write");

                if(callback != null) {
                    callback.onSuccess(ip);
                }
            } catch (final IOException e) {
                //e.printStackTrace();
                if(callback != null) {
                    callback.onFailed(ip, e);
                }
            }

            return null;
        }
    }

    public interface ConnectionCallback {
        public void onConnected(String hostName, String hostAddress);
        public void onConnectionFailed(String ip, Exception e);
    }

    public interface SendCallback {
        public void onSuccess(String ip);
        public void onFailed(String ip, Exception e);
    }
}