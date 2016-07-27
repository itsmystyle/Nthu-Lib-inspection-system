package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class History_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    Button all_history;
    Button state_history;
    Button search_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        all_history = (Button) findViewById(R.id.history_all_button);
        state_history = (Button) findViewById(R.id.history_state_button);
        search_history = (Button) findViewById(R.id.history_search_button);
    }
}
