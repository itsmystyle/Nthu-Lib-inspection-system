package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

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

        testProgressBar testProgressBar = new testProgressBar();
        testProgressBar.execute(progress);
    }

    class testProgressBar extends AsyncTask<Integer, Integer, Void>{

        @Override
        protected Void doInBackground(Integer... Progress) {
            int progress = Progress[0];

            while(progress < 100){
                progress += 10;

                try {
                    publishProgress(progress);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
