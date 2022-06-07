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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Main extends AppCompatActivity {

    private ImageButton addButton;
    private LinearLayout linearLayout;
    private LinearLayout horizontalScrollView;

    public static final String PREF = "myprefs";
    public static final String COLOR_PREF = "colorPref";
    private final String key = "keyMain";
    public static final String Variant = "variant";
    private SharedPreferences sPref;
    private SharedPreferences sPrefMain;
    private HashMap<Add.keys, String> hashMap = new HashMap<>();
    private Button[] buttonWeeks;
    private int mem;
    private String variants;
    private int variantsInt;
    private TextView currentWeek;
    private static Week[] weeks;

    public static Week getWeek(int index) {
        return weeks[index];
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    private void createList() {
        String getCount = sPref.getString(COLOR_PREF, "");

        if (!getCount.equals("")) {
            buttonWeeks = new Button[Integer.parseInt(getCount)];

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

    public void showTimetable(Week week) {
        for (int i = 0; i < week.getDaysOfWeek().size(); ++i) {
            LinearLayout dayOfWeek = new LinearLayout(linearLayout.getContext());
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(28));
            dayOfWeek.setLayoutParams(linearParams);
            dayOfWeek.setGravity(Gravity.CENTER_VERTICAL);
            dayOfWeek.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));

            TextView textView = new TextView(dayOfWeek.getContext());
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.leftMargin = dpToPx(8);
            textView.setLayoutParams(textParams);
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            textView.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.tenor_sans));
            textView.setText(week.getDaysOfWeek().get(i).getNameDay());
            textView.setTextSize(18);
            dayOfWeek.addView(textView);

            for (int j = 0; j < week.getDaysOfWeek().get(i).getSubjects().size(); ++j) {
                LinearLayout subjectLayout = new LinearLayout(dayOfWeek.getContext());
                LinearLayout.LayoutParams subjectParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(60));
                subjectLayout.setLayoutParams(subjectParams);
                subjectLayout.setGravity(Gravity.CENTER_VERTICAL);
                subjectLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                TextView subjectName = new TextView(dayOfWeek.getContext());
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                nameParams.leftMargin = dpToPx(8);
                subjectName.setLayoutParams(nameParams);
                subjectName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                subjectName.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.tenor_sans));
                subjectName.setText(week.getDaysOfWeek().get(i).getSubjects().get(j).getNameOfSubject());
                subjectName.setTextSize(18);
                dayOfWeek.addView(subjectName);
            }

            linearLayout.addView(dayOfWeek);
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
        linearLayout = findViewById(R.id.contentContainer);
        horizontalScrollView = findViewById(R.id.switcher);
        currentWeek = findViewById(R.id.week);

        sPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sPrefMain = getSharedPreferences(key, Context.MODE_PRIVATE);

        try {
            String serializedObject = sPrefMain.getString(key, null);
            if (serializedObject != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<Add.keys, String>>() {
                }.getType();
                hashMap = gson.fromJson(serializedObject, type);
            }
        } catch (Exception e) {
            System.out.println("ПУСТО");
        }

        createList();

//        variants = sPref.getString(Variant, "");
//        variantsInt = Integer.parseInt(variants);
//        if (!variants.isEmpty()) {
//            weeks = new Week[variantsInt];
//            for (int i = 0; i < variantsInt; ++i) {
//                weeks[i] = new Week();
//                DayOfWeek day = new DayOfWeek(hashMap.get(Add.keys.dayOfWeekKey));
//                Subject subject = new Subject(hashMap.get(Add.keys.subjectNameKey));
//                day.setSubjects(subject);
//                weeks[i].updateDaysOfWeek(day);
//            }
//        }

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