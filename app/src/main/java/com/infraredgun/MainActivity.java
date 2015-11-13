package com.infraredgun;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.uidata.CommonData;
import com.uidata.WifiAdmin;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonData.wifiAdmin = new WifiAdmin(this);
        CommonData.wifiAdmin.addNetwork(CommonData.wifiAdmin.CreateWifiInfo(CommonData.SSID, CommonData.PSWD, CommonData.NETTYPE));



    }

}
