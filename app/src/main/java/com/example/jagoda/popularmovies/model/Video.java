package com.example.jagoda.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/*
 * Model class for fetching data from JSON using Gson library. It uses @SerializedName annotation
 * for the field's name when it's different from property in JSON.
 */
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
