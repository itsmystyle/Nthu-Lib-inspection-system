package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;

public class Device_Activity extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        int mode = getIntent().getExtras().getInt(ViewContract.MODE);
        int floor = getIntent().getExtras().getInt(ViewContract.FLOOR);

        if(mode == ViewContract.INSPECTION){
            populateInspectionTableButton(floor);
        }else{
            populateHistoryTableButton(floor);
        }

    }

    private void populateHistoryTableButton(int floor) {

    }

    private void populateInspectionTableButton(int floor) {

    }
}
