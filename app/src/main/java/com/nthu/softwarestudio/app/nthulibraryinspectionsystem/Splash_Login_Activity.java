package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Splash_Login_Activity extends AppCompatActivity {

    private EditText userName;
    private EditText userPass;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        userName = (EditText) findViewById(R.id.username_input);
        userPass = (EditText) findViewById(R.id.password_input);
        signIn = (Button) findViewById(R.id.Splash_SignIn);
    }
}
