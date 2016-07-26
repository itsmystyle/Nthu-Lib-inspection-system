package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;
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
import java.util.ArrayList;
import java.util.List;

public class Device_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    TableLayout tableLayout;
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

        tableLayout = (TableLayout) findViewById(R.id.table_layout);

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
        }else{

        }
    }

    private void populateTableButton(List<String> machines_list, int mode) {
        int tableRow = (int) Math.floor(machines_list.size()/3);
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
                    tableButton.setText(machines_list.get(row*3 + col));
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);

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
                    tableButton.setText(machines_list.get(tableRow*3 + col));
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                }else{
                    tableButton.setText("");
                    tableButton.setTextSize(getResources().getDimension(R.dimen.tableButtonSize));
                    tableButton.setBackgroundResource(R.drawable.rounded_button_menu);
                    tableButton.setVisibility(View.INVISIBLE);
                }

                tRow.addView(tableButton);
            }

        }else{
            for(int row = 0; row < tableRow; row++){
                tRow = new TableRow(this);
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

                Log.e(LOG_TAG, url.toString() + " " + urlParameters);

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

                Log.e(LOG_TAG, stringBuffer.toString());

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

                        List<String> machines_list = new ArrayList<String>();
                        for(int i=0; i<machines_info.length(); i++)
                            machines_list.add(machines_info.getJSONObject(i).getString("machine_id"));

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
}
