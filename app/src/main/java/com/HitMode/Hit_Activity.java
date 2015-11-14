package com.HitMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.infraredgun.MainActivity;
import com.infraredgun.R;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;

public class Hit_Activity extends Activity {
    private TextView tv_mode1;
    private TextView tv_mode2;
    private TextView tv_mode3;
    private TextView tv_return;
    private TextView tv_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoot_main);
        tv_mode1 = (TextView)findViewById(R.id.tv_mode1);
        tv_mode2 = (TextView)findViewById(R.id.tv_mode2);
        tv_mode3 = (TextView)findViewById(R.id.tv_mode3);
        tv_return = (TextView)findViewById(R.id.tv_return);
        tv_set = (TextView)findViewById(R.id.tv_set);
        TouchListener touchListener = new TouchListener();
        tv_mode1.setOnTouchListener(touchListener);
        tv_mode2.setOnTouchListener(touchListener);
        tv_mode3.setOnTouchListener(touchListener);
        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Intent intent = new Intent(Hit_Activity.this, MainActivity.class);
                    startActivity(intent);
                    Hit_Activity.this.finish();;
                }
                return false;
            }
        });

    }
    private class TouchListener implements View.OnTouchListener
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Intent intent = new Intent(Hit_Activity.this, HitModeActivity.class);
                TextView tvView = (TextView)v;
                String strModeName = tvView.getText().toString();
                int iTime = PreferenceUtils.getPrefInt(Hit_Activity.this, strModeName, 0);
                intent.putExtra("ModeName", strModeName);
                intent.putExtra("Time", iTime);
                startActivity(intent);
            }
            return false;
        }
    }

}
