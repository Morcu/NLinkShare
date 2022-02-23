package com.example.linkshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.linkshare.Models.Enlaces;
import com.example.linkshare.adapters.SliderAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OnboardActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mdots;
    private EditText editIdNotion;
    private EditText editIdNotionDatabase;

    private Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentPage;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static String NOTION_ID = "NOTION_ID";
    public static String NOTION_DATABASE_ID = "NOTION_DATABASE_ID";

    //SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    //SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_onboard);

        // Front variables
        mSlideViewPager = (ViewPager) findViewById(R.id.slideviewpager);
        mDotLayout = (LinearLayout) findViewById(R.id.linearLayout);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        editIdNotion = (EditText) findViewById(R.id.editIdNotion);
        editIdNotionDatabase = (EditText) findViewById(R.id.editIdNotionDatabase);

        mNextBtn = (Button) findViewById(R.id.next);
        mBackBtn = (Button) findViewById(R.id.previous);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        

        // Next button listener
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CurrentPage", ""+mCurrentPage);
                switch(mCurrentPage) {
                    // case 1 & 2 save data ids
                    case 1:
                        Toast.makeText(OnboardActivity.this, editIdNotion.getText().toString(), Toast.LENGTH_SHORT).show();
                        saveData(NOTION_ID, editIdNotion.getText().toString());
                        break;
                    case 2:
                        saveData(NOTION_DATABASE_ID, editIdNotionDatabase.getText().toString());
                        break;
                    // Case 3 change intent
                    case 3:
                        Intent myIntent = new Intent(OnboardActivity.this, ListActivity.class);
                        startActivity(myIntent);
                        break;
                }
                // Slider for pages
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
        // Back button listener
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    // Saves data to Shared preferences
    public void saveData(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    // Load data from shared preferences
    public String loadData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
    

    // Onboarding dots front manager
    public void addDotsIndicator(int position) {
        mdots = new TextView[4];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mdots.length; i++) {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(25);
            mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mdots[i]);
        }
        if (mdots.length > 0) {
            mdots[position].setTextColor(getResources().getColor(R.color.navyblue));
        }
    }

    // Page change listener
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }


        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;

            // Fist page only info
            if (i == 0) {

                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Start");

                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mBackBtn.setText("");
                editIdNotionDatabase.setVisibility(View.INVISIBLE);
                editIdNotion.setVisibility(View.INVISIBLE);

            // Second page has to write notion ID in order to follow the process
            } else if (i == mdots.length - 3) {


                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mBackBtn.setText("Previous");

                editIdNotionDatabase.setVisibility(View.INVISIBLE);
                editIdNotion.setVisibility(View.VISIBLE);

                if (editIdNotion.getText().toString().isEmpty()) {
                    mNextBtn.setVisibility(View.INVISIBLE);
                }

                editIdNotion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mNextBtn.setVisibility(s.toString().trim().length() > 0 ? View.VISIBLE : View.INVISIBLE);
                        mNextBtn.setEnabled(true);
                        mNextBtn.setText("Save and continue");
                    }
                });

            // Third page has to write notion database ID in order to follow the process
            }  else if (i == mdots.length - 2) {
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mBackBtn.setText("Previous");

                editIdNotionDatabase.setVisibility(View.VISIBLE);
                editIdNotion.setVisibility(View.INVISIBLE);

                if (editIdNotionDatabase.getText().toString().isEmpty()) {
                    mNextBtn.setVisibility(View.INVISIBLE);
                }

                editIdNotionDatabase.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mNextBtn.setVisibility(s.toString().trim().length() > 0 ? View.VISIBLE : View.INVISIBLE);
                        mNextBtn.setEnabled(true);
                        mNextBtn.setText("Save and continue");


                    }
                });

            // Fourth page checks if the ids are correct and let you change to the list intent
            } else if (i == mdots.length - 1) {

                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mBackBtn.setText("Previous");
                editIdNotionDatabase.setVisibility(View.INVISIBLE);
                editIdNotion.setVisibility(View.INVISIBLE);

                String notion_id = loadData(NOTION_ID);
                String notion_database_id = loadData(NOTION_DATABASE_ID);
                Log.e("SSSS", notion_id + " "+ notion_database_id);

                // Handler for null safety ids
                if (!notion_id.isEmpty() && !notion_database_id.isEmpty()){
                    Log.e("SSSS", "True");

                    RequestQueue queue = Volley.newRequestQueue(OnboardActivity.this);

                    String url = "https://api.notion.com/v1/databases/" + notion_database_id;

                    StringRequest sr = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try{
                                        JSONObject obj = new JSONObject(response);

                                        if(obj.getString("object").equals("database")){
                                            mNextBtn.setEnabled(true);
                                            mNextBtn.setText("Let-s go");
                                            mNextBtn.setVisibility(View.VISIBLE);
                                        }else{
                                            Toast.makeText(OnboardActivity.this, "No HA PASASDO NAH", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e){
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
                            return params;
                        }

                    };
                    // Add the request to the RequestQueue.
                    queue.add(sr);
                    
                    
                    
                } else if (notion_id.isEmpty()){
                    Toast.makeText(OnboardActivity.this, "Debes rellenar el notion_id", Toast.LENGTH_SHORT).show();
                    mNextBtn.setVisibility(View.INVISIBLE);
                } else if (notion_database_id.isEmpty()){
                    Toast.makeText(OnboardActivity.this, "Debes rellenar el database_id", Toast.LENGTH_SHORT).show();
                    mNextBtn.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(OnboardActivity.this, "Debes rellenar el notion_id y el database_id", Toast.LENGTH_SHORT).show();
                    mNextBtn.setVisibility(View.INVISIBLE);
                }

                //Si tienes el texto en los shared object genial
                // Sino comprobar si estan en los edit text
                // si estan, hacer una llamada para ver si se pueden obtener datos
                // Si se puede poner el boton de finalizar
                // Sino mensaje del tipo falta X


                //sino toast que chequees el que este mal
            }
        }

        @Override
        public void onPageScrollStateChanged(int sitate) {

        }
    };
}