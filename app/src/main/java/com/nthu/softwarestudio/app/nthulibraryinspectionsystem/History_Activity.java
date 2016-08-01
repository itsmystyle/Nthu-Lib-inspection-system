package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import java.util.Calendar;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;

public class History_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    Button all_history;
    Button state_history;
    Button search_history;

    Calendar calendar;
    int year_x, month_x, day_x;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        all_history = (Button) findViewById(R.id.history_all_button);
        state_history = (Button) findViewById(R.id.history_state_button);
        search_history = (Button) findViewById(R.id.history_search_button);

        all_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        state_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), History_State_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(ViewContract.MODE, ViewContract.SEARCH);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

            if(month_x < 10) DA = "0"+DA;

            date = year_x +"-"+ MON + "-"+DA;
            Intent intent = new Intent(getApplicationContext(), Floor_Activity.class);
            intent.putExtra(ViewContract.MODE, ViewContract.HISTORY);
            intent.putExtra(WebServerContract.DAILIES_DATE, date);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    };
}
