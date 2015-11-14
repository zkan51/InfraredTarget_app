package com.HitMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView tv_mode;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    public AdapterRecycler adapterRecycler;
    public int arr_hitscores[] = new int[CommonData.TARGETNUM];
    public MyBroadcastReceiver myBroadcastReceiver;
    final int STRAT = 1;
    final int STOP = 2;
    final int CONTINUE = 3;
    final int RETURN = 4;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_main);
        initUI();
        Intent intent = getIntent();
        String strModeName = intent.getStringExtra("ModeName");
        int iTime = intent.getIntExtra("Time", 0);
        tv_mode.setText(strModeName);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        registerReceiver(myBroadcastReceiver, intentFilter);

	}
	private void initUI()
	{
		tv_start = (TextView)this.findViewById(R.id.tv_start);
        tv_stop = (TextView)this.findViewById(R.id.tv_stop);
        tv_continue = (TextView)this.findViewById(R.id.tv_continue);
        tv_return = (TextView)this.findViewById(R.id.tv_return);
        tv_mode = (TextView)this.findViewById(R.id.tv_mode);
        recyclerView =(RecyclerView)this.findViewById(R.id.rv_show);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterRecycler = new AdapterRecycler(arr_hitscores);
        recyclerView.setAdapter(adapterRecycler);
        TouchListener starttouchListener = new TouchListener(STRAT);
        tv_start.setOnTouchListener(starttouchListener);
        TouchListener stoptouchListener = new TouchListener(STOP);
        tv_start.setOnTouchListener(starttouchListener);
        TouchListener continuetouchListener = new TouchListener(CONTINUE);
        tv_start.setOnTouchListener(continuetouchListener);
        TouchListener returnTouchListener = new TouchListener(RETURN);
        tv_start.setOnTouchListener(returnTouchListener);


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
                        break;
                    case STOP:
                        break;
                    case CONTINUE:
                        break;
                    case RETURN:
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
                if(hitNum != 0)
                {
                    arr_hitscores[hitNum - 1]++;
                    adapterRecycler.notifyItemChanged(hitNum - 1);
                }
            }
        }
    }
    private class ModeThread extends Thread
    {
        public void run()
        {

        }
    }

}
