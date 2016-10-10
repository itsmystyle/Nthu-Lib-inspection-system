package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.AccountHelper;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BulletinButtonActivity extends AppCompatActivity {
    final String LOG_TAG = this.getClass().getSimpleName();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    android.support.design.widget.FloatingActionButton addBulletinButton;
    List<BulletinData> Data = new ArrayList<>();
    Calendar calendar;
    int year_x, month_x, day_x;
    DatePicker datePicker;
    BulletinData toPost;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_button);

        recyclerView = (RecyclerView) findViewById(R.id.messagelistview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext())
        );
        recyclerViewAdapter = new RecyclerViewAdapter(Data, getApplicationContext());
        recyclerView.setAdapter(recyclerViewAdapter);

        UpdateData();

        addBulletinButton = (FloatingActionButton) findViewById(R.id.message_add);
        addBulletinButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new Dialog(v.getContext(), R.style.AppTheme_Dialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.bulletinmessagedialog);

                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                            WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.setCanceledOnTouchOutside(false);

                        calendar = Calendar.getInstance();
                        year_x = calendar.get(Calendar.YEAR);
                        month_x = calendar.get(Calendar.MONTH);
                        day_x = calendar.get(Calendar.DAY_OF_MONTH);

                        final EditText message = (EditText) dialog.findViewById(R.id.bulletin_message);
                        datePicker = (DatePicker) dialog.findViewById(R.id.bulletin_datepicker);
                        Button buttonSubmit = (Button) dialog.findViewById(R.id.bulletin_submit_button);
                        Button buttonCancel = (Button) dialog.findViewById(R.id.bulletin_cancel_button);
                        Button buttonChangeDate = (Button) dialog.findViewById(R.id.bulletin_change_date_button);

                        buttonChangeDate.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                                        datePickerDialog.getDatePicker().setSpinnersShown(true);
                                        Dialog showDialog = (Dialog) datePickerDialog;
                                        showDialog.setCanceledOnTouchOutside(false);
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                        showDialog.show();
                                    }
                                }
                        );

                        datePicker.updateDate(year_x, month_x, day_x);

                        buttonSubmit.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String date;

                                        String MON = String.valueOf(month_x + 1);

                                        if(month_x < 10) MON = "0" + MON;

                                        String DAY = String.valueOf(day_x);

                                        if(day_x < 10) DAY = "0"+ DAY;

                                        date = year_x + "-" + MON + "-" + DAY;

                                        //testing
                                        /*PostMessage postMessage = new PostMessage();
                                        postMessage.execute("緣彩", date, message.getText().toString());*/

                                        AccountHelper accountHelper = new AccountHelper(getApplicationContext());
                                        String username = accountHelper.getUserName();

                                        PostMessage postMessage = new PostMessage();
                                        postMessage.execute(username, date, message.getText().toString());
                                    }
                                }
                        );

                        buttonCancel.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.onBackPressed();
                                    }
                                }
                        );

                        dialog.show();
                    }
                }
        );
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear;
                    day_x = dayOfMonth;

                    String MON = String.valueOf(month_x);

                    if(month_x < 10) MON = "0" + MON;

                    String DAY = String.valueOf(day_x);

                    if(day_x < 10) DAY = "0" + DAY;

                    datePicker.updateDate(year_x, month_x, day_x);
                }
            };

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
        private final String LOG_TAG = this.getClass().getSimpleName();
        List<BulletinData> DataSet;
        Context context;

        public RecyclerViewAdapter(List<BulletinData> dataSet, Context context) {
            DataSet = dataSet;
            this.context = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView username;
            TextView date;
            TextView message;
            Button edit;
            Button delete;
            Button pin;

            public ViewHolder(View itemView) {
                super(itemView);
                username = (TextView) itemView.findViewById(R.id.bulletin_textView_username);
                date = (TextView) itemView.findViewById(R.id.bulletin_textView_date);
                message = (TextView) itemView.findViewById(R.id.bulletin_textView_message);
                edit = (Button) itemView.findViewById(R.id.bulletinboard_edit_button);
                delete = (Button) itemView.findViewById(R.id.bulletinboard_delete_button);
                pin = (Button) itemView.findViewById(R.id.bulletinboard_pin_button);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bulletin_message_info,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.username.setText(DataSet.get(position).getUsename());
            holder.date.setText(DataSet.get(position).getStartDate());
            holder.message.setText(DataSet.get(position).getMessage());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(250);

                    dialog = new Dialog(v.getContext(), R.style.AppTheme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.bulletinmessagedialog);

                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.setCanceledOnTouchOutside(false);

                    year_x = Integer.parseInt(DataSet.get(position).getYear());
                    month_x = Integer.parseInt(DataSet.get(position).getMonth()) - 1;
                    day_x = Integer.parseInt(DataSet.get(position).getDay());

                    final EditText message = (EditText) dialog.findViewById(R.id.bulletin_message);
                    datePicker = (DatePicker) dialog.findViewById(R.id.bulletin_datepicker);
                    Button buttonSubmit = (Button) dialog.findViewById(R.id.bulletin_submit_button);
                    Button buttonCancel = (Button) dialog.findViewById(R.id.bulletin_cancel_button);
                    Button buttonChangeDate = (Button) dialog.findViewById(R.id.bulletin_change_date_button);

                    message.setText(DataSet.get(position).getMessage());

                    buttonChangeDate.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                                    datePickerDialog.getDatePicker().setSpinnersShown(true);
                                    Dialog showDialog = (Dialog) datePickerDialog;
                                    showDialog.setCanceledOnTouchOutside(false);
                                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                    showDialog.show();
                                }
                            }
                    );

                    datePicker.updateDate(year_x, month_x, day_x);

                    buttonSubmit.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String date;

                                    String MON = String.valueOf(month_x + 1);

                                    if(month_x < 10) MON = "0" + MON;

                                    String DAY = String.valueOf(day_x);

                                    if(day_x < 10) DAY = "0"+ DAY;

                                    date = year_x + "-" + MON + "-" + DAY;

                                    //testing
                                    EditMessage editMessage = new EditMessage();
                                    editMessage.execute(String.valueOf(DataSet.get(position).getMessageId()), date, message.getText().toString());
                                }
                            }
                    );

                    buttonCancel.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.onBackPressed();
                                }
                            }
                    );

                    dialog.show();


                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(250);
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.execute(String.valueOf(DataSet.get(position).getMessageId()));
                }
            });

            holder.pin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(250);
                    PinMessage pinMessage = new PinMessage();
                    String important = (DataSet.get(position).getImportant() == 1) ? "0" : "1";
                    pinMessage.execute(String.valueOf(DataSet.get(position).getMessageId()), important);
                }
            });
        }

        @Override
        public int getItemCount() {
            return DataSet.size();
        }

        public void updateData(List<BulletinData> DataSet){
            this.DataSet = DataSet;
            notifyDataSetChanged();
        }
    }

    public class BulletinData{
        String usename;
        String startDate;
        String endDate;
        String message;
        int messageId;
        int important;

        public BulletinData(int messageId, String message, String endDate, String startDate, String usename, int important) {
            this.messageId = messageId;
            this.message = message;
            this.endDate = endDate;
            this.startDate = startDate;
            this.usename = usename;
            this.important = important;
        }

        public int getImportant() {
            return important;
        }

        public void setImportant(int important) {
            this.important = important;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public String getUsename() {
            return usename;
        }

        public void setUsename(String usename) {
            this.usename = usename;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMonth(){
            String[] tmp = getEndDate().split("-");
            if(tmp[1] != null) return tmp[1];
            return null;
        }

        public String getDay(){
            String[] tmp = getEndDate().split("-");
            if(tmp[2] != null) return tmp[2];
            return null;
        }

        public String getYear(){
            String[] tmp = getEndDate().split("-");
            if(tmp[0] != null) return tmp[0];
            return null;
        }
    }

    public class PostMessage extends AsyncTask<String, Void, String>{
        final String LOG_TAG = getClass().getSimpleName();

        Boolean networkService = true;
        HttpURLConnection httpURLConnection;
        final String BASE_URL = WebServerContract.BASE_URL + "/postmessage.php";

        @Override
        protected String doInBackground(String... params) {
            String param = null;
            try {
                param = WebServerContract.USERNAME + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                                WebServerContract.DATE + "=" + params[1] + "&" +
                                WebServerContract.MESSAGE + "=" + URLEncoder.encode(params[2], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.e(LOG_TAG, BASE_URL + " " + param);

            try {
                URL url = new URL(BASE_URL);

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
                dataOutputStream.writeBytes(param);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if(stringBuffer == null) return null;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0) return null;

                Log.v(LOG_TAG, stringBuffer.toString());

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null || s.length() == 0 || !networkService){
                if(!networkService){
                    Toast.makeText(getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            try {
                JSONObject result = new JSONObject(s);
                String web_server = result.getString("web_server");
                if(web_server.equals("success")){
                    Toast.makeText(getApplicationContext(), "Done!",
                            Toast.LENGTH_SHORT).show();
                    if(dialog != null) dialog.onBackPressed();
                    UpdateData();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: failed. Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    public class PinMessage extends AsyncTask<String, Void, String>{
        final String LOG_TAG = getClass().getSimpleName();

        Boolean networkService = true;
        HttpURLConnection httpURLConnection;
        final String BASE_URL = WebServerContract.BASE_URL + "/pinmessage.php";

        int state;

        @Override
        protected String doInBackground(String... params) {
            String param = WebServerContract.MESSAGE_ID + "=" + params[0] + "&" +
                            WebServerContract.IMPORTANT + "=" + params[1];
            state = Integer.parseInt(params[1]);

            Log.e(LOG_TAG, BASE_URL + " " + param);

            try {
                URL url = new URL(BASE_URL);

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
                dataOutputStream.writeBytes(param);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if(stringBuffer == null) return null;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0) return null;

                Log.v(LOG_TAG, stringBuffer.toString());

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null || s.length() == 0 || !networkService){
                if(!networkService){
                    Toast.makeText(getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            try {
                JSONObject result = new JSONObject(s);
                String web_server = result.getString("web_server");
                if(web_server.equals("success")){
                    if(state == 1)
                        Toast.makeText(getApplicationContext(), "Pinned message!",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Unpinned message!",
                                Toast.LENGTH_SHORT).show();
                    UpdateData();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: failed. Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    public class DeleteMessage extends AsyncTask<String, Void, String>{
        final String LOG_TAG = getClass().getSimpleName();

        Boolean networkService = true;
        HttpURLConnection httpURLConnection;
        final String BASE_URL = WebServerContract.BASE_URL + "/deletemessage.php";

        @Override
        protected String doInBackground(String... params) {
            String param = WebServerContract.MESSAGE_ID + "=" + params[0];

            Log.e(LOG_TAG, BASE_URL + " " + param);

            try {
                URL url = new URL(BASE_URL);

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
                dataOutputStream.writeBytes(param);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if(stringBuffer == null) return null;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0) return null;

                Log.v(LOG_TAG, stringBuffer.toString());

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null || s.length() == 0 || !networkService){
                if(!networkService){
                    Toast.makeText(getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            try {
                JSONObject result = new JSONObject(s);
                String web_server = result.getString("web_server");
                if(web_server.equals("success")){
                    Toast.makeText(getApplicationContext(), "Deleted message!",
                            Toast.LENGTH_SHORT).show();
                    UpdateData();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: failed. Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    public class EditMessage extends AsyncTask<String, Void, String>{
        final String LOG_TAG = getClass().getSimpleName();

        Boolean networkService = true;
        HttpURLConnection httpURLConnection;
        final String BASE_URL = WebServerContract.BASE_URL + "/editmessage.php";

        @Override
        protected String doInBackground(String... params) {
            String param = null;
            try {
                param = WebServerContract.MESSAGE_ID + "=" + URLEncoder.encode(params[0], "utf-8") + "&" +
                        WebServerContract.DATE + "=" + params[1] + "&" +
                        WebServerContract.MESSAGE + "=" + URLEncoder.encode(params[2], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.e(LOG_TAG, BASE_URL + " " + param);

            try {
                URL url = new URL(BASE_URL);

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
                dataOutputStream.writeBytes(param);
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if(stringBuffer == null) return null;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0) return null;

                Log.v(LOG_TAG, stringBuffer.toString());

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null || s.length() == 0 || !networkService){
                if(!networkService){
                    Toast.makeText(getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            try {
                JSONObject result = new JSONObject(s);
                String web_server = result.getString("web_server");
                if(web_server.equals("success")){
                    Toast.makeText(getApplicationContext(), "Edited message!",
                            Toast.LENGTH_SHORT).show();
                    if(dialog != null) dialog.onBackPressed();
                    UpdateData();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: failed. Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    public class GetMessage extends AsyncTask<String, Void, String>{
        final String LOG_TAG = getClass().getSimpleName();

        HttpURLConnection httpURLConnection;
        final String BASE_URL = WebServerContract.BASE_URL + "/getmessage.php";
        Boolean networkService = true;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(BASE_URL);

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected()){
                    networkService = false;
                    return null;
                }

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(false);

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if(inputStream == null) return null;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                inputStream.close();

                if(stringBuffer.length() == 0) return null;

                Log.v(LOG_TAG, stringBuffer.toString());

                return stringBuffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == null || s.length() == 0 || !networkService){
                if(!networkService){
                    Toast.makeText(getApplicationContext(), "Unable to connect to internet. Please check for network service.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            try {
                JSONObject result = new JSONObject(s);
                String web_server = result.getString("web_server");
                if(web_server.equals("success")){
                    int lenght = result.getInt("length");
                    if(lenght > 0){
                        JSONArray jsonArray = result.getJSONArray("data");
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject tmp = jsonArray.getJSONObject(i);
                            String username = tmp.getString(WebServerContract.USERNAME);
                            String postdate = tmp.getString(WebServerContract.POST_DATE);
                            String enddate = tmp.getString(WebServerContract.END_DATE);
                            String message = tmp.getString(WebServerContract.MESSAGE);
                            int messageId = tmp.getInt(WebServerContract.MESSAGE_ID);
                            int important = tmp.getInt(WebServerContract.IMPORTANT);

                            Data.add(new BulletinData(messageId, message, enddate, postdate, username, important));
                        }

                        recyclerViewAdapter.updateData(Data);
                    }else{
                        recyclerViewAdapter.updateData(Data);
                        Toast.makeText(getApplicationContext(), "Bulletin Board is empty. Create a bulletin message.",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Error: failed. Unable to connect to server. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateData(){
        Data = new ArrayList<>();
        GetMessage getMessage = new GetMessage();
        getMessage.execute();
    }
}
