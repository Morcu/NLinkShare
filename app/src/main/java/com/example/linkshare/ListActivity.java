package com.example.linkshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.linkshare.Models.Enlaces;
import com.example.linkshare.adapters.EnlaceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    private EnlaceAdapter eAdapter;
    private RecyclerView eRecycleview;
    private ArrayList<Enlaces> enlacesList = new ArrayList<>();

    public static final String SHARED_PREFS = "sharedPrefs";
    public static String NOTION_ID = "NOTION_ID";
    public static String NOTION_DATABASE_ID = "NOTION_DATABASE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);


        eRecycleview = findViewById(R.id.myReciclerView);
        eRecycleview.setLayoutManager(new LinearLayoutManager(this));

        getDataForLayout(getApplicationContext());
    }

    // Load data from shared preferences
    public String loadData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void getDataForLayout(Context context){

        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String notion_id = loadData(NOTION_ID);
        String notion_database_id = loadData(NOTION_DATABASE_ID);
        String url = "https://api.notion.com/v1/databases/" + notion_database_id + "/query";

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());
                        enlacesList.clear();
                        try {
                            JSONObject obj = new JSONObject(response.toString());
                            JSONArray resutls = obj.getJSONArray("results");
                            for (int i = 0; i < resutls.length(); i++) {
                                try{
                                    JSONObject listDetail = resutls.getJSONObject(i);
                                    Log.e("JSON", " JSOOOOON" );
                                    String texto = listDetail.getJSONObject("properties").getJSONObject("Description").getJSONArray("rich_text").getJSONObject(0).getJSONObject("text").getString("content");
                                    Log.e("JSON", " " + texto);


                                    enlacesList.add(new Enlaces(texto, "descripcion", "img_url", "url2"));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                            eAdapter = new EnlaceAdapter(context, enlacesList, R.layout.list_view);
                            eRecycleview.setAdapter(eAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("user","YOUR USERNAME");
                //  params.put("pass","YOUR PASSWORD");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + notion_id);
                params.put("Notion-Version", "2021-08-16");
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(sr);

 /*
        enlacesList.clear();

        for (int i = 0; i < 4; i++) {

            String texto = "Texto " + i;
            String descripcion = "Descripcion " + i;
            String img_url = "https://media.istockphoto.com/photos/madrid-spain-on-gran-via-picture-id1297090032?b=1&k=20&m=1297090032&s=170667a&w=0&h=OLFFlSPXDqXq7SZaLMTUpGJh-bz7FKRCjnOTGBT7GRc=";
            String url2 = "http://example.com";
            enlacesList.add(new Enlaces(texto, descripcion, img_url, url2));
        }

        eAdapter = new EnlaceAdapter(context, enlacesList, R.layout.list_view);
        eRecycleview.setAdapter(eAdapter);
        */
    }

}