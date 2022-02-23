package com.example.linkshare;

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

import com.example.linkshare.adapters.SliderAdapter;

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
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(mCurrentPage) {
                    case 1:
                        Toast.makeText(OnboardActivity.this, editIdNotion.getText().toString(), Toast.LENGTH_SHORT).show();
                        //editor.putString(NOTION_ID, editIdNotion.getText().toString());
                        //editor.apply();
                        saveData(NOTION_ID, editIdNotion.getText().toString());
                    case 2:
                        Toast.makeText(OnboardActivity.this, editIdNotionDatabase.getText().toString(), Toast.LENGTH_SHORT).show();
                        //editor.putString(NOTION_DATABASE_ID, editIdNotionDatabase.getText().toString());
                        //editor.apply();
                        saveData(NOTION_DATABASE_ID, editIdNotionDatabase.getText().toString());
                }
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }
    public void saveData(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
        Toast.makeText(this, "Data saved"+ " "+ key + " " + value, Toast.LENGTH_SHORT).show();
    }


    public String loadData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

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
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;


            if (i == 0) {

                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Start");

                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mBackBtn.setText("");
                editIdNotionDatabase.setVisibility(View.INVISIBLE);
                editIdNotion.setVisibility(View.INVISIBLE);

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

                // Guarda conexion

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


                // Guarda la tabla

            } else if (i == mdots.length - 1) {

                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mBackBtn.setText("Previous");
                editIdNotionDatabase.setVisibility(View.INVISIBLE);
                editIdNotion.setVisibility(View.INVISIBLE);

                String nid = loadData(NOTION_ID);
                String ndid = loadData(NOTION_DATABASE_ID);
                Log.e("SSSS", nid + " "+ ndid);
                if (!nid.isEmpty() && !ndid.isEmpty()){
                    Log.e("SSSS", "True");
                    mNextBtn.setEnabled(true);
                    mNextBtn.setText("Let-s go");
                    mNextBtn.setVisibility(View.VISIBLE);
                } else {
                    Log.e("SSSS", "False");
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