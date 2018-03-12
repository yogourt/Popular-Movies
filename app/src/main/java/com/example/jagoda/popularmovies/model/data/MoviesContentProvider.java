package com.example.jagoda.popularmovies.model.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class MoviesContentProvider extends ContentProvider {

    // code used in UriMatcher for Favourite Movies Table Uri
    private static final int CODE_FAVOURITE_MOVIES_TABLE = 100;

    private MoviesDbHelper dbHelper;
    private static final UriMatcher sMatcher = buildUriMatcher();


    /*
     * Method to build uriMatcher with only one match for Favourite Movies Table
     */
    private static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVOURITE_MOVIES,
                CODE_FAVOURITE_MOVIES_TABLE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int code = sMatcher.match(uri);
        Cursor cursor;

        switch (code) {
            case CODE_FAVOURITE_MOVIES_TABLE:
                cursor = dbHelper.getReadableDatabase().query(
                        MoviesContract.FavouriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    // method not implemented in this app, as it doesn't use MIME types
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int code = sMatcher.match(uri);
        Uri returnUri;

        switch (code) {
            case CODE_FAVOURITE_MOVIES_TABLE:
                int _id = (int) dbHelper.getWritableDatabase().insert(
                        MoviesContract.FavouriteMoviesEntry.TABLE_NAME,
                        null,
                        contentValues);

                if(_id != -1) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.FavouriteMoviesEntry.CONTENT_URI, _id);
                    break;
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int code = sMatcher.match(uri);
        int deletedItems;

        switch (code) {
            case CODE_FAVOURITE_MOVIES_TABLE:
                deletedItems = dbHelper.getWritableDatabase().delete(
                        MoviesContract.FavouriteMoviesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedItems;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        int code = sMatcher.match(uri);
        int updatedItems;

        switch (code) {
            case CODE_FAVOURITE_MOVIES_TABLE:
                updatedItems = dbHelper.getWritableDatabase().update(
                    MoviesContract.FavouriteMoviesEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedItems;
    }

}
