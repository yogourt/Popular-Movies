package com.example.jagoda.popularmovies.model.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

public class DatabaseSingleton {

    private Context context;

    private static DatabaseSingleton instance;

    private DatabaseSingleton(Context context) {
        this.context = context;
    }

    public static synchronized DatabaseSingleton getInstance(Context context) {

        if(instance == null) {
            instance = new DatabaseSingleton(context.getApplicationContext());
        }

        return instance;
    }

    public synchronized void addMovieToDb(@NonNull String title, int movieId) {

        ContentValues values = new ContentValues();
        values.put(MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE, title);
        values.put(MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID, movieId);
        context.getContentResolver().insert(MoviesContract.FavouriteMoviesEntry.CONTENT_URI, values);

    }

    public synchronized int deleteMovieFromDb(int movieId) {

        String selection = MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movieId)};

        return context.getContentResolver().delete(
                MoviesContract.FavouriteMoviesEntry.CONTENT_URI,
                selection,
                selectionArgs);
    }

    public synchronized boolean isPresentInDb(int movieId) {

        String selection = MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movieId)};

        Cursor cursor = context.getContentResolver().query(
                MoviesContract.FavouriteMoviesEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        if(cursor == null || cursor.getCount() == 0) {
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    /*
     * fetchAllFavourites() is method called by Main Presenter
     */
    public Cursor fetchAllFavourites() {

        String[] projection = {MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID};
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.FavouriteMoviesEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else return cursor;

    }

}

