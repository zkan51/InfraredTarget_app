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

import com.infraredgun.DetectThread;
import com.infraredgun.R;
import com.uidata.CommonData;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Compete_Mode_Activity extends Activity {
    private TextView tv_resttime;
    private TextView tv_hitnum;
    private TextView tv_return;
    private TextView tv_start;
    private TextView tv_mode_name;
    int nRest_time = CommonData.GAMETIME;
    int nFrenquency;
    int nDifficult;
    boolean isRunning = false;//to signal whether is started
    boolean isOver = false;//to end sending data in advance;
    boolean isActive = true;//to signal whether activity is active
    int totalHitNum = 0;
    MyBroadCastReceiver myBroadcastReceiver;
    ArrayList list = new ArrayList();
    Handler timeHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competemode);
        Intent intent = getIntent();
        nFrenquency = intent.getIntExtra("Time", 0);//how frequent it falls
        String strModeName = intent.getStringExtra("ModeName");
        nDifficult = intent.getIntExtra("Difficult", 0);
        tv_resttime = (TextView)findViewById(R.id.tv_rest_time);
        tv_return = (TextView)findViewById(R.id.tv_compete_return);
        tv_start = (TextView)findViewById(R.id.tv_compete_start);
        tv_hitnum = (TextView)findViewById(R.id.tv_hit_num);
        tv_mode_name = (TextView)findViewById(R.id.tv_competemodename);
        tv_mode_name.setText(strModeName);
        tv_resttime.setText(nRest_time+"");

        myBroadcastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        intentFilter.addAction("CompeteACK");
        registerReceiver(myBroadcastReceiver, intentFilter);

        DetectThread detectThread = new DetectThread();
        detectThread.start();

        timeHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                int ntime = (Integer)msg.obj;
                tv_resttime.setText(""+ntime);
            }
        };

        tv_start.setOnTouchListener(new TouchListener());
        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(!isOver && isRunning)
                    {
                        CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
                    }
                    isActive = false;
                    startActivity(new Intent(Compete_Mode_Activity.this, Compete_Activity.class));
                    Compete_Mode_Activity.this.finish();
                }
                return false;
            }
        });
    }

    class TouchListener implements View.OnTouchListener
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            if(!isRunning) {
                totalHitNum = 0;
                nRest_time = CommonData.GAMETIME;
                tv_hitnum.setText(totalHitNum+"");
                tv_resttime.setText(nRest_time+"");
                isRunning = true;
                isOver = false;
                TimeThread timeThread= new TimeThread();
                timeThread.start();
                RunThread runThread = new RunThread();
                runThread.start();
            }
            return false;
        }
    }
    class RunThread extends Thread//to produce random number to control target stand
    {
        public void run()
        {
            try
            {
                Thread.sleep(1000);
            }catch(Exception e)
            {
            }

            while(!isOver && isActive)
            {
                int nSize = list.size();

                if(nSize != 0) {
                    switch (nDifficult) {
                        case 1:
                        case 2:
                            int nRandom = (int) (Math.random() * nSize);
                            int ntarget = (Integer) list.get(nRandom);
                            int t = nFrenquency + (int) (Math.random() * 11 - 5);
                            CommonData.dataProcess.sendCmd(ntarget, CommonData.COMPETECMD, CommonData.STARTSTT, t, 0x00);
                            break;
                        case 3:
                            int nRandom1 = (int) (Math.random() * nSize);
                            int nRandom2 = (int) (Math.random() * nSize);
                            int ntarget1 = (Integer) list.get(nRandom1);
                            int ntarget2 = (Integer) list.get(nRandom2);
                            int t1 = nFrenquency + (int) (Math.random() * 11 - 5);
                            int t2 = nFrenquency + (int) (Math.random() * 11 - 5);
                            CommonData.dataProcess.sendCmd(ntarget1, CommonData.COMPETECMD, CommonData.STARTSTT, t1, 0x00);
                            CommonData.dataProcess.sendCmd(ntarget2, CommonData.COMPETECMD, CommonData.STARTSTT, t2, 0x00);
                            break;
                    }
                }
                try
                {
                    Thread.sleep(nFrenquency);
                }catch(Exception e)
                {
                }
            }
        }
    }
    class TimeThread extends Thread
    {
        public void run() {
            while(nRest_time > 0 && isActive)
            {
                try {
                    Thread.sleep(1000);
                } catch (Exception e)
                {
                }
                nRest_time --;
                if(nRest_time == 2)//let RunThread stop in advance
                {
                    isOver = true;
                    CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
                }
                Message message=Message.obtain();
                message.obj=nRest_time;
                timeHandler.sendMessage(message);
            }
            isRunning = false;
        }
    }
    class DetectTargetThread extends Thread{//to know what targets are used
        public void run()
        {
            while(isActive)
            {
                CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.ACKSTT, 0x00, 0x00);
                try
                {
                    Thread.sleep(10000);
                }catch(Exception e)
                {
                }
            }
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
                    if(!list.contains(iTarget)) {
                        list.add(iTarget);
                    }
                }
            }
            if(action.equals("ReceiveData"))
            {
                int hitNum = intent.getIntExtra("HitNum", 0);
                int mode = intent.getIntExtra("Mode", 0);
                if(hitNum != 0 && isRunning && mode == CommonData.COMPETECMD)
                {
                    totalHitNum++;
                    tv_hitnum.setText(""+totalHitNum);
                }

            }
        }
    }
    public void onBackPressed()
    {
        if(!isOver && isRunning)
        {
            CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
        }
        isActive = false;
        startActivity(new Intent(Compete_Mode_Activity.this, Compete_Activity.class));
        Compete_Mode_Activity.this.finish();
    }
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}
