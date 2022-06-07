package com.schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    private EditText setNumberOfWeeks;
    private EditText setVariantsOfWeeks;
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;
    public static final String PREF = "myprefs";
    public static final String COLOR_PREF = "colorPref";
    public static final String Variant = "variant";

    private void saveText() {
        ed.putString(COLOR_PREF, setNumberOfWeeks.getText().toString());
        ed.putString(Variant, setVariantsOfWeeks.getText().toString());
        ed.apply();
    }

    private void loadText() {
        String savedText = sPref.getString(COLOR_PREF, "");
        String savedVariant = sPref.getString(Variant, "");
        setVariantsOfWeeks.setText(savedVariant);
        setNumberOfWeeks.setText(savedText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        ed = sPref.edit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setSelectedItemId(R.id.settingsButton);

        setNumberOfWeeks = findViewById(R.id.setNumberOfWeeks);
        setVariantsOfWeeks = findViewById(R.id.numberOfVariantsEditText);
        loadText();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), Main.class));
                        overridePendingTransition(0, 0);
                        saveText();
                        return true;
                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        saveText();
                        return true;
                    case R.id.files:
                        startActivity(new Intent(getApplicationContext(), Files.class));
                        overridePendingTransition(0, 0);
                        saveText();
                        return true;
                    case R.id.settingsButton:
                        saveText();
                        return true;
                }
                return false;
            }
        });
    }
}
