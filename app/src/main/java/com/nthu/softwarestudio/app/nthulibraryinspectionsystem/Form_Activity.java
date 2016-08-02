package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.MachineContract;
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

public class Form_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    Dialog problem_dialog;
    Dialog problem_solve_dialog;

    TextView machine_number_textView;
    TextView place_textView;
    TextView dialog_problem_machine_number;
    TextView dialog_problem_machine_place;
    TextView dialog_solve_machine_number;
    TextView dialog_solve_machine_place;
    Button past7thday_button;
    Button past6thday_button;
    Button past5thday_button;
    Button past4thday_button;
    Button past3rdday_button;
    Button past2ndday_button;
    Button past1stday_button;
    Button submit_button;
    Button daily_dialog_submit_button;
    Button daily_dialog_cancel_button;
    EditText daily_problem_input;
    EditText daily_problem_solve_input;
    Spinner spinner;
    Spinner problem_spinner;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> problemAdapter;

    String CurrentDate;
    String Past1stDate;
    String Past2ndDate;
    String Past3rdDate;
    String Past4thDate;
    String Past5thDate;
    String Past6thDate;
    String Past7thDate;
    String MachinePlace;

    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        RetreatMachineInfoAsyncTask retreatMachineInfoAsyncTask = new RetreatMachineInfoAsyncTask();
        retreatMachineInfoAsyncTask.execute(getIntent().getExtras().getString(MachineContract.MACHINE_NUMBER));

        machine_number_textView = (TextView) findViewById(R.id.form_machine_id);
        place_textView = (TextView) findViewById(R.id.form_place);
        past1stday_button = (Button) findViewById(R.id.form_past1stday_button);
        past2ndday_button = (Button) findViewById(R.id.form_past2ndday_button);
        past3rdday_button = (Button) findViewById(R.id.form_past3rdhday_button);
        past4thday_button = (Button) findViewById(R.id.form_past4thday_button);
        past5thday_button = (Button) findViewById(R.id.form_past5thday_button);
        past6thday_button = (Button) findViewById(R.id.form_past6thday_button);
        past7thday_button = (Button) findViewById(R.id.form_past7thday_button);
        submit_button = (Button) findViewById(R.id.form_submit_button);

        spinner = (Spinner) findViewById(R.id.form_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.condition, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()){
                    case MachineContract.MACHINE_STATE_STRING_使用中: state = MachineContract.MACHINE_STATE_使用中;
                        break;
                    case MachineContract.MACHINE_STATE_STRING_良好: state = MachineContract.MACHINE_STATE_良好;
                        break;
                    case MachineContract.MACHINE_STATE_STRING_問題排除: state = MachineContract.MACHINE_STATE_問題排除;
                        break;
                    case MachineContract.MACHINE_STATE_STRING_通知人員: state = MachineContract.MACHINE_STATE_通知人員;
                        break;
                    case  MachineContract.MACHINE_STATE_STRING_其他: state = MachineContract.MACHINE_STATE_其他;
                        break;
                    default: state = MachineContract.MACHINE_STATE_未紀錄;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AccountHelper accountHelper = new AccountHelper(getApplicationContext());
                switch(state){
                    case MachineContract.MACHINE_STATE_通知人員:
                        problem_dialog = new Dialog(v.getContext(), R.style.AppTheme_Dialog);
                        problem_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        problem_dialog.setContentView(R.layout.daily_problem_dialog);
                        problem_dialog.setCanceledOnTouchOutside(false);

                        Window window_problem = problem_dialog.getWindow();
                        window_problem.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.WRAP_CONTENT);

                        daily_problem_input = (EditText) problem_dialog.findViewById(R.id.daily_problem_dialog_problem_input);
                        daily_dialog_submit_button = (Button) problem_dialog.findViewById(R.id.daily_problem_dialog_submit_button);
                        daily_dialog_cancel_button = (Button) problem_dialog.findViewById(R.id.daily_problem_dialog_cancel_button);
                        dialog_problem_machine_number = (TextView) problem_dialog.findViewById(R.id.daily_problem_dialog_machine_number);
                        dialog_problem_machine_place = (TextView) problem_dialog.findViewById(R.id.daily_problem_dialog_place);

                        daily_dialog_submit_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PostTodayMachineInfoAsyncTask postTodayMachineInfoAsyncTask = new PostTodayMachineInfoAsyncTask();
                                postTodayMachineInfoAsyncTask.execute(getIntent().getExtras().getString(WebServerContract.MACHINE_NUMBER),
                                        String.valueOf(accountHelper.getForeignKey()),
                                        daily_problem_input.getText().toString(),
                                        accountHelper.getUserName());
                            }
                        });

                        daily_dialog_cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                problem_dialog.onBackPressed();
                            }
                        });

                        dialog_problem_machine_number.setText(dialog_problem_machine_number.getText() + getIntent().getExtras().getString(WebServerContract.MACHINE_NUMBER));
                        dialog_problem_machine_place.setText(dialog_problem_machine_place.getText() + MachinePlace);

                        problem_dialog.show();

                        break;

                    case MachineContract.MACHINE_STATE_問題排除:
                        problem_solve_dialog = new Dialog(v.getContext(), R.style.AppTheme_Dialog);
                        problem_solve_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        problem_solve_dialog.setContentView(R.layout.daily_problem_solve_dialog);
                        problem_solve_dialog.setCanceledOnTouchOutside(false);

                        Window window_solve = problem_solve_dialog.getWindow();
                        window_solve.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.WRAP_CONTENT);

                        daily_problem_input = (EditText) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_problem_input);
                        daily_problem_solve_input = (EditText) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_problem_solve_input);
                        daily_dialog_submit_button = (Button) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_submit_button);
                        daily_dialog_cancel_button = (Button) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_cancel_button);
                        dialog_solve_machine_number = (TextView) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_machine_number);
                        dialog_solve_machine_place = (TextView) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_place);

                        problem_spinner = (Spinner) problem_solve_dialog.findViewById(R.id.daily_problem_solve_dialog_spinner);
                        problemAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.problem_list, android.R.layout.simple_spinner_item);
                        problemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        problem_spinner.setAdapter(problemAdapter);

                        problem_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                switch(parent.getItemAtPosition(position).toString()){
                                    case MachineContract.MACHINE_PROBLEM_其他:
                                        daily_problem_input.setText("");
                                        break;
                                    case  MachineContract.MACHINE_PROBLEM_VGA線被拔掉:
                                        daily_problem_input.setText(MachineContract.MACHINE_PROBLEM_VGA線被拔掉);
                                        break;
                                    default:
                                        daily_problem_input.setText("");
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        daily_dialog_submit_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PostTodayMachineInfoAsyncTask postTodayMachineInfoAsyncTask = new PostTodayMachineInfoAsyncTask();
                                postTodayMachineInfoAsyncTask.execute(getIntent().getExtras().getString(WebServerContract.MACHINE_NUMBER),
                                        String.valueOf(accountHelper.getForeignKey()),
                                        daily_problem_input.getText().toString(),
                                        daily_problem_solve_input.getText().toString(),
                                        accountHelper.getUserName());
                            }
                        });

                        daily_dialog_cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                problem_solve_dialog.onBackPressed();
                            }
                        });

                        dialog_solve_machine_number.setText(dialog_solve_machine_number.getText() + getIntent().getExtras().getString(WebServerContract.MACHINE_NUMBER));
                        dialog_solve_machine_place.setText(dialog_solve_machine_place.getText() + MachinePlace);

                        problem_solve_dialog.show();

                        break;

                    default:
                        PostTodayMachineInfoAsyncTask postTodayMachineInfoAsyncTask = new PostTodayMachineInfoAsyncTask();
                        postTodayMachineInfoAsyncTask.execute(getIntent().getExtras().getString(MachineContract.MACHINE_NUMBER),
                                String.valueOf(accountHelper.getForeignKey()));
                }
            }
        });

        machine_number_textView.setText(machine_number_textView.getText() + getIntent().getExtras().getString(MachineContract.MACHINE_NUMBER));

    }

    class RetreatMachineInfoAsyncTask extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = getClass().getSimpleName();
        private final String MACHINE_INFO_URL = WebServerContract.BASE_URL + WebServerContract.MACHINE_INFO_FORM_URL;

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            try{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                CurrentDate = dateFormat.format(Calendar.getInstance().getTime());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-1);
                Past1stDate = dateFormat.format(calendar.getTime());
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-2);
                Past2ndDate = dateFormat.format(calendar.getTime());
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-3);
                Past3rdDate = dateFormat.format(calendar.getTime());
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-4);
                Past4thDate = dateFormat.format(calendar.getTime());
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-5);
                Past5thDate = dateFormat.format(calendar.getTime());
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-6);
                Past6thDate = dateFormat.format(calendar.getTime());
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-7);
                Past7thDate = dateFormat.format(calendar.getTime());

                URL url = new URL(MACHINE_INFO_URL);
                String urlParameters = WebServerContract.MACHINE_NUMBER + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                        WebServerContract.MACHINE_PAST_1ST_DATE + "=" + Past1stDate + "&" +
                        WebServerContract.MACHINE_PAST_2ND_DATE + "=" + Past2ndDate + "&" +
                        WebServerContract.MACHINE_PAST_3RD_DATE + "=" + Past3rdDate + "&" +
                        WebServerContract.MACHINE_PAST_4TH_DATE + "=" + Past4thDate + "&" +
                        WebServerContract.MACHINE_PAST_5TH_DATE + "=" + Past5thDate + "&" +
                        WebServerContract.MACHINE_PAST_6TH_DATE + "=" + Past6thDate + "&" +
                        WebServerContract.MACHINE_PAST_7TH_DATE + "=" + Past7thDate;

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

                JSONObject response = new JSONObject(s);
                if(response.getString(WebServerContract.MACHINE_INFO_SERVER)
                        .equals(WebServerContract.MACHINE_SERVER_ERROR)){
                    Log.e(LOG_TAG, "Web Server Database Failed! Please contact us to fix this.");
                    Toast.makeText(getApplicationContext(),"Web Server Database Failed! Please contact us to fix this."
                            , Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    JSONObject machine_info = response.getJSONObject(WebServerContract.MACHINE_INFO_SERVER);

                    int tmpState;
                    place_textView.setText(place_textView.getText() + machine_info.getString(WebServerContract.MACHINE_PLACE));
                    MachinePlace =  machine_info.getString(WebServerContract.MACHINE_PLACE);
                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_1ST_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past1stday_button.setText(R.string.past1stDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past1stday_button.setText(R.string.past1stDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past1stday_button.setText(R.string.past1stDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past1stday_button.setText(R.string.past1stDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past1stday_button.setText(R.string.past1stDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past1stday_button.setText(R.string.past1stDate_state_5);
                            break;
                        default: past1stday_button.setText(R.string.past1stDate_state_0);
                            break;
                    }

                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_2ND_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past2ndday_button.setText(R.string.past2ndDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past2ndday_button.setText(R.string.past2ndDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past2ndday_button.setText(R.string.past2ndDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past2ndday_button.setText(R.string.past2ndDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past2ndday_button.setText(R.string.past2ndDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past2ndday_button.setText(R.string.past2ndDate_state_5);
                            break;
                        default: past2ndday_button.setText(R.string.past2ndDate_state_0);
                            break;
                    }

                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_3RD_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past3rdday_button.setText(R.string.past3rdDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past3rdday_button.setText(R.string.past3rdDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past3rdday_button.setText(R.string.past3rdDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past3rdday_button.setText(R.string.past3rdDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past3rdday_button.setText(R.string.past3rdDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past3rdday_button.setText(R.string.past3rdDate_state_5);
                            break;
                        default: past3rdday_button.setText(R.string.past3rdDate_state_0);
                            break;
                    }

                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_4TH_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past4thday_button.setText(R.string.past4thDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past4thday_button.setText(R.string.past4thDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past4thday_button.setText(R.string.past4thDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past4thday_button.setText(R.string.past4thDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past4thday_button.setText(R.string.past4thDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past4thday_button.setText(R.string.past4thDate_state_5);
                            break;
                        default: past4thday_button.setText(R.string.past4thDate_state_0);
                            break;
                    }

                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_5TH_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past5thday_button.setText(R.string.past5thDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past5thday_button.setText(R.string.past5thDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past5thday_button.setText(R.string.past5thDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past5thday_button.setText(R.string.past5thDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past5thday_button.setText(R.string.past5thDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past5thday_button.setText(R.string.past5thDate_state_5);
                            break;
                        default: past5thday_button.setText(R.string.past5thDate_state_0);
                            break;
                    }

                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_6TH_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past6thday_button.setText(R.string.past6thDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past6thday_button.setText(R.string.past6thDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past6thday_button.setText(R.string.past6thDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past6thday_button.setText(R.string.past6thDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past6thday_button.setText(R.string.past6thDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past6thday_button.setText(R.string.past6thDate_state_5);
                            break;
                        default: past6thday_button.setText(R.string.past6thDate_state_0);
                            break;
                    }

                    tmpState = machine_info.getInt(WebServerContract.MACHINE_PAST_7TH_DATE);
                    switch (tmpState){
                        case MachineContract.MACHINE_STATE_未紀錄: past7thday_button.setText(R.string.past7thDate_state_0);
                            break;
                        case MachineContract.MACHINE_STATE_使用中: past7thday_button.setText(R.string.past7thDate_state_1);
                            break;
                        case MachineContract.MACHINE_STATE_良好: past7thday_button.setText(R.string.past7thDate_state_2);
                            break;
                        case MachineContract.MACHINE_STATE_問題排除: past7thday_button.setText(R.string.past7thDate_state_3);
                            break;
                        case MachineContract.MACHINE_STATE_通知人員: past7thday_button.setText(R.string.past7thDate_state_4);
                            break;
                        case MachineContract.MACHINE_STATE_其他: past7thday_button.setText(R.string.past7thDate_state_5);
                            break;
                        default: past7thday_button.setText(R.string.past7thDate_state_0);
                            break;
                    }
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }

            super.onPostExecute(s);
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
                CurrentDate = dateFormat.format(Calendar.getInstance().getTime());

                URL url = new URL(POST_MACHINE_INFO_BASE_URL);
                String urlParameters = null;

                switch(state){
                    case MachineContract.MACHINE_STATE_通知人員:
                        urlParameters = WebServerContract.DAILIES_MACHINE_ID + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                                WebServerContract.DAILIES_DATE + "=" + CurrentDate + "&" +
                                WebServerContract.DAILIES_USER_ID + "=" + URLEncoder.encode(params[1], "utf-8") + "&" +
                                WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL + "=" + URLEncoder.encode(params[2], "utf-8") + "&" +
                                WebServerContract.DAILIES_STATE + "=" + MachineContract.MACHINE_STATE_通知人員 + "&" +
                                WebServerContract.DAILIES_USER_NAME + "=" + URLEncoder.encode(params[3], "utf-8");
                        break;

                    case MachineContract.MACHINE_STATE_問題排除:
                        urlParameters = WebServerContract.DAILIES_MACHINE_ID + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                                WebServerContract.DAILIES_DATE + "=" + CurrentDate + "&" +
                                WebServerContract.DAILIES_USER_ID + "=" + URLEncoder.encode(params[1], "utf-8") + "&" +
                                WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL + "=" + URLEncoder.encode(params[2], "utf-8") + "&" +
                                WebServerContract.DAILIES_STATE + "=" + MachineContract.MACHINE_STATE_問題排除 + "&" +
                                WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL + "=" + URLEncoder.encode(params[3], "utf-8") + "&" +
                                WebServerContract.DAILIES_USER_NAME + "=" + URLEncoder.encode(params[4], "utf-8");
                        break;

                    default:
                        urlParameters = WebServerContract.DAILIES_MACHINE_ID + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                                WebServerContract.DAILIES_DATE + "=" + CurrentDate + "&" +
                                WebServerContract.DAILIES_USER_ID + "=" + URLEncoder.encode(params[1], "utf-8") + "&" +
                                WebServerContract.DAILIES_STATE + "=" + state;
                        break;
                }

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
            try{
                if(s == null || s.length() == 0){
                    Toast.makeText(getApplicationContext(), "Failed update.", Toast.LENGTH_SHORT).show();
                }

                JSONObject response = new JSONObject(s);
                String web_server = response.getString("web_server");
                String machine_id = response.getString(WebServerContract.MACHINE_ID);
                if(web_server.equals("failed")){
                    Toast.makeText(getApplicationContext(), "Failed update " + machine_id, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Updated " + machine_id, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            } finally {
                if(problem_dialog != null) problem_dialog.dismiss();
                if(problem_solve_dialog != null) problem_solve_dialog.dismiss();
                onBackPressed();
            }
        }
    }
}
