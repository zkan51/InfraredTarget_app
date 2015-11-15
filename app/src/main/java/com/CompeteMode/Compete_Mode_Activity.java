package com.CompeteMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.infraredgun.R;
import com.uidata.CommonData;

import java.util.Set;
import java.util.TreeSet;

public class Compete_Mode_Activity extends Activity {
    private TextView tv_resttime;
    private TextView tv_hitnum;
    private TextView tv_return;
    private TextView tv_start;
    private TextView tv_mode_name;
    int iRest_time;
    boolean isRunning = false;
    int totalHitNum = 0;
    MyBroadCastReceiver myBroadcastReceiver;
    Set set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competemode);
        Intent intent = getIntent();
        iRest_time = intent.getIntExtra("Time", 0);
        String strModeName = intent.getStringExtra("ModeName");
        tv_resttime = (TextView)findViewById(R.id.tv_rest_time);
        tv_return = (TextView)findViewById(R.id.tv_compete_return);
        tv_start = (TextView)findViewById(R.id.tv_compete_start);
        tv_hitnum = (TextView)findViewById(R.id.tv_hit_num);
        tv_mode_name = (TextView)findViewById(R.id.tv_competemodename);
        tv_mode_name.setText(strModeName);
        myBroadcastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        registerReceiver(myBroadcastReceiver, intentFilter);

    }
    class TouchListener implements View.OnTouchListener
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            if(!isRunning) {
                CommonData.dataProcess.sendCmd(0x00, 0x09, 0x01, 0x00, 0x00);
                totalHitNum = 0;
            }
            return false;
        }
    }
    class MyBroadCastReceiver extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action.equals("CompeteACK"))
            {
                int iTarget = intent.getIntExtra("TargetExist", 0);
                if(iTarget != 0)
                {
                    set.add(iTarget);
                }
            }
            if(action.equals("ReceiveData"))
            {

                int hitNum = intent.getIntExtra("HitNum", 0);
                if(hitNum != 0)
                {
                    totalHitNum++;
                }
                tv_hitnum.setText(""+totalHitNum);
            }
        }
    }

}
