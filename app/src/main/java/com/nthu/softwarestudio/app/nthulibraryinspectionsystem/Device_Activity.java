package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;

public class Device_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        int mode = getIntent().getExtras().getInt(ViewContract.MODE);
        int floor = getIntent().getExtras().getInt(ViewContract.FLOOR);

        populateTableButton(mode, floor);
    }

    private void populateTableButton(int mode, int floor) {
        int tableRow = 4;
        int tableCol = 3;

        if(mode == ViewContract.INSPECTION){
            for(int row = 0; row < tableRow; row++){
                TableRow tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        0.1f
                ));
                tableLayout.addView(tRow);

                for(int col = 0; col < tableCol; col++){
                    Button tableButton = new Button(this);
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT,
                            0.1f
                    ));
                    tableButton.setText(row + "," + col);
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);

                    tRow.addView(tableButton);
                }
            }

            TableRow tRow = new TableRow(this);
            tRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    0.1f
            ));
            tableLayout.addView(tRow);
            for(int col = 0; col < tableCol; col++){
                Button tableButton = new Button(this);
                tableButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        0.1f
                ));
                tableButton.setText(4 + "," + col);
                tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                if(col == 2) tableButton.setVisibility(View.INVISIBLE);

                tRow.addView(tableButton);
            }

        }else{
            for(int row = 0; row < tableRow; row++){
                TableRow tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        0.1f
                ));
                tableLayout.addView(tRow);

                for(int col = 0; col < tableCol; col++){
                    Button tableButton = new Button(this);
                    tableButton.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT,
                            0.1f
                    ));
                    tableButton.setText(tableRow + "," + tableCol);
                    tRow.addView(tableButton);
                }
            }

        }
    }

}
