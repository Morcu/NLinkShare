package com.example.linkshare;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.linkshare.Models.Enlaces;
import com.example.linkshare.adapters.EnlaceAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReceiveActivity extends AppCompatActivity {

    private TextView txtRecTitulo;
    private TextView txtRecDesc;
    private Button btnRecCrear;
    private ImageView imgRecImg;
    private String imageURL;
    private String url;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static String NOTION_ID = "NOTION_ID";
    public static String NOTION_DATABASE_ID = "NOTION_DATABASE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_layout);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

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
                List<String> extractedUrls = extractUrls(url);
                Log.e("Extracted URL", extractedUrls.toString());
                Log.e("Extracted URL", extractedUrls.get(0));

                try {
                    content.execute(extractedUrls.get(0));
                }catch (Exception e){
                    Toast.makeText(this, "Invalid url", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(this, ListActivity.class);
                    startActivity(myIntent);
                }

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

                //TODO: Hacer push!!!!

    //--------

                RequestQueue queue = Volley.newRequestQueue(ReceiveActivity.this);

                String notion_id = loadData(NOTION_ID);
                String notion_database_id = loadData(NOTION_DATABASE_ID);
                String url_pages = "https://api.notion.com/v1/pages";

                String json_string = "{" +
                        " 'parent': { 'database_id': '"+ notion_database_id+"' }," +
                        " 'properties': {" +
                        "  'Name': {" +
                        "   'title': [" +
                        "    {" +
                        "     'text': {" +
                        "      'content': '"+ txtRecTitulo.getText().toString()+"'" +
                        "     }" +
                        "    }" +
                        "   ]" +
                        "  }," +
                        "  'Description': {" +
                        "   'rich_text': [" +
                        "    {" +
                        "     'text': {" +
                        "      'content': '"+ txtRecDesc.getText().toString() +"'" +
                        "     }" +
                        "    }" +
                        "   ]" +
                        "  }," +
                        "        'Link': {" +
                        "            'url': '"+ url +"'" +
                        "        }," +
                        "        'Image':{" +
                        "            'files': [" +
                        "            {" +
                        "                'name': 'Filename'," +
                        "                'type': 'external'," +
                        "                'external': {" +
                        "                    'url': '"+ imageURL +"'" +
                        "                }" +
                        "                }" +
                        "            ]" +
                        "        }" +
                        "    }" +
                        "}";

                JSONObject js = null;
                try {
                    js = new JSONObject(json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST,url_pages, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent myIntent = new Intent(ReceiveActivity.this, ListActivity.class);
                                startActivity(myIntent);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error: " + error.getMessage());

                    }
                })

                {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + notion_id);
                        params.put("Notion-Version", "2021-08-16");
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(jsonObjReq);


            }
        });

    }

    // Finish the activity to reload data each time
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


    /**
     * Returns a list with all links contained in the input
     */
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public String loadData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    // Setting the front data
    private void setText(final String title, final String desc, final String image_url, final String url_save){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtRecTitulo.setText(title);
                txtRecDesc.setText(desc);
                Picasso.with(ReceiveActivity.this)
                        .load(image_url)
                        .error(R.mipmap.ic_launcher)
                        .into(imgRecImg);
                imageURL = image_url;
                url = url_save;
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
            Log.e("QQQQQQ", Arrays.toString(url));
            Log.e("QQQQQQ", url[0]);

            try {
                // Get the HTML for the URL
                Document doc = Jsoup.connect(url[0]).get();
                // Set the title, description and Image (favicon)
                String title;
                String desc;
                String img_url_s;

                try {
                    title = doc.title();
                } catch (Exception e) {
                    e.printStackTrace();
                    title = "Default Title";
                }
                try {
                    desc = doc.select("meta[name=description]").get(0)
                            .attr("content");
                } catch (Exception e) {
                    e.printStackTrace();
                    desc = "Default description";
                }
                try {
                    img_url_s = doc.head().select("link[href~=.*\\.(ico|png)]").last().attr("href");
                } catch (Exception e) {
                    e.printStackTrace();
                    img_url_s = ""+R.drawable.ic_launcher_background;
                }


                setText(
                        title, desc, img_url_s, url[0]
                );

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}