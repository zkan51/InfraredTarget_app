package com.ExerciseMode;

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
import com.uidata.CommonData;

public class Exercise_Activity extends Activity {
    private TextView tv_exerciseReturn;
    int time = 0x1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_main);
        CommonData.dataProcess.sendCmd(0x00, CommonData.EXERCISECMD, time, 0x00, 0x00);
        tv_exerciseReturn = (TextView)findViewById(R.id.tv_exercise_return);
        tv_exerciseReturn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                   CommonData.dataProcess.sendCmd(0x00, CommonData.EXERCISECMD, 0x00, 0x00, 0x00);
                   startActivity(new Intent(Exercise_Activity.this, MainActivity.class));
                   Exercise_Activity.this.finish();
                }
                return false;
            }
        });
    }
}
