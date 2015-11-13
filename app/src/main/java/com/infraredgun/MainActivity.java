package com.infraredgun;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.uidata.CommonData;
import com.uidata.WifiAdmin;


public class MainActivity extends Activity {

    private TextView tv_exercise;
    private TextView tv_hit;
    private TextView tv_compete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



   }

}
