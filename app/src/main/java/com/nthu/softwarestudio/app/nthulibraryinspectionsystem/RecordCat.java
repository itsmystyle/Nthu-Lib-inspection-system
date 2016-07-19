package com.nthu.softwarestudio.app.nthulibraryinspectionsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RecordCat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_cat);

        ListView mlist = (ListView) findViewById(R.id.RecordCat_listView);
        String[] values = new String[]{"All","Cat","Search"};
        ListAdapter ma = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1 , values);
        mlist.setAdapter(ma);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long tmp=id;
                Toast.makeText(RecordCat.this,tmp.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
