package com.cloudpos.aidl.tester;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cloudpos.utils.Logger;
import com.cloudpos.utils.TextViewUtil;
import com.wizarpos.wizarviewagentassistant.aidl.IAPNManagerService;



public class MainActivity extends AbstractActivity implements OnClickListener, ServiceConnection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_test1 = (Button) this.findViewById(R.id.btn_test1);
        Button btn_test2 = (Button) this.findViewById(R.id.btn_test2);

        log_text = (TextView) this.findViewById(R.id.text_result);
        log_text.setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById(R.id.settings).setOnClickListener(this);
        btn_test1.setOnClickListener(this);
        btn_test2.setOnClickListener(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == R.id.log_default) {
                    log_text.append("\t" + msg.obj + "\n");
                } else if (msg.what == R.id.log_success) {
                    String str = "\t" + msg.obj + "\n";
                    TextViewUtil.infoBlueTextView(log_text, str);
                } else if (msg.what == R.id.log_failed) {
                    String str = "\t" + msg.obj + "\n";
                    TextViewUtil.infoRedTextView(log_text, str);
                } else if (msg.what == R.id.log_clear) {
                    log_text.setText("");
                }
            }
        };
        bindAPNManagerService();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(this);
    }

    public void bindAPNManagerService() {
        try {
            startConnectService(MainActivity.this,
                    "com.wizarpos.wizarviewagentassistant",
                    "com.wizarpos.wizarviewagentassistant.APNManagerService", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean startConnectService(Context mContext, String packageName, String className, ServiceConnection connection) {
        boolean isSuccess = startConnectService(mContext, new ComponentName(packageName, className), connection);
        writerInLog("bind service result : " + isSuccess, R.id.log_default);
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

    boolean enable = true;

    @Override
    public void onClick(View arg0) {
        int index = arg0.getId();
        if (index == R.id.btn_test1) {
            try {
                AidlHelper.getInstance(this).enableAirPlaneMode(enable, completionListener);
                enable = !enable;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (index == R.id.btn_test2) {
            try {
                String r1 = iapnManagerService.add("Att", "iot011.com.attz");
                writerInLog("add APN result : " + r1 + "\n", R.id.log_default);
                boolean r2 = iapnManagerService.setSelected("Att");
                writerInLog("select APN result : " + r1 + "\n", R.id.log_default);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (index == R.id.settings) {
            log_text.setText("");
        }
    }

    private boolean enableBlocked = true;

    private IAPNManagerService iapnManagerService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            String des = service.getInterfaceDescriptor();
            if (des.contains("IAPNManagerService")) {
                iapnManagerService = IAPNManagerService.Stub.asInterface(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private AidlHelper.CompletionListener completionListener = new AidlHelper.CompletionListener() {
        @Override
        public void onTaskCompleted(boolean success, String errMsg) {
            writerInSuccessLog("enableAirPlaneMode(" + enable + ") result:" + success + "\n");
        }
    };

}
