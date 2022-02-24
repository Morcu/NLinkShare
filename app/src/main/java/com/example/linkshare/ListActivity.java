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

    public Boolean sol_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);


        eRecycleview = findViewById(R.id.myReciclerView);
        eRecycleview.setLayoutManager(new LinearLayoutManager(this));
        // Comprobar que tienen las claves
        String notion_id = loadData(NOTION_ID);
        String notion_database_id = loadData(NOTION_DATABASE_ID);
        if (!notion_id.isEmpty() && !notion_database_id.isEmpty()){
            // Comprobar que la database tiene los campos correctos



            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://api.notion.com/v1/databases/" + notion_database_id;
            final Boolean[] is_correct = {false};
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Respuesta", response);
                            // Display the first 500 characters of the response string.
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONObject properties = obj.getJSONObject("properties");
                                if (properties.getJSONObject("Image").getString("type").equals("files") &&
                                        properties.getJSONObject("Description").getString("type").equals("rich_text") &&
                                        properties.getJSONObject("Link").getString("type").equals("url") &&
                                        properties.getJSONObject("Name").getString("type").equals("title")
                                ){

                                    Log.e("CCHECJ", "CHEQUEAO");
                                    getDataForLayout(getApplicationContext());
                                }else{

                                    Log.e("CCHECJ", "NOOOOOOOOO");

                                    //TODO:
                                    // Avisar de que la database esta mal
                                    // Preguntar si quiere que la app la edite para que funcione correctamente
                                    // Avisar de que se van a borrar todos los datos de la database

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                sol_res = false;

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERR", error.toString());
                    Toast.makeText(ListActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
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
            ;

            // Add the request to the RequestQueue.
            queue.add(stringRequest);




        } else {
            Toast.makeText(this, "Las claves no estan bien seteadas", Toast.LENGTH_SHORT).show();
        }
    }

    // Load data from shared preferences
    public String loadData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void check_notiondb_properties(String notion_id, String database_id){

        // Hacer un get
        // Instantiate the RequestQueue.


        // Comprobar que tiene los campos necesarios
    }


    private void getDataForLayout(Context context){

        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String notion_id = loadData(NOTION_ID);
        String notion_database_id = loadData(NOTION_DATABASE_ID);
        String url = "https://api.notion.com/v1/databases/"+notion_database_id + "/query";

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
                                    Log.e("JSON", listDetail.toString() );

                                    String title = listDetail.getJSONObject("properties").getJSONObject("Name").getJSONArray("title").getJSONObject(0).getJSONObject("text").getString("content");
                                    String desc = listDetail.getJSONObject("properties").getJSONObject("Description").getJSONArray("rich_text").getJSONObject(0).getJSONObject("text").getString("content");
                                    String img_url = listDetail.getJSONObject("properties").getJSONObject("Image").getJSONArray("files").getJSONObject(0).getJSONObject("external").getString("url");
                                    String url = listDetail.getJSONObject("properties").getJSONObject("Link").getString("url");
                                    enlacesList.add(new Enlaces(title, desc, img_url, url));

                                } catch (Exception e){
                                    Log.e("errrrooor", e.toString());
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
        queue.add(sr);

    }

}