package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.MachineContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.MachineData;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Device_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    AccountHelper accountHelper;
    TableLayout tableLayout;
    TableLayout tableLayout_2;
    TableLayout tableLayout_3;
    TableLayout tableLayout_4;
    TableLayout tableLayout_5;
    TableLayout tableLayout_6;
    TableLayout tableLayout_rs1;
    TableLayout tableLayout_rs2;
    int mode;
    int floor;

    /**
     * Schema for Inspection
     * onCreate -> MachineInfoInspection(AsyncTask) -> populateTableButton
     *
     * Schema for History
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        accountHelper = new AccountHelper(getApplicationContext());

        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        tableLayout_2 = (TableLayout) findViewById(R.id.table_layout_f2);
        tableLayout_3 = (TableLayout) findViewById(R.id.table_layout_f3);
        tableLayout_4 = (TableLayout) findViewById(R.id.table_layout_f4);
        tableLayout_5 = (TableLayout) findViewById(R.id.table_layout_f5);
        tableLayout_6 = (TableLayout) findViewById(R.id.table_layout_f6);
        tableLayout_rs1 = (TableLayout) findViewById(R.id.table_layout_rsf1);
        tableLayout_rs2 = (TableLayout) findViewById(R.id.table_layout_rsf2);

        mode = getIntent().getExtras().getInt(ViewContract.MODE);
        floor = getIntent().getExtras().getInt(ViewContract.FLOOR);

        if(mode == ViewContract.INSPECTION){
            MachineInfoInspection machineInfoInspection = new MachineInfoInspection();
            int branch = 0;
            if(floor > 6){
                branch = WebServerContract.MACHINE_BRANCH_RS;
                floor = floor - 6;
            }
            else branch = WebServerContract.MACHINE_BRANCH_ZT;

            machineInfoInspection.execute(Integer.toString(branch), Integer.toString(floor));
        }else if(mode == ViewContract.HISTORY){
            String paraDate = getIntent().getExtras().getString(WebServerContract.DAILIES_DATE);
            String paraFloor;
            if(floor > 6){
                floor = floor - 6;
                paraFloor = 'C' + String.valueOf(floor);
            }else{
                paraFloor = 'F' + String.valueOf(floor);
            }
            MachineInfoHistory machineInfoHistory = new MachineInfoHistory();
            machineInfoHistory.execute(paraDate, paraFloor);

        }else if(mode == ViewContract.HISTORY_STATE){
            String paraDate = getIntent().getExtras().getString(WebServerContract.DAILIES_DATE);
            String paraFloor;
            if(floor > 6){
                floor = floor - 6;
                paraFloor = 'C' + String.valueOf(floor);
            }else{
                paraFloor = 'F' + String.valueOf(floor);
            }
            Integer tmp = getIntent().getExtras().getInt(WebServerContract.DAILIES_STATE);
            String paraState = tmp.toString();

            MachineInfoHistory machineInfoHistory = new MachineInfoHistory();
            machineInfoHistory.execute(paraDate, paraFloor , paraState);
        }else if(mode == ViewContract.STATE){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String CurrentDate = dateFormat.format(Calendar.getInstance().getTime());
            MachineInfoHistory machineInfoHistory = new MachineInfoHistory();
            machineInfoHistory.execute(CurrentDate);
        }
    }

    private void populateTableButton(final List<MachineData> machines_list, int mode) {
        final int tableRow = (int) Math.floor(machines_list.size()/3);
        int tableCol = 3;
        int remainderCol = machines_list.size()%3;
        TableRow tRow = null;

        if(mode == ViewContract.INSPECTION){
            for(int row = 0; row < tableRow; row++){
                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout.addView(tRow);

                for(int col = 0; col < tableCol; col++){
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth()/3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));
                    tableButton.setText(machines_list.get(row*3 + col).getMachine_id());
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);

                    final int finalRow = row;
                    final int finalCol = col;
                    tableButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), Form_Activity.class);
                            intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list.get(finalRow *3 + finalCol).getMachine_id());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });

                    tableButton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            PostTodayMachineInfoAsyncTask postTodayMachineInfoAsyncTask = new PostTodayMachineInfoAsyncTask();
                            postTodayMachineInfoAsyncTask.execute(machines_list.get(finalRow *3 + finalCol).getMachine_id(),
                                    String.valueOf(accountHelper.getForeignKey()));
                            return true;
                        }
                    });

                    tRow.addView(tableButton);
                }
            }

            tRow = new TableRow(this);
            tRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            tableLayout.addView(tRow);

            for(int col = 0; col < tableCol; col++){
                Button tableButton = new Button(this);
                int size = tRow.getWidth()/3;
                tableButton.setLayoutParams(new TableRow.LayoutParams(
                        size,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f
                ));

                if(col < remainderCol){
                    tableButton.setText(machines_list.get(tableRow*3 + col).getMachine_id());
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);

                    final int finalCol = col;
                    tableButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), Form_Activity.class);
                            intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list.get(tableRow*3 + finalCol).getMachine_id());
                            startActivity(intent);
                        }
                    });

                    tableButton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            PostTodayMachineInfoAsyncTask postTodayMachineInfoAsyncTask = new PostTodayMachineInfoAsyncTask();
                            postTodayMachineInfoAsyncTask.execute(machines_list.get(tableRow *3 + finalCol).getMachine_id(),
                                    String.valueOf(accountHelper.getForeignKey()));
                            return true;
                        }
                    });

                }else{
                    tableButton.setText("");
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                    tableButton.setVisibility(View.INVISIBLE);
                }

                tRow.addView(tableButton);
            }

        }else if(mode == ViewContract.HISTORY || mode == ViewContract.HISTORY_STATE){
            for(int row = 0; row < tableRow; row++){
                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout.addView(tRow);

                for(int col = 0; col < tableCol; col++){
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth()/3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));
                    String stringState;
                    switch (Integer.parseInt(machines_list.get(row*3 + col).getState())){
                        case MachineContract.MACHINE_STATE_使用中:
                            stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                            break;
                        case MachineContract.MACHINE_STATE_良好:
                            stringState = MachineContract.MACHINE_STATE_STRING_良好;
                            break;
                        case MachineContract.MACHINE_STATE_通知人員:
                            stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                            break;
                        case MachineContract.MACHINE_STATE_問題排除:
                            stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                            break;
                        case MachineContract.MACHINE_STATE_其他:
                            stringState = MachineContract.MACHINE_STATE_STRING_其他;
                            break;
                        default:
                            stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                            break;

                    }
                    tableButton.setText(machines_list.get(row*3 + col).getMachine_id() + "\n" + stringState);
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    switch (Integer.parseInt(machines_list.get(row*3 + col).getState())){
                        case MachineContract.MACHINE_STATE_使用中:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                            break;
                        case MachineContract.MACHINE_STATE_良好:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                            break;
                        case MachineContract.MACHINE_STATE_其他:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                            break;
                        default:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                            break;

                    }

                    final int finalRow = row;
                    final int finalCol = col;
                    tableButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                            intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list.get(finalRow *3 + finalCol).getMachine_id());
                            intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list.get(finalRow *3 + finalCol).getPlace());
                            intent.putExtra(WebServerContract.MACHINE_DATE, machines_list.get(finalRow *3 + finalCol).getDate());
                            intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list.get(finalRow *3 + finalCol).getUser_name());
                            intent.putExtra(WebServerContract.DAILIES_STATE, machines_list.get(finalRow *3 + finalCol).getState());
                            intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list.get(finalRow *3 + finalCol).getProblem());
                            intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list.get(finalRow *3 + finalCol).getSolution());
                            intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list.get(finalRow *3 + finalCol).getSolve_date());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });

                    tRow.addView(tableButton);
                }
            }

            tRow = new TableRow(this);
            tRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            tableLayout.addView(tRow);

            for(int col = 0; col < tableCol; col++){
                Button tableButton = new Button(this);
                int size = tRow.getWidth()/3;
                tableButton.setLayoutParams(new TableRow.LayoutParams(
                        size,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f
                ));

                if(col < remainderCol){
                    String stringState;
                    switch (Integer.parseInt(machines_list.get(tableRow*3 + col).getState())){
                        case MachineContract.MACHINE_STATE_使用中:
                            stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                            break;
                        case MachineContract.MACHINE_STATE_良好:
                            stringState = MachineContract.MACHINE_STATE_STRING_良好;
                            break;
                        case MachineContract.MACHINE_STATE_通知人員:
                            stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                            break;
                        case MachineContract.MACHINE_STATE_問題排除:
                            stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                            break;
                        case MachineContract.MACHINE_STATE_其他:
                            stringState = MachineContract.MACHINE_STATE_STRING_其他;
                            break;
                        default:
                            stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                            break;

                    }
                    tableButton.setText(machines_list.get(tableRow*3 + col).getMachine_id() + "\n" + stringState);
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    switch (Integer.parseInt(machines_list.get(tableRow*3 + col).getState())){
                        case MachineContract.MACHINE_STATE_使用中:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                            break;
                        case MachineContract.MACHINE_STATE_良好:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                            break;
                        case MachineContract.MACHINE_STATE_其他:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                            break;
                        default:
                            tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                            break;

                    }

                    final int finalCol = col;
                    tableButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                            intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list.get(tableRow *3 + finalCol).getMachine_id());
                            intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list.get(tableRow *3 + finalCol).getPlace());
                            intent.putExtra(WebServerContract.MACHINE_DATE, machines_list.get(tableRow *3 + finalCol).getDate());
                            intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list.get(tableRow *3 + finalCol).getUser_name());
                            intent.putExtra(WebServerContract.DAILIES_STATE, machines_list.get(tableRow *3 + finalCol).getState());
                            intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list.get(tableRow *3 + finalCol).getProblem());
                            intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list.get(tableRow *3 + finalCol).getSolution());
                            intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list.get(tableRow *3 + finalCol).getSolve_date());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });

                }else{
                    tableButton.setText("");
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                    tableButton.setVisibility(View.INVISIBLE);
                }

                tRow.addView(tableButton);
            }

        }else {
            final List<MachineData> machines_list_f1 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_f2 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_f3 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_f4 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_f5 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_f6 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_rsf1 = new ArrayList<MachineData>();
            final List<MachineData> machines_list_rsf2 = new ArrayList<MachineData>();

            for (int i = 0; i < machines_list.size(); i++) {
                if (machines_list.get(i).getBranch().equals("總圖")) {
                    switch (machines_list.get(i).getFloor()) {
                        case "1":
                            machines_list_f1.add(machines_list.get(i));
                            break;
                        case "2":
                            machines_list_f2.add(machines_list.get(i));
                            break;
                        case "3":
                            machines_list_f3.add(machines_list.get(i));
                            break;
                        case "4":
                            machines_list_f4.add(machines_list.get(i));
                            break;
                        case "5":
                            machines_list_f5.add(machines_list.get(i));
                            break;
                        case "6":
                            machines_list_f6.add(machines_list.get(i));
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Can't parse object floor", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    switch (machines_list.get(i).getFloor()) {
                        case "1":
                            machines_list_rsf1.add(machines_list.get(i));
                            break;
                        case "2":
                            machines_list_rsf2.add(machines_list.get(i));
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Can't parse object floor", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            final int tableRow_f1 = (int) Math.floor(machines_list_f1.size() / 3);
            final int tableRow_f2 = (int) Math.floor(machines_list_f2.size() / 3);
            final int tableRow_f3 = (int) Math.floor(machines_list_f3.size() / 3);
            final int tableRow_f4 = (int) Math.floor(machines_list_f4.size() / 3);
            final int tableRow_f5 = (int) Math.floor(machines_list_f5.size() / 3);
            final int tableRow_f6 = (int) Math.floor(machines_list_f6.size() / 3);
            final int tableRow_rsf1 = (int) Math.floor(machines_list_rsf1.size() / 3);
            final int tableRow_rsf2 = (int) Math.floor(machines_list_rsf2.size() / 3);
            int remainderCol_f1 = machines_list_f1.size() % 3;
            int remainderCol_f2 = machines_list_f2.size() % 3;
            int remainderCol_f3 = machines_list_f3.size() % 3;
            int remainderCol_f4 = machines_list_f4.size() % 3;
            int remainderCol_f5 = machines_list_f5.size() % 3;
            int remainderCol_f6 = machines_list_f6.size() % 3;
            int remainderCol_rsf1 = machines_list_rsf1.size() % 3;
            int remainderCol_rsf2 = machines_list_rsf2.size() % 3;

            //floor 1
            if (machines_list_f1.size() != 0) {
                for (int row = 0; row < tableRow_f1; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_f1.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f1.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f1.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f1.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f1.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f1.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f1.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f1.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f1.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f1.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f1.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_f1) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_f1.get(tableRow_f1 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f1.get(tableRow_f1 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f1.get(tableRow_f1 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f1.get(tableRow_f1 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_f1 > 0) tRow.addView(tableButton);
                }
            }

            //floor 2
            if (machines_list_f2.size() != 0) {
                for (int row = 0; row < tableRow_f2; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_2.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_f2.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f2.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f2.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f2.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f2.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f2.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f2.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f2.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f2.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f2.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f2.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_2.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_f2) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_f2.get(tableRow_f2 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f2.get(tableRow_f2 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f2.get(tableRow_f2 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f2.get(tableRow_f2 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_f2 > 0) tRow.addView(tableButton);
                }
            }

            //floor 3
            if (machines_list_f3.size() != 0) {
                for (int row = 0; row < tableRow_f3; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_3.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_f3.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f3.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f3.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f3.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f3.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f3.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f3.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f3.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f3.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f3.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f3.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_3.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_f3) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_f3.get(tableRow_f3 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f3.get(tableRow_f3 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f3.get(tableRow_f3 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f3.get(tableRow_f3 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_f3 > 0) tRow.addView(tableButton);
                }
            }

            //floor 4
            if (machines_list_f4.size() != 0) {
                for (int row = 0; row < tableRow_f4; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_4.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_f4.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f4.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f4.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f4.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f4.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f4.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f4.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f4.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f4.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f4.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f4.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_4.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_f4) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_f4.get(tableRow_f4 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f4.get(tableRow_f4 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f4.get(tableRow_f4 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f4.get(tableRow_f4 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_f4 > 0) tRow.addView(tableButton);
                }
            }

            //floor 5
            if (machines_list_f5.size() != 0) {
                for (int row = 0; row < tableRow_f5; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_5.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_f5.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f5.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f5.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f5.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f5.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f5.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f5.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f5.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f5.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f5.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f5.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_5.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_f5) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_f5.get(tableRow_f5 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f5.get(tableRow_f5 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f5.get(tableRow_f5 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f5.get(tableRow_f5 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_f5 > 0) tRow.addView(tableButton);
                }
            }

            //floor 6
            if (machines_list_f6.size() != 0) {
                for (int row = 0; row < tableRow_f6; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_6.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_f6.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f6.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f6.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f6.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f6.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f6.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f6.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f6.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f6.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f6.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f6.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_6.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_f6) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_f6.get(tableRow_f6 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_f6.get(tableRow_f6 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_f6.get(tableRow_f6 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_f6.get(tableRow_f6 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_f6 > 0) tRow.addView(tableButton);
                }
            }

            //floor rsf1
            if (machines_list_rsf1.size() != 0) {
                for (int row = 0; row < tableRow_rsf1; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_rs1.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_rsf1.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_rsf1.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_rsf1.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_rsf1.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_rsf1.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_rsf1.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_rsf1.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_rsf1.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_rsf1.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_rsf1.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_rsf1.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_rs1.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_rsf1) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_rsf1.get(tableRow_rsf1 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_rsf1.get(tableRow_rsf1 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_rsf1.get(tableRow_rsf1 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_rsf1.get(tableRow_rsf1 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_rsf1 > 0) tRow.addView(tableButton);
                }
            }

            //floor rsf2
            if (machines_list_rsf2.size() != 0) {
                for (int row = 0; row < tableRow_rsf2; row++) {
                    tRow = new TableRow(this);
                    tRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT
                    ));
                    tableLayout_rs2.addView(tRow);

                    for (int col = 0; col < tableCol; col++) {
                        Button tableButton = new Button(this);
                        int size = tRow.getWidth() / 3;
                        tableButton.setLayoutParams(new TableRow.LayoutParams(
                                size,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));
                        String stringState;
                        switch (Integer.parseInt(machines_list_rsf2.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_rsf2.get(row * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_rsf2.get(row * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalRow = row;
                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_rsf2.get(finalRow * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_rsf2.get(finalRow * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_rsf2.get(finalRow * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_rsf2.get(finalRow * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_rsf2.get(finalRow * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_rsf2.get(finalRow * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_rsf2.get(finalRow * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_rsf2.get(finalRow * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                        tRow.addView(tableButton);
                    }
                }

                tRow = new TableRow(this);
                tRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT
                ));
                tableLayout_rs2.addView(tRow);

                for (int col = 0; col < tableCol; col++) {
                    Button tableButton = new Button(this);
                    int size = tRow.getWidth() / 3;
                    tableButton.setLayoutParams(new TableRow.LayoutParams(
                            size,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    ));

                    if (col < remainderCol_rsf2) {
                        String stringState;
                        switch (Integer.parseInt(machines_list_rsf2.get(tableRow_rsf2 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                                break;
                            default:
                                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                                break;

                        }
                        tableButton.setText(machines_list_rsf2.get(tableRow_rsf2 * 3 + col).getMachine_id() + "\n" + stringState);
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        switch (Integer.parseInt(machines_list_rsf2.get(tableRow_rsf2 * 3 + col).getState())) {
                            case MachineContract.MACHINE_STATE_使用中:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_using);
                                break;
                            case MachineContract.MACHINE_STATE_良好:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                                break;
                            case MachineContract.MACHINE_STATE_通知人員:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_problem);
                                break;
                            case MachineContract.MACHINE_STATE_問題排除:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_solved);
                                break;
                            case MachineContract.MACHINE_STATE_其他:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_others);
                                break;
                            default:
                                tableButton.setBackgroundResource(R.drawable.rounded_button_history_noinfo);
                                break;

                        }

                        final int finalCol = col;
                        tableButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Detail_Activity.class);
                                intent.putExtra(MachineContract.MACHINE_NUMBER, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getMachine_id());
                                intent.putExtra(WebServerContract.MACHINE_PLACE, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getPlace());
                                intent.putExtra(WebServerContract.MACHINE_DATE, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getDate());
                                intent.putExtra(WebServerContract.DAILIES_USER_ID, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getUser_name());
                                intent.putExtra(WebServerContract.DAILIES_STATE, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getState());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getProblem());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getSolution());
                                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE, machines_list_rsf2.get(tableRow_rsf2 * 3 + finalCol).getSolve_date());
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    } else {
                        tableButton.setText("");
                        tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                        tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                        tableButton.setVisibility(View.INVISIBLE);
                    }

                    if(remainderCol_rsf2 > 0) tRow.addView(tableButton);
                }
            }
        }
    }

    /**
     * Inspection
     * AsyncTask to get every machine info in each floor
     * @pararmeter branch, floor
     */
    class MachineInfoInspection extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = getClass().getSimpleName();
        private final String MACHINE_INFO_URL = WebServerContract.BASE_URL + WebServerContract.MACHINE_INFO_URL;

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL(MACHINE_INFO_URL);
                String urlParameters = WebServerContract.MACHINE_BRANCH + "=" + params[0] + "&" +
                        WebServerContract.MACHINE_FLOOR + "=" + params[1];

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo == null || !networkInfo.isConnected()){
                    networkService = false;
                    return null;
                }

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(urlParameters);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if(inputStream == null){
                    Log.e(LOG_TAG, "InputStream Error");
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0){
                    Log.e(LOG_TAG, "String Buffer Error");
                    return null;
                }

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, e.getMessage() + "Error MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(LOG_TAG, e.getMessage() + "Error ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing buffer.", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(s == null || s.length() == 0 || !networkService){
                    Log.e(LOG_TAG, "Unable to get json");
                    if(!networkService){
                        Toast.makeText(getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unable to connect to server. Please try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                JSONObject reponse = new JSONObject(s);
                if(reponse.getString(WebServerContract.MACHINE_INFO_SERVER)
                        .equals(WebServerContract.MACHINE_SERVER_ERROR)){
                    Log.e(LOG_TAG, "Web Server Database Failed! Please contact us to fix this.");
                    Toast.makeText(getApplicationContext(),"Web Server Database Failed! Please contact us to fix this."
                            , Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    String secure = (reponse.getJSONObject(WebServerContract.MACHINE_INFO_SERVER))
                            .getString(WebServerContract.MACHINE_NUMBER);

                    if(secure.equals(WebServerContract.MACHINE_NONE)){
                        Toast.makeText(getApplicationContext(),"Don't have machines in this floor."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        JSONArray machines_info = (reponse.getJSONObject(WebServerContract.MACHINE_INFO_SERVER)).getJSONArray(WebServerContract.MACHINE_NUMBER);

                        List<MachineData> machines_list = new ArrayList<MachineData>();
                        for(int i=0; i<machines_info.length(); i++)
                            machines_list.add(new MachineData(machines_info.getJSONObject(i).getString("machine_id"), null, null, null, null, null, null, null, null, null));

                        populateTableButton(machines_list, mode);
                    }
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    /**
     * History display
     * AsyncTask to get every history of the particular machine in each floor
     * @parameter date, floor
     */
    class MachineInfoHistory extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = getClass().getSimpleName();
        private final String MACHINE_INFO_URL = WebServerContract.BASE_URL + WebServerContract.LIST_ALL_PROBLEN_URL;

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            String outputstring;
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String FORECAST_BASE_URL =
                        WebServerContract.BASE_URL+ WebServerContract.LIST_ALL_PROBLEN_URL+"?";
                final String DATE = "date";
                final String FLOOR = "floor";
                final String STATE = "state";
                Uri builtUri;
                Integer tmp = params.length;

                if(params.length == 2){
                    builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                            .appendQueryParameter(DATE, params[0])
                            .appendQueryParameter(FLOOR, params[1])
                            .build();

                }else if(params.length == 1){
                    builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                            .appendQueryParameter(DATE, params[0])
                            .build();
                }else {
                    builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                            .appendQueryParameter(DATE, params[0])
                            .appendQueryParameter(FLOOR, params[1])
                            .appendQueryParameter(STATE, params[2])
                            .build();
                }

                URL url = new URL(builtUri.toString());

                Log.v("date look look",builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                outputstring = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return outputstring;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.v("date look look",s);

            super.onPostExecute(s);
            try{
                if(s.equals("No data\n")){
                    Toast.makeText(Device_Activity.this, "No data", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray reader = new JSONArray(s);
                int data_length = reader.length();
                List<MachineData> listMachineData = new ArrayList<MachineData>();
                MachineData mdata;
                for(int i=0;i<data_length;i++){
                    mdata = new MachineData(
                            reader.getJSONObject(i).getString("machine_id"),
                            reader.getJSONObject(i).getString("place"),
                            reader.getJSONObject(i).getString("date"),
                            reader.getJSONObject(i).getString("username"),
                            reader.getJSONObject(i).getString("state"),
                            reader.getJSONObject(i).getString("problem_detail"),
                            reader.getJSONObject(i).getString("solve_detail"),
                            reader.getJSONObject(i).getString("solve_date"),
                            reader.getJSONObject(i).getString("branch"),
                            reader.getJSONObject(i).getString("floor")
                    );
                    listMachineData.add(mdata);
                }

                if(mode == ViewContract.HISTORY)
                    populateTableButton(listMachineData, ViewContract.HISTORY);
                else if(mode == ViewContract.HISTORY_STATE)
                    populateTableButton(listMachineData, ViewContract.HISTORY_STATE);
                else if(mode == ViewContract.STATE)
                    populateTableButton(listMachineData, ViewContract.STATE);

            }catch(JSONException e){e.printStackTrace();}
        }
    }

    class PostTodayMachineInfoAsyncTask extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = getClass().getSimpleName();
        private final String POST_MACHINE_INFO_BASE_URL = WebServerContract.BASE_URL + WebServerContract.MACHINE_INFO_POST_FORM_URL;

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            try{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String CurrentDate = dateFormat.format(Calendar.getInstance().getTime());

                URL url = new URL(POST_MACHINE_INFO_BASE_URL);
                String urlParameters = WebServerContract.DAILIES_MACHINE_ID + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                        WebServerContract.DAILIES_DATE + "=" + CurrentDate + "&" +
                        WebServerContract.DAILIES_USER_ID + "=" + URLEncoder.encode(params[1], "utf-8") + "&" +
                        WebServerContract.DAILIES_STATE + "=" + MachineContract.MACHINE_STATE_良好;

                Log.e(LOG_TAG, url + " " + urlParameters);

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo == null || !networkInfo.isConnected()){
                    networkService = false;
                    return null;
                }

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(urlParameters);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if(inputStream == null){
                    Log.e(LOG_TAG, "InputStream Error");
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0){
                    Log.e(LOG_TAG, "String Buffer Error");
                    return null;
                }

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, e.getMessage() + "Error MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(LOG_TAG, e.getMessage() + "Error ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing buffer.", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}
