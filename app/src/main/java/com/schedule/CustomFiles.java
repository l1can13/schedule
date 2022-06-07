package com.schedule;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class CustomFiles {

    private String uri;
    private String filename;

    public CustomFiles(String filename, String uri) {
        this.filename = filename;
        this.uri = uri;
    }

    public String getName() {
        return this.filename;
    }

    public String getUri() {
        return this.uri;
    }

    public String getType() {
        StringBuilder typeOfFile = new StringBuilder("");
        for (int i = this.filename.length() - 1; i > 0; --i) {
            if (this.filename.charAt(i) == '.')
                break;
            typeOfFile.append(this.filename.charAt(i));
        }

        return new String(typeOfFile.reverse());
    }

    public void open(Context context) {
        String mime = getType();
        Uri uriReal = Uri.parse(getUri());
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uriReal);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uriReal, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Toast.makeText(this, "Не найдено приложений для открытия этого файла", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
