package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonToken;
import android.widget.TextView;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.MachineContract;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import java.util.Calendar;
import java.util.Date;

public class Detail_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    TextView machine_id;
    TextView place;
    TextView date;
    TextView user_name;
    TextView state;
    TextView problem;
    TextView solution;
    TextView solved_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        machine_id = (TextView) findViewById(R.id.detail_machine_id);
        place = (TextView) findViewById(R.id.detail_place);
        date = (TextView) findViewById(R.id.detail_date);
        user_name = (TextView) findViewById(R.id.detail_user_name);
        state = (TextView) findViewById(R.id.detail_state);
        problem = (TextView) findViewById(R.id.detail_problem);
        solution = (TextView) findViewById(R.id.detail_solution);
        solved_date = (TextView) findViewById(R.id.detail_solved_date);

        String stringState;
        switch (Integer.parseInt(getIntent().getExtras().getString(WebServerContract.DAILIES_STATE))){
            case MachineContract.MACHINE_STATE_使用中:
                stringState = MachineContract.MACHINE_STATE_STRING_使用中;
                break;
            case MachineContract.MACHINE_STATE_良好:
                stringState = MachineContract.MACHINE_STATE_STRING_良好;
                break;
            case MachineContract.MACHINE_STATE_問題排除:
                stringState = MachineContract.MACHINE_STATE_STRING_問題排除;
                break;
            case MachineContract.MACHINE_STATE_通知人員:
                stringState = MachineContract.MACHINE_STATE_STRING_通知人員;
                break;
            case MachineContract.MACHINE_STATE_其他:
                stringState = MachineContract.MACHINE_STATE_STRING_其他;
                break;
            default:
                stringState = MachineContract.MACHINE_STATE_STRING_未紀錄;
                break;
        }

        machine_id.setText(machine_id.getText() + getIntent().getExtras().getString(WebServerContract.MACHINE_NUMBER));
        place.setText(place.getText() + getIntent().getExtras().getString(WebServerContract.MACHINE_PLACE));
        date.setText(date.getText() + getIntent().getExtras().getString(WebServerContract.MACHINE_DATE));
        user_name.setText(user_name.getText() + getIntent().getExtras().getString(WebServerContract.DAILIES_USER_ID));
        state.setText(state.getText() + stringState);
        problem.setText(problem.getText() + getIntent().getExtras().getString(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL));
        solution.setText(solution.getText() + getIntent().getExtras().getString(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL));
        String[] stringDate = getIntent().getExtras().getString(WebServerContract.DAILY_PROBLEM_SOLVE_DATE).split(" ");
        solved_date.setText(solved_date.getText() + stringDate[0]);

    }
}
