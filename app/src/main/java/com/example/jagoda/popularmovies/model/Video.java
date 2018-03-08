package com.example.jagoda.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("name")
    private String title;
    private String type;
    private String key;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
