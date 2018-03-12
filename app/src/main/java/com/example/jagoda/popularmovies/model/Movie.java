package com.example.jagoda.popularmovies.model;

/*
 * Model class for fetching data from JSON using Gson library. It contains fields with required
 * movie details. It uses @SerializedName annotation for the field's name when it's different
 * from property in JSON.
 */

import com.google.gson.annotations.SerializedName;

public class Movie {

    private int id;
    private String title;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    private String overview;
    @SerializedName("vote_average")
    private double rating;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

}
