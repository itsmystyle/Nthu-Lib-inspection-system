package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SearchView msearchview;

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
        MenuItem msearchItem = menu.findItem(R.id.search_view);
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
                // Your code here
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });
        msearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
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
            if (SUGGESTIONS.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS.get(i)});
        }
        mAdapter.changeCursor(c);
    }



}
