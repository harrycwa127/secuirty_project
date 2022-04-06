package com.example.guitest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private String[] temp;
    private TextView title;

    //db variable
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        title = findViewById(R.id.item_title);
        title.setText("Item list");

        //db version
        ListView listView = findViewById(R.id.content_list);
        //create db
        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        if (intent != null) {
            //get record
            int count = dbHelper.getCount();
            temp = new String[count];
            for (int i = 0; i < count; i++) {
                Cursor c = dbHelper.getData((i+1));
                c.moveToFirst();
                temp[i] = c.getString(1);
            }

            //put record to item list
            final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, temp);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), ItemDisplayActivity.class);
                intent.putExtra("id",String.valueOf((position+1)));
                startActivity(intent);
                }
            });
        }
    }
}




