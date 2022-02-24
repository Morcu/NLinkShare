package com.example.linkshare;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;

import java.util.Map;
import java.util.Objects;


public class ReceiveActivity extends AppCompatActivity {

    private TextView txtRecTitulo;
    private TextView txtRecDesc;
    private Button btnRecCrear;
    private ImageView imgRecImg;
    private String imageURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_layout);
        Log.e("AAAAAAA", "QUE PASA AQUIII");
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Log.e("III", intent.toString() + "   " + action.toString() + "   "+  type.toString());
        txtRecTitulo = findViewById(R.id.txtRecTitulo);
        txtRecDesc = findViewById(R.id.txtRecDesc);
        btnRecCrear = findViewById(R.id.btnRecCrear);
        imgRecImg = findViewById(R.id.imgRecImg);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                Log.e("SEND", intent.toString());

                Content content = new Content();
                String url = intent.getStringExtra(Intent.EXTRA_TEXT);
                Log.e("URL PRE", url);
                content.execute(url);
            }
        }

        btnRecCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> linkMap = new HashMap<>();
                linkMap.put("titulo", txtRecTitulo.getText().toString());
                linkMap.put("descripcion", txtRecDesc.getText().toString());
                linkMap.put("img_url", imageURL);
                linkMap.put("url", url);
                //tDatabase.child("enlace").push().setValue(linkMap);
                Intent myIntent = new Intent(ReceiveActivity.this, ListActivity.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private void setText(final String title, final String desc, final String url){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtRecTitulo.setText(title);
                txtRecDesc.setText(desc);
                Picasso.with(ReceiveActivity.this)
                        .load(url)
                        .error(R.mipmap.ic_launcher)
                        .into(imgRecImg);
                imageURL = url;
            }
        });
    }

    private class Content extends AsyncTask<String,Void,Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            //progressBar.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressBar.setVisibility(View.GONE);
            //progressBar.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            //adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... url) {

            try {
                Log.e("URL", url[0]);
                Document doc = Jsoup.connect(url[0]).get();

                Log.e("TITLE", doc.title());

                setText(
                        doc.title().toString(), doc.select("meta[name=description]").get(0)
                        .attr("content").toString(),
                        doc.head().select("link[href~=.*\\.(ico|png)]").last().attr("href")
                );



            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}