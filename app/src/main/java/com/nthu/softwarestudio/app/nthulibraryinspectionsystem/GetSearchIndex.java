package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
class GetSearchIndex extends AsyncTask<Integer, Integer, String> {

    private Context mContext;
    private ArrayList<String> mIndex;

    public GetSearchIndex(ArrayList<String> tmpstring){
        mIndex = tmpstring;
    }

    @Override
    protected String doInBackground(Integer... Progress) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String alldatastring = "";


        try{
            Uri builtUri = Uri.parse(WebServerContract.BASE_URL+WebServerContract.SEARCH_INDEX).buildUpon().build();
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.v("Look url:", "Built URI " + builtUri.toString());

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

            Log.v("take a look:  ", "Forecast string: " + alldatastring);

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
        if(!s.equals("Somthing Wrong with database")){
            try{
                JSONArray reader = new JSONArray(s);
                for(int i=0;i<reader.length();i++){
                    mIndex.add(reader.getJSONObject(i).getString("machine_id"));
                }
            }
            catch(JSONException e){e.printStackTrace();}
        }
    }
}