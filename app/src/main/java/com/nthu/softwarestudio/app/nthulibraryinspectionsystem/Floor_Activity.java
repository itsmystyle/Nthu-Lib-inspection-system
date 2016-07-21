package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.DeviceContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;

public class Floor_Activity extends AppCompatActivity {

    Button floor_1;
    Button floor_2;
    Button floor_3;
    Button floor_4;
    Button floor_5;
    Button floor_6;
    Button floor_rs1;
    Button floor_rs2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);

        floor_1 = (Button) findViewById(R.id.button_f1);
        floor_2 = (Button) findViewById(R.id.button_f2);
        floor_3 = (Button) findViewById(R.id.button_f3);
        floor_4 = (Button) findViewById(R.id.button_f4);
        floor_5 = (Button) findViewById(R.id.button_f5);
        floor_6 = (Button) findViewById(R.id.button_f6);
        floor_rs1 = (Button) findViewById(R.id.button_rsf1);
        floor_rs2 = (Button) findViewById(R.id.button_rsf2);

        int viewMode = getIntent().getExtras().getInt(ViewContract.MODE);

        if(viewMode == ViewContract.INSPECTION){
            floor_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_1, ViewContract.INSPECTION);
                }
            });

            floor_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_2, ViewContract.INSPECTION);
                }
            });

            floor_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_3, ViewContract.INSPECTION);
                }
            });

            floor_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_4, ViewContract.INSPECTION);
                }
            });

            floor_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_5, ViewContract.INSPECTION);
                }
            });

            floor_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_6, ViewContract.INSPECTION);
                }
            });

            floor_rs1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_RS1, ViewContract.INSPECTION);
                }
            });

            floor_rs2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_RS2, ViewContract.INSPECTION);
                }
            });

        }else{
            floor_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_1, ViewContract.HISTORY);
                }
            });

            floor_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_2, ViewContract.HISTORY);
                }
            });

            floor_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_3, ViewContract.HISTORY);
                }
            });

            floor_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_4, ViewContract.HISTORY);
                }
            });

            floor_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_5, ViewContract.HISTORY);
                }
            });

            floor_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_6, ViewContract.HISTORY);
                }
            });

            floor_rs1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_RS1, ViewContract.HISTORY);
                }
            });

            floor_rs2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_RS2, ViewContract.HISTORY);
                }
            });
        }



    }

    private void buttonClicked(int floor, int mode){
        Intent intent = new Intent(getApplicationContext(), Device_Activity.class);
        intent.putExtra(ViewContract.FLOOR, floor);
        intent.putExtra(ViewContract.MODE, mode);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
