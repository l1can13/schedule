package com.schedule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class Files extends AppCompatActivity {

    /* Other */
    private List<CustomFiles> filesList = new LinkedList<>();
    private ImageButton load;

    /* Permission */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /* Shared Preferences */
    private final String key = "keyFiles";
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;

    /* RecyclerView */
    private RecyclerView recyclerView;
    private RecyclerViewFiles recyclerViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent chooser = Intent.createChooser(intent, "Select a File to Upload");

        try {
            startActivityForResult(chooser, 0);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openFile(CustomFiles file) {
        String mime = file.getType();
        Uri uriReal = Uri.parse(file.getUri());
        verifyStoragePermissions(this);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uriReal);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uriReal, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Не найдено приложений для открытия этого файла", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String getFilename(Uri uri) {
        return new File(uri.getPath()).getName();
    }

    public void saveList(List<CustomFiles> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        ed.putString(key, json);
        ed.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<CustomFiles> loadList() {
        List<CustomFiles> arrayItems = new LinkedList<>();
        String serializedObject = sPref.getString(key, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomFiles>>() {}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    private void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public boolean isContains(List<CustomFiles> list, String filename) {
        for (CustomFiles item : list) {
            if (item.getName().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"Range", "NotifyDataSetChanged"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        verifyStoragePermissions(this);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Uri uri = result.getData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            String filename = getFilename(uri);
            CustomFiles file = new CustomFiles(filename, uri.toString());
            if (!isContains(filesList, filename)) {
                filesList.add(file);
                saveList(filesList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Files.this)
                        .setTitle("Данный файл уже сохранён!")
                        .setNeutralButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                dialog.show();
            }
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        saveList(uriList);
//    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        ActivityCompat.requestPermissions(Files.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        verifyStoragePermissions(this);

        /* SharedPreferences setter */
        sPref = getSharedPreferences(key, Context.MODE_PRIVATE);
        ed = sPref.edit();

        load = findViewById(R.id.addButton);

        filesList = loadList();

        /* RecyclerView setter */
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewFiles(filesList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setSelectedItemId(R.id.files);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calendar:
                        saveList(filesList);

                        startActivity(new Intent(getApplicationContext(), Main.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.task:
                        saveList(filesList);

                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.files:
                        return true;
                    case R.id.settingsButton:
                        saveList(filesList);

                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}