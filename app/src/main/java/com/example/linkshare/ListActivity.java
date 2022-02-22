package com.example.linkshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.linkshare.Models.Enlaces;
import com.example.linkshare.adapters.EnlaceAdapter;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private EnlaceAdapter eAdapter;
    private RecyclerView eRecycleview;
    private ArrayList<Enlaces> enlacesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);


        eRecycleview = findViewById(R.id.myReciclerView);
        eRecycleview.setLayoutManager(new LinearLayoutManager(this));

        getDataForLayout(getApplicationContext());
    }

    private void getDataForLayout(Context context){

        enlacesList.clear();
        for (int i = 0; i < 4; i++) {

            String texto = "Texto " + i;
            String descripcion = "Descripcion " + i;
            String img_url = "https://media.istockphoto.com/photos/madrid-spain-on-gran-via-picture-id1297090032?b=1&k=20&m=1297090032&s=170667a&w=0&h=OLFFlSPXDqXq7SZaLMTUpGJh-bz7FKRCjnOTGBT7GRc=";
            String url = "http://example.com";
            enlacesList.add(new Enlaces(texto, descripcion, img_url, url));
        }
        eAdapter = new EnlaceAdapter(context, enlacesList, R.layout.list_view);
        eRecycleview.setAdapter(eAdapter);
    }

}