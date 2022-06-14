package com.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Main extends AppCompatActivity {

    private ImageButton addButton;
    private LinearLayout horizontalScrollView;

    public static final String PREF = "myprefs";
    public static final String COLOR_PREF = "colorPref";
    private final String key = "keyMain";
    private SharedPreferences sPref;
    private SharedPreferences sPrefMain;
    private Button[] buttonWeeks;
    private int mem;
    public static final String Variant = "variant";
    private int variantsCount;
    private TextView currentWeek;

    private void createList() {
        String getCount = sPref.getString(COLOR_PREF, "");
        //String getVariantsCount = sPref.getString(Variant, "");
        //variantsCount = Integer.parseInt(getVariantsCount);

        if (!getCount.equals("")) {
            int size = Integer.parseInt(getCount);
            buttonWeeks = new Button[size];

            int buttonWidth = getScreenWidth() / 11;
            for (int i = 0; i < buttonWeeks.length; ++i) {
                buttonWeeks[i] = new Button(this);
                buttonWeeks[i].setText(String.valueOf(i + 1));

                Typeface font = ResourcesCompat.getFont(this, R.font.skranji);
                buttonWeeks[i].setTypeface(font);
                buttonWeeks[i].setTextSize(18);

                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.MATCH_PARENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    buttonWeeks[i].setTextColor(getColor(R.color.white));
                }

                buttonWeeks[i].setBackgroundColor(Color.TRANSPARENT);
                buttonWeeks[i].setLayoutParams(buttonParams);

                int finalI = i;
                buttonWeeks[i].setOnClickListener(new View.OnClickListener() {
                    @SuppressLint({"NewApi", "SetTextI18n"})
                    @Override
                    public void onClick(View view) {
                        if (finalI != mem) {
                            buttonWeeks[mem].setTextColor(getColor(R.color.white));
                        }
                        buttonWeeks[finalI].setTextColor(getColor(R.color.blue));
                        currentWeek.setText(getText(R.string.week) + " " + (finalI + 1));
                        mem = finalI;
                    }
                });
                horizontalScrollView.addView(buttonWeeks[i]);
            }
        }
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width;
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        return width;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        horizontalScrollView = findViewById(R.id.switcher);
        currentWeek = findViewById(R.id.week);

        sPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sPrefMain = getSharedPreferences(key, Context.MODE_PRIVATE);

        createList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Add.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setSelectedItemId(R.id.calendar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calendar:
                        try {
                            Add.saveList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case R.id.task:
                        try {
                            Add.saveList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.files:
                        try {
                            Add.saveList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(getApplicationContext(), Files.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settingsButton:
                        try {
                            Add.saveList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}