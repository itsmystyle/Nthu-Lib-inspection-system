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
import android.widget.CheckBox;
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
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Splash_Login_Activity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    private EditText userName;
    private EditText userPass;
    private Button signIn;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        userName = (EditText) findViewById(R.id.username_input);
        userPass = (EditText) findViewById(R.id.password_input);
        signIn = (Button) findViewById(R.id.Splash_SignIn);
        rememberMe = (CheckBox) findViewById(R.id.Splash_checkBox);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.length() == 0 || userPass.length() == 0){
                    Toast.makeText(getApplicationContext(), "Username or Password must not be empty. Please try again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Authorization authorization = new Authorization();
                authorization.execute(userName.getText().toString(), userPass.getText().toString());
            }
        });
    }

    class Authorization extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = this.getClass().getSimpleName();
        private final String USER_AUTHORIZATION_URL = WebServerContract.BASE_URL + WebServerContract.USER_AUTHORIZATION_URL;
        private final String TestingServerURL = "https://lib-test-peter3846.c9users.io/users/sign_in";

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            try{
                //URL url = new URL(TestingServerURL);
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

                //String cookieString = "";
                //String csrf_token = "";
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(urlParameters);
                dataOutputStream.flush();
                dataOutputStream.close();

                //getting csrf token
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

                /*char[] charSequence = stringBuffer.toString().toCharArray();
                StringBuffer stringBufferTmp = new StringBuffer();
                for(int i=3976; i < 4064; i++)
                    stringBufferTmp.append(charSequence[i]);

                Log.e(LOG_TAG, stringBufferTmp.toString());
                csrf_token = stringBufferTmp.toString();

                //get cookie
                for(int i=0 ;i<httpURLConnection.getHeaderFields().size(); i++){
                    Log.e(LOG_TAG, httpURLConnection.getHeaderFieldKey(i) + " =" + httpURLConnection.getHeaderField(i));
                }

                CookieManager cookieManager = new CookieManager();
                Map<String, List<String>> headers = httpURLConnection.getHeaderFields();
                List<String> cookiesHeader = headers.get("Set-Cookie");

                if(cookiesHeader != null){
                    for(String cookie: cookiesHeader){
                        cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }

                List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                Iterator<HttpCookie> cookieIterator = cookies.iterator();
                while(cookieIterator.hasNext()){
                    HttpCookie cookie = cookieIterator.next();
                    cookieString += cookie.getName() + "=" + cookie.getValue() + ";";
                }
                Log.e(LOG_TAG, cookieString);
                httpURLConnection.disconnect();

                url = new URL(TestingServerURL);
                String urlParameters = URLEncoder.encode("utf8='✓'&authenticity_token","utf-8") + "='" + URLEncoder.encode(csrf_token, "utf-8") + "'&" +
                        URLEncoder.encode("user[account]", "utf-8") + "='" + URLEncoder.encode(params[0], "utf-8") + "'&" +
                        URLEncoder.encode("user[password]", "utf-8") + "='" + URLEncoder.encode(params[1], "utf-8") + "'&" +
                        URLEncoder.encode("user[remember_me]='0'&commit='登入'", "utf-8");

                String urlParametersTmp = "authenticity_token=" + csrf_token + "&user[account]=" + params[0] + "&user[password]=" + params[1] + "&user[remember_me]=0";

                Log.e(LOG_TAG, urlParameters);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
                httpURLConnection.setRequestProperty("Cookie", cookieString);
                httpURLConnection.setRequestProperty("Content-Language", "en-US");
                httpURLConnection.setUseCaches (false);


                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(urlParameters);
                dataOutputStream.flush();
                dataOutputStream.close();
                */

                /*inputStream = httpURLConnection.getInputStream();
                stringBuffer = new StringBuffer();
                if(inputStream == null){
                    Log.e(LOG_TAG, "InputStream Error");
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0){
                    Log.e(LOG_TAG, "String Buffer Error");
                    return null;
                }

                Log.e(LOG_TAG, stringBuffer.toString());*/
                //Log.e(LOG_TAG, httpURLConnection.getErrorStream().toString());

                //return String.valueOf(httpURLConnection.getResponseCode());

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
            //Log.e(LOG_TAG, s);
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

                        int remeberMe =  rememberMe.isChecked()?1:0;

                        if(accountHelper.insertData(user_name, user_access_token, user_real_name, user_id, remeberMe)){
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
