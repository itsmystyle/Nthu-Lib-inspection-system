package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;
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
import java.net.URL;

public class Splash_Login_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authorization authorization = new Authorization();
                authorization.execute(userName.getText().toString(), userPass.getText().toString());
            }
        });
    }

    class Authorization extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = this.getClass().getSimpleName();
        private final String USER_AUTHORIZATION_URL = WebServerContract.BASE_URL + WebServerContract.USER_AUTHORIZATION_URL;

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL(USER_AUTHORIZATION_URL);
                String urlParameters = WebServerContract.USER_AUTHORIZATION_USERID + "=" + params[0] + "&" +
                                        WebServerContract.USER_AUTHORIZATION_USERPASSWORD + "=" + params[1];

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

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, e.getMessage() + "Error MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage() + "Error IOException");
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
                if(reponse.getString(WebServerContract.USER_AUTHORIZATION_SERVER)
                        .equals(WebServerContract.USER_AUTHORIZATION_SERVER_ERROR)){
                    Log.e(LOG_TAG, "Web Server Database Failed! Please contact us to fix this.");
                    Toast.makeText(getApplicationContext(),"Web Server Database Failed! Please contact us to fix this."
                            , Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    String secure = (reponse.getJSONObject(WebServerContract.USER_AUTHORIZATION_SERVER))
                                            .getString(WebServerContract.USER_AUTHORIZATION_SERVER_SECURE);

                    if(secure.equals(WebServerContract.USER_AUTHORIZATION_SERVER_SECURE_UNAUTHORIZED)){
                        Toast.makeText(getApplicationContext(),"Wrong Username or Password. Please try again."
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        JSONObject server_data = reponse.getJSONObject(WebServerContract.USER_AUTHORIZATION_SERVER);

                        Integer user_id = server_data.getInt(WebServerContract.USER_AUTHORIZATION_USER_ID_AUTOINCREAMENT);
                        String user_name = userName.getText().toString();
                        String user_real_name = server_data.getString(WebServerContract.USER_AUTHORIZATION_USER_NAME);
                        String user_access_token = server_data.getString(WebServerContract.USER_AUTHORIZATION_USER_ACCESS_TOKEN);

                        AccountHelper accountHelper = new AccountHelper(getApplicationContext());
                        accountHelper.deleteData();
                        if(accountHelper.insertData(user_name, user_access_token, user_real_name, user_id)){
                            Log.v(LOG_TAG, "inserted to database");
                        }else{
                            Log.e(LOG_TAG, "failed to insert to database");
                        }

                        Intent intent = new Intent(getApplicationContext(), Main_Menu_Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
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
