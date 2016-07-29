package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;

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

        search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(ViewContract.MODE, ViewContract.INSPECTION);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
