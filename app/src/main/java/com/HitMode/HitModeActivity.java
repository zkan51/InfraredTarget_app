package com.HitMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.infraredgun.R;
import com.uidata.CommonData;

public class HitModeActivity extends Activity {

	private TextView tv_start;
	private TextView tv_stop;
	private TextView tv_continue;
	private TextView tv_return;
    private TextView tv_end;
    private TextView tv_mode;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    public AdapterRecycler adapterRecycler;
    public String arrhitscores[] = new String[CommonData.TARGETNUM];
    public int arrhitscorenum[] = new int[CommonData.TARGETNUM];
    public MyBroadcastReceiver myBroadcastReceiver;
    final int STRAT = 1;
    final int STOP = 2;
    final int CONTINUE = 3;
    final int RETURN = 4;
    int iTime = 0;
    boolean bStart = false;
    boolean bStop = false;


    Drawable dwPress;
    Drawable dwDisable;
    int Gray;
    int Black;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_main);
        dwPress = getResources().getDrawable(R.drawable.pressed);
        dwDisable= getResources().getDrawable(R.drawable.disabled);
        Gray= getResources().getColor(R.color.gray);
        Black = getResources().getColor(R.color.black);

        initUI();
        Intent intent = getIntent();
        String strModeName = intent.getStringExtra("ModeName");
        iTime = intent.getIntExtra("Time", 20);
        tv_mode.setText(strModeName);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        registerReceiver(myBroadcastReceiver, intentFilter);

	}
	private void initUI()
	{
		tv_start = (TextView)this.findViewById(R.id.tv_compete_start);
        tv_stop = (TextView)this.findViewById(R.id.tv_stop);
        tv_continue = (TextView)this.findViewById(R.id.tv_tryagain);
        tv_return = (TextView)this.findViewById(R.id.tv_compete_return);
        tv_mode = (TextView)this.findViewById(R.id.tv_competemodename);
        tv_end = (TextView)this.findViewById(R.id.tv_end);
        recyclerView =(RecyclerView)this.findViewById(R.id.rv_show);
        gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        for(int i = 0; i < CommonData.TARGETNUM; i++)
        {
            arrhitscores[i] = "0";
        }
        adapterRecycler = new AdapterRecycler(arrhitscores);
        recyclerView.setAdapter(adapterRecycler);
        TouchListener starttouchListener = new TouchListener(STRAT);
        tv_start.setOnTouchListener(starttouchListener);
        TouchListener stoptouchListener = new TouchListener(STOP);
        tv_stop.setOnTouchListener(stoptouchListener);
        TouchListener continuetouchListener = new TouchListener(CONTINUE);
        tv_continue.setOnTouchListener(continuetouchListener);
        TouchListener returnTouchListener = new TouchListener(RETURN);
        tv_return.setOnTouchListener(returnTouchListener);
        tv_end.setOnTouchListener(returnTouchListener);
        tv_start.setBackground(dwPress);
        tv_start.setTextColor(Black);
        tv_continue.setBackground(dwDisable);
        tv_continue.setTextColor(Gray);
        tv_stop.setBackground(dwDisable);
        tv_stop.setTextColor(Gray);
        tv_end.setBackground(dwPress);
        tv_end.setTextColor(Black);
	}
    public class TouchListener implements View.OnTouchListener
    {
        int iFunction;
        public TouchListener(int ifunction)
        {
            iFunction = ifunction;
        }
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                switch (iFunction)
                {
                    case STRAT:
                        CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.STARTSTT, iTime, 0x00);
                        tv_start.setBackground(dwDisable);
                        tv_start.setTextColor(Gray);
                        tv_continue.setBackground(dwPress);
                        tv_continue.setTextColor(Black);
                        tv_stop.setBackground(dwPress);
                        tv_stop.setTextColor(Black);
                        bStart = true;
                        break;
                    case STOP:
                        if(bStart&&!bStop) {
                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.PAUSESTT, 0x00, 0x00);
                            tv_stop.setBackground(dwDisable);
                            tv_stop.setTextColor(Gray);
                            tv_continue.setBackground(dwPress);
                            tv_continue.setTextColor(Black);
                            bStop = true;
                        }
                        break;
                    case CONTINUE:
                        if(bStop&&bStart)
                        {
                            tv_stop.setBackground(dwPress);
                            tv_stop.setTextColor(Black);
                            tv_continue.setBackground(dwDisable);
                            tv_continue.setTextColor(Gray);
                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.RESUMESTT, iTime, 0x00);
                            bStop = false;
                        }
                        break;
                    case RETURN:
                        if(bStart)
                        {
                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.STOPSTT, 0x00, 0x00);
                        }
                        Intent intent = new Intent(HitModeActivity.this, Hit_Activity.class);
                        startActivity(intent);
                        HitModeActivity.this.finish();
                        break;
                }
            }
            return false;
        }
    }
    public class MyBroadcastReceiver extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action.equals("ReceiveData"))
            {
                int hitNum = intent.getIntExtra("HitNum", 0);
                if( hitNum != 0 && hitNum < CommonData.TARGETNUM)
                {
                    arrhitscorenum[hitNum - 1]++;
                    arrhitscores[hitNum - 1] = ""+arrhitscorenum[hitNum - 1];
                    adapterRecycler.notifyItemChanged(hitNum - 1);
                }
            }
        }
    }
    public void onBackPressed() {
        if(bStart)
        {
            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.STOPSTT, 0x00, 0x00);
        }
        Intent intent = new Intent(HitModeActivity.this, Hit_Activity.class);
        startActivity(intent);
        HitModeActivity.this.finish();
    }
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}
