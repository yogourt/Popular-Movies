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
        values.put(MoviesDbContract.FavouriteMoviesEntry.COLUMN_TITLE, title);
        values.put(MoviesDbContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID, movieId);
        context.getContentResolver().insert(MoviesDbContract.FavouriteMoviesEntry.CONTENT_URI, values);

    }

    public synchronized int deleteMovieFromDb(int movieId) {

        String selection = MoviesDbContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movieId)};

        return context.getContentResolver().delete(
                MoviesDbContract.FavouriteMoviesEntry.CONTENT_URI,
                selection,
                selectionArgs);
    }

    public synchronized boolean isPresentInDb(int movieId) {

        String selection = MoviesDbContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movieId)};

        Cursor cursor = context.getContentResolver().query(
                MoviesDbContract.FavouriteMoviesEntry.CONTENT_URI,
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

        String[] projection = {MoviesDbContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID};
        Cursor cursor = context.getContentResolver().query(
                MoviesDbContract.FavouriteMoviesEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else return cursor;

    }

}

