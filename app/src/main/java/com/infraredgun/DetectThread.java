package com.infraredgun;



import com.uidata.CommonData;
import com.uidata.WifiAdmin;

/**
 * Created by summersunshine on 2015/11/12.
 */
public class DetectThread extends Thread {
    public void run()
    {
        while(CommonData.isRunning)
        {
            if(!CommonData.wifiAdmin.isConnected())
            {
                CommonData.wifiAdmin.openWifi();
                CommonData.wifiAdmin.addNetwork(CommonData.wifiAdmin.CreateWifiInfo(CommonData.SSID, CommonData.PSWD, CommonData.NETTYPE));
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CommonData.dataProcess.startConn();
            }

        }
    }
}
