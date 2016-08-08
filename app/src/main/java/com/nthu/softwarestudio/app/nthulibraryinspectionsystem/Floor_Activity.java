package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

public class Floor_Activity extends AppCompatActivity {

    Button floor_1;
    Button floor_2;
    Button floor_3;
    Button floor_4;
    Button floor_5;
    Button floor_6;
    Button floor_rs1;
    Button floor_rs2;
    Button floor_3_language;
    Button floor_2_sharing;

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
        floor_3_language = (Button) findViewById(R.id.button_f3_language);
        floor_2_sharing = (Button) findViewById(R.id.button_f2_sharing);

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

            floor_3_language.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_3_語言學習區, ViewContract.INSPECTION);
                }
            });

            floor_2_sharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(ViewContract.FLOOR_2_學習共享區, ViewContract.INSPECTION);
                }
            });

        }else if(viewMode == ViewContract.HISTORY){
            floor_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_1, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_2, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_3, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_4, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_5, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_6, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_rs1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_RS1, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_rs2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_RS2, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_3_language.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_3_語言學習區, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

            floor_2_sharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyButtonClicked(ViewContract.FLOOR_2_學習共享區, ViewContract.HISTORY,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE));
                }
            });

        }else if(viewMode == ViewContract.HISTORY_STATE){
            floor_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_1, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_2, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_3, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_4, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_5, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_6, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_rs1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_RS1, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_rs2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_RS2, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_3_language.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_3_語言學習區, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

            floor_2_sharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyStateButtonClicked(ViewContract.FLOOR_2_學習共享區, ViewContract.HISTORY_STATE,
                            getIntent().getExtras().getString(WebServerContract.DAILIES_DATE),
                            getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE));
                }
            });

        }else{

        }
    }

    private void buttonClicked(int floor, int mode){
        Intent intent = new Intent(getApplicationContext(), Device_Activity.class);
        intent.putExtra(ViewContract.FLOOR, floor);
        intent.putExtra(ViewContract.MODE, mode);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void historyButtonClicked(int floor, int mode, String date){
        Intent intent = new Intent(getApplicationContext(), Device_Activity.class);
        intent.putExtra(ViewContract.FLOOR, floor);
        intent.putExtra(ViewContract.MODE, mode);
        intent.putExtra(WebServerContract.DAILIES_DATE, date);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void historyStateButtonClicked(int floor, int mode, String date, int state){
        Intent intent = new Intent(getApplicationContext(), Device_Activity.class);
        intent.putExtra(ViewContract.FLOOR, floor);
        intent.putExtra(ViewContract.MODE, mode);
        intent.putExtra(WebServerContract.DAILIES_STATE, state);
        intent.putExtra(WebServerContract.DAILIES_DATE, date);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
