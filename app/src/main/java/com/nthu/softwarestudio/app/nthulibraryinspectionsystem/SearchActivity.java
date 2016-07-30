package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.ViewContract;
import com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data.WebServerContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    SearchView msearchview;

    String submittext;

    Calendar calendar;
    int year_x, month_x, day_x;

    private ArrayList<String> SUGGESTIONS;
    private SimpleCursorAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(Html.fromHtml("<font color = #FDFDFE>" + "搜尋" + "</font>"));
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(SearchActivity.this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater minflator = getMenuInflater();
        minflator.inflate(R.menu.search_menu, menu);
        final MenuItem msearchItem = menu.findItem(R.id.search_view);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        msearchview = (SearchView) msearchItem.getActionView();
        msearchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        msearchview.setIconifiedByDefault(true);
        msearchview.setQueryHint("只能搜尋編號");
        msearchview.setSuggestionsAdapter(mAdapter);
        int autoCompleteTextViewID = getResources().getIdentifier("search_src_text","id",getPackageName());
        AutoCompleteTextView searchAutoCompleteTextView = (AutoCompleteTextView) msearchview.findViewById(autoCompleteTextViewID);
        searchAutoCompleteTextView.setThreshold(1);



        SharedPreferences prefs = getSharedPreferences("NTHI_LIB_pref", MODE_PRIVATE);
        String forjsondata = prefs.getString("search_index_prf","null");
        SUGGESTIONS = new ArrayList<String>();
        Log.v("forjsondata",forjsondata);
        if(!forjsondata.equals("Somthing Wrong with database")){
            try{
                JSONArray reader = new JSONArray(forjsondata);
                for(int i=0;i<reader.length();i++){
                    SUGGESTIONS.add(reader.getJSONObject(i).getString("machine_id"));
                }
            }
            catch(JSONException e){e.printStackTrace();}
        }


        msearchview.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Integer tmp = position;
                Cursor tmpcursor = msearchview.getSuggestionsAdapter().getCursor();
                if(tmpcursor.getCount() != 0){
                    tmpcursor.moveToFirst();
                    for(int i=0;i<position;i++){
                        tmpcursor.moveToNext();
                    }
                }
                msearchview.setQuery(tmpcursor.getString(1),true);
                tmpcursor.close();
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {

                return true;
            }
        });
        msearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                submittext=s;
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this, R.style.AppTheme_Dialog, dpickerListener, year_x, month_x, day_x);
                datePickerDialog.getDatePicker().setSpinnersShown(true);
                Dialog showDialog = (Dialog) datePickerDialog;
                showDialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

        ((EditText)msearchview.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.rgb(230,230,230));
        ((EditText)msearchview.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.rgb(180,180,180));
        ImageView searchCloseIcon = (ImageView)msearchview.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.ic_action_close_search);





        return super.onCreateOptionsMenu(menu);
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.size(); i++) {
            if (SUGGESTIONS.get(i).toLowerCase().contains(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS.get(i)});
        }
        mAdapter.changeCursor(c);
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date;
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    if(month_x < 10){
                        date = year_x + "-0" + month_x + "-" + day_x;
                    }else{
                        date = year_x + "-" + month_x + "-" + day_x;
                    }

                    GetSearchIndex mindex = new GetSearchIndex(SearchActivity.this);
                    mindex.execute(date,submittext);
                    Toast.makeText(SearchActivity.this,"searching ...",Toast.LENGTH_SHORT).show();
                }
            };



}
