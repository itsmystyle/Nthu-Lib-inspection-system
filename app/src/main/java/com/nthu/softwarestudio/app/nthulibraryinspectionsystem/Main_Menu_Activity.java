package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main_Menu_Activity extends AppCompatActivity {

    ProgressBar progressBar;
    private int progress =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        progressBar = (ProgressBar) findViewById(R.id.loading_data_progressbar);

        progressBar.setMax(100);
        progressBar.setProgress(progress);

        LoadingProgressBar loadingProgressBar = new LoadingProgressBar();
        loadingProgressBar.execute(progress);
    }

    class LoadingProgressBar extends AsyncTask<Integer, Integer, String>{

        @Override
        protected String doInBackground(Integer... Progress) {
            int progress = Progress[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String alldatastring = "";


            try{

                Uri b2 = Uri.parse(WebServerContract.BASE_URL+WebServerContract.UPDATE_DATA).buildUpon().build();
                URL u2 =new URL(b2.toString());
                HttpURLConnection uc2 = (HttpURLConnection)u2.openConnection();
                uc2.setRequestMethod("GET");
                uc2.connect();
                uc2.getInputStream();
                if(uc2!=null) uc2.disconnect();
                //Log.v("Look url:", "Built URI " + b2.toString());
                publishProgress(25);
                try{
                    Thread.sleep(500);
                }
                catch (InterruptedException e){}


                Uri builtUri = Uri.parse(WebServerContract.BASE_URL+WebServerContract.SEARCH_INDEX).buildUpon().build();
                URL url = new URL(builtUri.toString());
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences.Editor editor;
            editor = getSharedPreferences("NTHI_LIB_pref", MODE_PRIVATE).edit();
            editor.putString("search_index_prf",s);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}
