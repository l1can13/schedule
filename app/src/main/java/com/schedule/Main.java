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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class Main extends AppCompatActivity {

    private ImageButton addButton;
    private LinearLayout horizontalScrollView;
    private Button[] buttonWeeks;
    private TextView currentWeek;
    private RecyclerView recyclerView;
    private RecyclerViewMain recyclerViewAdapter;

    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;
    private String PREF = "myprefs";
    private String LIST_OF_WEEKS = "listOfWeeks";
    private String AMOUNT_OF_WEEKS = "amountOfWeeks";
    private String VARIANTS = "variants";

    private List<Week> weeks = new LinkedList<>();
    private int mem;
    private int amountOfWeeks;
    private int variantsCount;

    private void createList() {
        int size = amountOfWeeks;
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

                    recyclerViewAdapter = new RecyclerViewMain(weeks.get(finalI % variantsCount), Main.this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
            });
            horizontalScrollView.addView(buttonWeeks[i]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Week> loadWeeks() {
        List<Week> arrayItems = new LinkedList<>();
        String serializedObject = sPref.getString(LIST_OF_WEEKS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Week>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    public void saveWeeks(List<Week> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        ed.putString(LIST_OF_WEEKS, json);
        ed.commit();
    }

    public Week getCurrentWeek() {
        return weeks.get(mem % variantsCount);
    }

    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        horizontalScrollView = findViewById(R.id.switcher);
        currentWeek = findViewById(R.id.week);
        recyclerView = findViewById(R.id.recyclerViewMain);

        sPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        ed = sPref.edit();

        String buf = sPref.getString(AMOUNT_OF_WEEKS, "");
        if (!buf.equals(""))
            amountOfWeeks = Integer.parseInt(buf);

        buf = sPref.getString(VARIANTS, "");
        if (!buf.equals(""))
            variantsCount = Integer.parseInt(buf);

        weeks = loadWeeks();
        if (weeks.size() == 0) {
            for (int i = 0; i < variantsCount; ++i) {
                weeks.add(new Week());
            }
            saveWeeks(weeks);
        }
        System.out.println(weeks.toString());
        System.out.println("SIZE = " + weeks.size());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                        return true;
                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.files:
                        startActivity(new Intent(getApplicationContext(), Files.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settingsButton:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}