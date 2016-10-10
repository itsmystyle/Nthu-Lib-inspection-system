package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    AccountHelper accountHelper;

    Button inspectionButton;
    Button historyButton;
    Button statisticButton;
    Button settingButton;
    Button signOutButton;

    Dialog settingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountHelper = new AccountHelper(getApplicationContext());

        inspectionButton = (Button) findViewById(R.id.main_menu_button_inspection);
        historyButton = (Button) findViewById(R.id.main_menu_button_inspection_history);
        statisticButton = (Button) findViewById(R.id.main_menu_button_statistics);
        settingButton = (Button) findViewById(R.id.main_menu_button_setting);
        signOutButton = (Button) findViewById(R.id.main_menu_button_log_out);

        inspectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Floor_Activity.class);
                intent.putExtra(ViewContract.MODE, ViewContract.INSPECTION);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), History_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        statisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Device_Activity.class);
                intent.putExtra(ViewContract.MODE, ViewContract.STATE);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BulletinButtonActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountHelper.deleteData();

                Intent intent = new Intent(getApplicationContext(), Splash_Login_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }
}
