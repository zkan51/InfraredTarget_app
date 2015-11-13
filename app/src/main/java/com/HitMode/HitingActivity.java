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
import android.widget.TextView;

import com.infraredgun.R;
import com.uidata.CommonData;

public class HitingActivity extends Activity {

	private TextView tv_start;
	private TextView tv_stop;
	private TextView tv_continue;
	private TextView tv_return;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    public AdapterRecycler adapterRecycler;
    public int arr_hitscores[] = new int[CommonData.TARGETNUM];
    public MyBroadcastReceiver myBroadcastReceiver;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_main);
        initUI();
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
        recyclerView =(RecyclerView)this.findViewById(R.id.rv_show);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterRecycler = new AdapterRecycler(arr_hitscores);
        recyclerView.setAdapter(adapterRecycler);

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

}
