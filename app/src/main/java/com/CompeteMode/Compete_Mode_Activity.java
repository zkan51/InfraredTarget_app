package com.CompeteMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.infraredgun.DetectThread;
import com.infraredgun.R;
import com.uidata.CommonData;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Compete_Mode_Activity extends Activity {
    private TextView tv_resttime;
    private TextView tv_hitnum;
    private TextView tv_return;
    private TextView tv_start;
    private TextView tv_mode_name;
    int nRest_time;
    int nFrenquency;
    int nDifficult;
    boolean isRunning = false;//to signal whether is started
    boolean isOver = false;//to end sending data in advance;
    boolean isActive = true;//to signal whether activity is active
    int totalHitNum = 0;
    MyBroadCastReceiver myBroadcastReceiver;
    ArrayList list = new ArrayList();
    Handler timeHandler;
    Handler showHandler;

    Drawable dwPress;
    Drawable dwDisable;
    int Gray;
    int Black;

    boolean isHit = false;

    int state[] = new int[3];

    static int NOHIT = 1;
    static int SEND = 2;

    Handler taskHandler;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competemode);
        dwPress = getResources().getDrawable(R.drawable.pressed);
        dwDisable = getResources().getDrawable(R.drawable.disabled);
        Gray = getResources().getColor(R.color.gray);
        Black = getResources().getColor(R.color.black);
        Intent intent = getIntent();
        nFrenquency = intent.getIntExtra("Time", CommonData.COMMONTIME);//how frequent it falls
        String strModeName = intent.getStringExtra("ModeName");
        nDifficult = intent.getIntExtra("Difficult", 1);
        tv_resttime = (TextView) findViewById(R.id.tv_rest_time);
        tv_return = (TextView) findViewById(R.id.tv_compete_return);
        tv_start = (TextView) findViewById(R.id.tv_compete_start);
        tv_hitnum = (TextView) findViewById(R.id.tv_hit_num);
        tv_mode_name = (TextView) findViewById(R.id.tv_competemodename);
        tv_mode_name.setText(strModeName);

        nRest_time = PreferenceUtils.getPrefInt(this, PreferenceConstants.GameTime, 180);

        isRunning = false;
        isOver = false;
        isActive = true;

        list.add(1);
        list.add(2);
        list.add(3);

        if (nRest_time == 0) {
            nRest_time = CommonData.GameTime;
            PreferenceUtils.setPrefInt(this, PreferenceConstants.GameTime, nRest_time);
        }
        tv_resttime.setText(nRest_time + "");
        myBroadcastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        //intentFilter.addAction("CompeteACK");
        registerReceiver(myBroadcastReceiver, intentFilter);

        CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);

      //  DetectTargetThread detectThread = new DetectTargetThread();
      //  detectThread.start();

        timeHandler = new Handler() {
            public void handleMessage(Message msg) {
                int ntime = (Integer) msg.obj;
                tv_resttime.setText("" + ntime);
            }
        };

        showHandler = new Handler()
        {
            public void handleMessage(Message msg) {
                tv_start.setBackground(dwPress);
                tv_start.setTextColor(Black);
                tv_resttime.setText(""+nRest_time);
                tv_hitnum.setText("" + 0);
            }
        };

        taskHandler = new Handler()
        {
            public void handleMessage(Message msg) {
                int nObj = (Integer) msg.obj;
                if(nObj < 3 && state[nObj] != NOHIT)
                {
                    state[nObj] = NOHIT;
                }
            }
        };

        timer = new Timer();
        tv_start.setOnTouchListener(new TouchListener());
        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isOver && isRunning) {
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

    class TouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if (!isRunning) {
                tv_start.setBackground(dwPress);
                tv_start.setTextColor(Gray);
                totalHitNum = 0;
                tv_hitnum.setText(totalHitNum + "");
                tv_resttime.setText(nRest_time + "");
                isRunning = true;
                isOver = false;
                TimeThread timeThread = new TimeThread();
                timeThread.start();
                RunThread runThread = new RunThread();
                runThread.start();
            }
            return false;
        }
    }

    class RunThread extends Thread//to produce random number to control target stand
    {
        public void run() {
            for(int i = 0; i < 3; i++)
            {
                state[i] = NOHIT;
            }
            switch (nDifficult) {
                case 1:
                    randomTask();
                    break;
                case 2:
                case 3:
                    randomTask();
                    try
                    {
                        Thread.sleep(600);
                    }catch(Exception e)
                    {
                    }
                    randomTask();
                    break;
             }
            while (!isOver && isActive) {
                int time = nFrenquency/1000 + 1;
                isHit = false;
                while(time > 0 && !isHit)
                {
                    try
                    {
                        Thread.sleep(1000);
                    }catch(Exception e)
                    {

                    }
                    time--;
                }
                if(nDifficult == 1)
                {
                    randomTask();
                }
                else
                {
                    int count = 0;
                    for(int i = 0; i < 3; i++)
                    {
                        if(state[i] == NOHIT)
                        {
                            count ++;
                        }
                    }
                    switch(count)
                    {
                        case 1:
                            break;
                        case 2:
                            randomTask();
                            break;
                        case 3:
                            randomTask();
                            try
                            {
                                Thread.sleep(600);
                            }catch(Exception e)
                            {
                            }
                            randomTask();
                            break;
                    }
                }
             }
        }
    }

    class TimeThread extends Thread {
        public void run() {
            while (nRest_time > 0 && isActive) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                nRest_time--;
                if (nRest_time == 2)//let RunThread stop in advance
                {
                    isOver = true;
                    isHit = true;
                    CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
                }
                Message message = Message.obtain();
                message.obj = nRest_time;
                timeHandler.sendMessage(message);
            }
            if (nRest_time == 0) {
                isRunning = false;
                isOver = false;
                Message msg = Message.obtain();
                showHandler.sendMessage(msg);
                nRest_time = PreferenceUtils.getPrefInt(Compete_Mode_Activity.this, PreferenceConstants.GameTime, 0);
            }
        }
    }

    class DetectTargetThread extends Thread {//to know what targets are used
        public void run() {
                try {
                    Thread.sleep(600);
                } catch (Exception e) {
                }
        }
    }
    void randomTask()
    {
        ArrayList arrayList = new ArrayList();//store targets to produce random target
        for(int i = 0; i< 3; i++)
        {
            if(state[i] == NOHIT) {
                arrayList.add(i);
            }
        }
        int r =(int) Math.random() * arrayList.size();
        final int t = (Integer)arrayList.get(r);
        state[t] = SEND;
        CommonData.dataProcess.sendCmd( t + 1,CommonData.COMPETECMD, CommonData.STARTSTT, nFrenquency/1000, 0x00);
        TimerTask task = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.obj = t;
                taskHandler.sendMessage(msg);
            }
        };
        timer.schedule(task, nFrenquency);
    }
     class MyBroadCastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*if (action.equals("CompeteACK")) {
                int iTarget = intent.getIntExtra("TargetExist", 0);
                if (iTarget != 0) {
                    if (!list.contains(iTarget)) {
                        list.add(iTarget);
                    }
                }
            }*/
            if (action.equals("ReceiveData")) {
                int hitNum = intent.getIntExtra("HitNum", 0);
                if (hitNum != 0 && isRunning ) {
                    totalHitNum++;
                    isHit = true;
                    state[hitNum - 1] = NOHIT;
                    tv_hitnum.setText("" + totalHitNum);
                }
            }
        }
    }

    public void onBackPressed() {
        if (!isOver && isRunning) {
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
