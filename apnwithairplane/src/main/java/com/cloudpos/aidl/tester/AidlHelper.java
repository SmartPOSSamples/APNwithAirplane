package com.cloudpos.aidl.tester;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cloudpos.utils.Logger;
import com.wizarpos.wizarviewagentassistant.aidl.ISystemExtApi;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class AidlHelper implements ServiceConnection {
    private static volatile AidlHelper instance = null;

    private Context mContext;
    private ISystemExtApi systemExtApi;

    private AidlHelper(Context context) {
        this.mContext = context;
    }

    public static AidlHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (AidlHelper.class) {
                if (instance == null) {
                    instance = new AidlHelper(context);
                }
            }
        }
        return instance;
    }

    private Thread th;
    private Semaphore semaphore;

    private boolean waitAidlApi() {
        if (semaphore != null) {
            try {
                if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
                    Logger.debug("waitAidlApi success.");
                } else {
                    Logger.debug("waitAidlApi failed. reason: timeout");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean enableAirPlaneMode(final boolean enable, CompletionListener listener) {
        Logger.debug("enableAirPlaneMode(%s)", enable);
        if (th == null || th.getState() == Thread.State.TERMINATED) {
            semaphore = new Semaphore(0);
            startConnectService(this.mContext,
                    "com.wizarpos.wizarviewagentassistant",
                    "com.wizarpos.wizarviewagentassistant.SystemExtApiService", this);
            th = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        if (waitAidlApi()) {
                            systemExtApi.enableAirplaneMode(enable);
                            if (listener != null) {
                                listener.onTaskCompleted(true, null);
                            }
                        } else {
                            if (listener != null) {
                                listener.onTaskCompleted(false, "bind service failed!");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mContext.unbindService(AidlHelper.this);
                        systemExtApi = null;
                        semaphore = null;
                    }
                }
            };
            th.start();
            return true;
        }
        Logger.debug("enableAirPlaneMode failed! reason: task has started.");
        return false;
    }


    protected boolean startConnectService(Context mContext, String packageName, String className, ServiceConnection connection) {
        boolean isSuccess = startConnectService(mContext, new ComponentName(packageName, className), connection);
        return isSuccess;
    }

    protected boolean startConnectService(Context context, ComponentName comp, ServiceConnection connection) {
        Intent intent = new Intent();
        intent.setPackage(comp.getPackageName());
        intent.setComponent(comp);
        boolean isSuccess = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        comp = context.startService(intent);
        Logger.debug("bind service (%s, %s)", isSuccess, comp.getPackageName(), comp.getClassName());
        return isSuccess;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.systemExtApi = ISystemExtApi.Stub.asInterface(service);
        this.semaphore.release();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


    public interface CompletionListener {
        void onTaskCompleted(boolean success, String errMsg);
    }

}
