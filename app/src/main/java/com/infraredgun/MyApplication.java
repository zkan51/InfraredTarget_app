package com.infraredgun;

import android.app.Application;
import android.content.Context;

import com.uidata.CommonData;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;
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
        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.Mode1,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.Mode1, 10000);
        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.Mode2,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.Mode2, 20000);
        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.Mode3,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.Mode3,30000);
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
