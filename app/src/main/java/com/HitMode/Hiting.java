package com.HitMode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.infraredgun.R;
import com.uidata.CommonData;

public class Hiting extends Activity {

	private TextView tv_start;
	private TextView tv_stop;
	private TextView tv_continue;
	private TextView tv_return;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_main);
        initUI();

        if(CommonData.dataProcess.socket.isClosed())
        {
            CommonData.dataProcess.startConn(CommonData.IP, CommonData.PORT);
        }

	}
	private void initUI()
	{
		tv_start = (TextView)this.findViewById(R.id.tv_start);
        tv_stop = (TextView)this.findViewById(R.id.tv_stop);
        tv_continue = (TextView)this.findViewById(R.id.tv_continue);
        tv_return = (TextView)this.findViewById(R.id.tv_return);
        recyclerView =(RecyclerView)this.findViewById(R.id.rv_show);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);;
	}
}
