package com.schedule;

import android.net.Uri;

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

}
