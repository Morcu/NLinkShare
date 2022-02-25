package com.example.linkshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnLista;
    private Button btnOnboard;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static String NOTION_ID = "NOTION_ID";
    public static String NOTION_DATABASE_ID = "NOTION_DATABASE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLista = findViewById(R.id.btnLista);
        btnOnboard = findViewById(R.id.btnOnboard);

        String notion_id = loadData(NOTION_ID);
        String notion_database_id = loadData(NOTION_DATABASE_ID);

        Log.e("ID", notion_id);
        Log.e("ID", notion_database_id);

        if (notion_id.isEmpty() || notion_database_id.isEmpty()){
            Intent myIntent = new Intent(this, OnboardActivity.class);
            startActivity(myIntent);
        }

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(myIntent);
            }
        });

        btnOnboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, OnboardActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public String loadData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}