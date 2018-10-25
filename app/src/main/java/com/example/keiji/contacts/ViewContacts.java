package com.example.keiji.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keiji on 3/24/2018.
 */

public class ViewContacts extends AppCompatActivity {
    private ListView mListView;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        FloatingActionButton fab = findViewById(R.id.add_fab);

        DatabaseHandler mydb = new DatabaseHandler(getApplicationContext());

        mListView = findViewById(R.id.ContactsListView);

        final ArrayList<Contact> contactList = mydb.getAllContacts();


        final ArrayAdapter<Contact> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, contactList);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Intent i = new Intent(ViewContacts.this, MainActivity.class);
                // add the current position's TABLE id, and pass as extra
                int contactId = adapter.getItem(position).get_id();
                i.putExtra("contactId", contactId);
                Log.d(TAG, "ID of Contact being selected: "+contactId);
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Show main activity in 'Add' state
                Intent intent = new Intent(ViewContacts.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}