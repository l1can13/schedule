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
import java.util.LinkedList;
import java.util.List;

public class Add extends AppCompatActivity {

    private ImageButton back;
    private ImageButton done;
    private EditText variantEditText;
    private EditText dayOfWeek;
    private EditText begin;
    private EditText end;
    private EditText subjectName;
    private EditText building;
    private EditText audition;
    private EditText subjectType;
    private EditText professor;
    private EditText eMail;
    private EditText phoneNumber;

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor ed;
    private String PREF = "myprefs";
    private String LIST_OF_WEEKS = "listOfWeeks";

    private List<Week> weeks;
    private Week currentWeek;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Week> loadWeeks() {
        List<Week> arrayItems = new LinkedList<>();
        String serializedObject = sPref.getString(LIST_OF_WEEKS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Week>>() {}.getType();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        back = findViewById(R.id.backArrow);
        done = findViewById(R.id.doneButton);

        variantEditText = findViewById(R.id.variantEditText);
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

        sPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        ed = sPref.edit();

        weeks = loadWeeks();

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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject subject = new Subject();

                subject.setStartTime(begin.getText().toString());
                subject.setEndTime(end.getText().toString());
                subject.setName(subjectName.getText().toString());
                subject.setBuilding(building.getText().toString());
                subject.setAudition(audition.getText().toString());
                subject.setType(subjectType.getText().toString());

                Lecturer lecturer = new Lecturer(professor.getText().toString(), eMail.getText().toString(), phoneNumber.getText().toString());
                subject.setLecturer(lecturer);

                currentWeek = weeks.get(Integer.parseInt(variantEditText.getText().toString()) - 1);
                if (!currentWeek.isExist(dayOfWeek.getText().toString())) {
                    Day day = new Day();
                    day.setName(dayOfWeek.getText().toString());
                    day.getSubjects().add(subject);
                    currentWeek.getDays().add(day);
                }
                else {
                    Day day = currentWeek.findDay(dayOfWeek.getText().toString());
                    day.getSubjects().add(subject);
                }

                saveWeeks(weeks);
                startActivity(new Intent(getApplicationContext(), Main.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}