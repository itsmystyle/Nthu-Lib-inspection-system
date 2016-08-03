package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.MachineContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import java.util.Calendar;

public class History_State_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    Button history_state_good;
    Button history_state_problem;
    Button history_state_solved;
    Button history_state_noinfo;
    Button history_state_other;
    Button history_state_using;

    Calendar calendar;
    int year_x, month_x, day_x;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_state);

        history_state_good = (Button) findViewById(R.id.history_state_good_button);
        history_state_problem = (Button) findViewById(R.id.history_state_problem_button);
        history_state_solved = (Button) findViewById(R.id.history_state_solved_button);
        history_state_noinfo = (Button) findViewById(R.id.history_state_noinfo_button);
        history_state_using = (Button) findViewById(R.id.history_state_using_button);
        history_state_other = (Button) findViewById(R.id.history_state_other_button);

        history_state_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MachineContract.MACHINE_STATE_良好;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
            }
        });

        history_state_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MachineContract.MACHINE_STATE_通知人員;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
            }
        });

        history_state_solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MachineContract.MACHINE_STATE_問題排除;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
            }
        });

        history_state_noinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MachineContract.MACHINE_STATE_未紀錄;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
            }
        });

        history_state_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MachineContract.MACHINE_STATE_其他;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
            }
        });

        history_state_using.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = MachineContract.MACHINE_STATE_使用中;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date;
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    String MON = String.valueOf(month_x);

                    if(month_x < 10) MON = "0"+MON;

                    String DA = String.valueOf(day_x);

                    if(day_x < 10) DA = "0"+DA;

                    date = year_x +"-"+ MON + "-"+DA;
                    Intent intent = new Intent(getApplicationContext(), Floor_Activity.class);
                    intent.putExtra(ViewContract.MODE, ViewContract.HISTORY_STATE);
                    intent.putExtra(WebServerContract.DAILIES_DATE, date);
                    intent.putExtra(WebServerContract.DAILIES_STATE, state);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            };
}
