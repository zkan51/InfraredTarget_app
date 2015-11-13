package com.infraredgun;

import android.app.Application;
import android.content.Context;

import com.uidata.CommonData;
import com.uidata.WifiAdmin;

/**
 * Created by summersunshine on 2015/11/13.
 */
public class MyApplication  extends Application {
    private static Context context;
    public void onCreate()
    {
        super.onCreate();
        CommonData.wifiAdmin = new WifiAdmin(this);
        DetectThread detectThread = new DetectThread();
        detectThread.start();
        context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
