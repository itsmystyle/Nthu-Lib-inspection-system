package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dodaking on 2016/7/30.
 */
class GetSearchIndex extends AsyncTask<String, Integer, String> {

    Context mContext;
    Boolean networkService = true;

    public GetSearchIndex(Context tmp){
        mContext = tmp;
    }

    @Override
    protected String doInBackground(String... Progress) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String alldatastring = "";

        try{
            Uri builtUri = Uri.parse(WebServerContract.BASE_URL+WebServerContract.LIST_ALL_PROBLEN_URL+"?date="+Progress[0]+"&machine_id="+Progress[1]).buildUpon().build();

            URL url = new URL(builtUri.toString());

            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo == null || !networkInfo.isConnected()){
                networkService = false;
                return null;
            }

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Log.v("Look url:", "Built URI " + builtUri.toString());

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
                buffer.append(line);
            }
            inputStream.close();

            publishProgress(50);

            try{
                Thread.sleep(500);
            }
            catch (InterruptedException e){}

            int pp=51;
            for(pp=51 ; pp<100;pp++){
                try{
                    Thread.sleep(20);
                    publishProgress(pp);
                }
                catch (InterruptedException e){}
            }


            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            alldatastring = buffer.toString();

            //Log.v("take a look:  ", "Forecast string: " + alldatastring);

        } catch (IOException e) {
            Log.e("Somthing Wrong", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Wrong", "Error closing stream", e);
                }
            }
        }



        publishProgress(100);
        return alldatastring;
    }


    @Override
    protected void onPostExecute(String s) {
        try{
            if(s == null || s.length() == 0 || !networkService){
                if(!networkService){
                    Toast.makeText(mContext.getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext.getApplicationContext(), "Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(s.equals("No data")){
                Toast.makeText(mContext,s,Toast.LENGTH_LONG).show();
            }
            else{
                JSONArray reader =new JSONArray(s);
                Intent intent = new Intent(mContext, Detail_Activity.class);
                intent.putExtra(WebServerContract.MACHINE_NUMBER,reader.getJSONObject(0).getString("machine_id"));
                intent.putExtra(WebServerContract.MACHINE_PLACE,reader.getJSONObject(0).getString("place"));
                intent.putExtra(WebServerContract.MACHINE_DATE,reader.getJSONObject(0).getString("date"));
                intent.putExtra(WebServerContract.DAILIES_USER_ID,reader.getJSONObject(0).getString("username"));
                intent.putExtra(WebServerContract.DAILIES_STATE,reader.getJSONObject(0).getString("state"));
                intent.putExtra(WebServerContract.DAILY_PROBLEM_PROBLEM_DETAIL,reader.getJSONObject(0).getString("problem_detail"));
                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DETAIL,reader.getJSONObject(0).getString("comment"));
                intent.putExtra(WebServerContract.DAILY_PROBLEM_SOLVE_DATE,reader.getJSONObject(0).getString("solve_date"));
                mContext.startActivity(intent);
            }
        }catch (JSONException e){e.printStackTrace();}


    }
}