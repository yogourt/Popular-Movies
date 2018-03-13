package com.example.jagoda.popularmovies.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * This is helper class that is used in Movies Content Provider to get access to
 * readable/writable database
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_FAVOURITE_MOVIES_TABLE =
                "CREATE TABLE " + MoviesDbContract.FavouriteMoviesEntry.TABLE_NAME + " (" +
                        MoviesDbContract.FavouriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesDbContract.FavouriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MoviesDbContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_FAVOURITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDbContract.FavouriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
