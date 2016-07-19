package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;

public class Splash_Activity extends AppCompatActivity {

    AccountHelper accountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        accountHelper = new AccountHelper(getApplicationContext());
        if(accountHelper.isEmpty()){
            //empty start new login activity

            Intent intent = new Intent(getApplicationContext(), Splash_Login_Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else{
            //not empty start loading activity

            Intent intent = new Intent(getApplicationContext(), Main_Menu_Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}