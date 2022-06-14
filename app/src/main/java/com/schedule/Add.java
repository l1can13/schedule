package com.schedule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;

public class Add extends AppCompatActivity {

    private ImageButton back;
    private static EditText variantOfWeek;
    private static EditText dayOfWeek;
    private static EditText begin;
    private static EditText end;
    private static EditText subjectName;
    private static EditText building;
    private static EditText audition;
    private static EditText subjectType;
    private static EditText professor;
    private static EditText eMail;
    private static EditText phoneNumber;
    private static HashMap<keys, String> infoDict = new HashMap<>();
    private final static String key = "keyMain";

    protected enum keys {variantOfWeekKey, dayOfWeekKey, beginKey, endKey, subjectNameKey, buildingKey, auditionKey, subjectTypeKey, professorKey, eMailKey, phoneNumberKey}

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor ed;

    private static void fillHashMap() {
        infoDict.put(keys.variantOfWeekKey, variantOfWeek.getText().toString());
        infoDict.put(keys.dayOfWeekKey, dayOfWeek.getText().toString());
        infoDict.put(keys.beginKey, begin.getText().toString());
        infoDict.put(keys.endKey, end.getText().toString());
        infoDict.put(keys.subjectNameKey, subjectName.getText().toString());
        infoDict.put(keys.buildingKey, building.getText().toString());
        infoDict.put(keys.auditionKey, audition.getText().toString());
        infoDict.put(keys.subjectTypeKey, subjectType.getText().toString());
        infoDict.put(keys.professorKey, professor.getText().toString());
        infoDict.put(keys.eMailKey, eMail.getText().toString());
        infoDict.put(keys.phoneNumberKey, phoneNumber.getText().toString());
    }

    private void setHashMap() {
        variantOfWeek.setText(infoDict.get(keys.variantOfWeekKey));
        dayOfWeek.setText(infoDict.get(keys.dayOfWeekKey));
        begin.setText(infoDict.get(keys.beginKey));
        end.setText(infoDict.get(keys.endKey));
        subjectName.setText(infoDict.get(keys.subjectNameKey));
        building.setText(infoDict.get(keys.buildingKey));
        audition.setText(infoDict.get(keys.auditionKey));
        subjectType.setText(infoDict.get(keys.subjectTypeKey));
        professor.setText(infoDict.get(keys.professorKey));
        eMail.setText(infoDict.get(keys.eMailKey));
        phoneNumber.setText(infoDict.get(keys.phoneNumberKey));
    }

    protected static void saveList() throws IOException {
        try {
            fillHashMap();
            Gson gson = new Gson();
            String json = gson.toJson(infoDict);
            ed.putString(key, json);
            ed.commit();
        } catch (Exception e) {
            System.out.println("ПУСТО");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected static HashMap<keys, String> loadHashMap() throws IOException, ClassNotFoundException {
        HashMap<keys, String> hashMap = new HashMap<>();
        try {
            String serializedObject = sPref.getString(key, null);
            if (serializedObject != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<keys, String>>() {
                }.getType();
                hashMap = gson.fromJson(serializedObject, type);
            }
        } catch (Exception e) {
            System.out.println("ПУСТО");
        }
        return hashMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        back = findViewById(R.id.backArrow);
        variantOfWeek = findViewById(R.id.variantEditText);
        dayOfWeek = findViewById(R.id.dayOfWeekEditText);
        begin = findViewById(R.id.timePickerBegin);
        end = findViewById(R.id.timePickerEnd);
        subjectName = findViewById(R.id.subjectNameEditText);
        building = findViewById(R.id.buildingEditText);
        audition = findViewById(R.id.auditionEditText);
        subjectType = findViewById(R.id.subjectTypeEditText);
        professor = findViewById(R.id.professorEditText);
        eMail = findViewById(R.id.emailEditText);
        phoneNumber = findViewById(R.id.phoneNumberEditText);

        sPref = getSharedPreferences(key, Context.MODE_PRIVATE);
        ed = sPref.edit();

        try {
            infoDict = loadHashMap();
            setHashMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        begin.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        end.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(), Main.class));
            }
        });
    }
}