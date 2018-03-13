package com.example.jagoda.popularmovies.model.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesDbContract {

    // The "Content authority" is a name for the entire content provider.
    // It's the package name for the app, which is guaranteed to be unique on the Play Store.
    public static final String CONTENT_AUTHORITY = "com.example.jagoda.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE_MOVIES = "favourite_movies";

    public static final class FavouriteMoviesEntry implements BaseColumns {

        // Uri for Favourite Movies Table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITE_MOVIES).build();

        public static final String TABLE_NAME = "favourite_movies";

        public static final String COLUMN_TITLE = "title";

        // movie id in theMovieDb.org database
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
}
