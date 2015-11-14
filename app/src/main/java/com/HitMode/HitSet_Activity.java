package com.HitMode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.infraredgun.R;

public class HitSet_Activity extends Activity {

    private Spinner spMode1;
    private Spinner spMode2;
    private Spinner spMode3;
    private TextView tvReturn;
    private TextView tvConfirm;
    ArrayAdapter arrayAdapter;
    String strTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_main);
        spMode1 = (Spinner)findViewById(R.id.sp_mode1);
        spMode2 = (Spinner)findViewById(R.id.sp_mode2);
        spMode3 = (Spinner)findViewById(R.id.sp_mode3);
        tvReturn = (TextView)findViewById(R.id.tv_setreturn);
        tvConfirm = (TextView)findViewById(R.id.tv_Confirm);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.ModeTime, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMode1.setAdapter(arrayAdapter);
        spMode2.setAdapter(arrayAdapter);
        spMode3.setAdapter(arrayAdapter);

    }
    public class ItemListner implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            strTime=(String) parent.getItemAtPosition(position);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }

}
