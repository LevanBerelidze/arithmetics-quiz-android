package com.example.android.arithmeticsquiz.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ResultsProvider extends ContentProvider {

    public static final String LOG_TAG = ResultsProvider.class.getSimpleName();

    private static final int ENTRIES_URI_CODE = 100;
    private static final int RESULTS_URI_CODE = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_ENTRIES, ENTRIES_URI_CODE);
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_RESULTS, RESULTS_URI_CODE);
    }

    private ResultsReaderDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ResultsReaderDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match) {

            case ENTRIES_URI_CODE:
                cursor = database.query(QuizContract.PlayerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case RESULTS_URI_CODE:
                cursor = database.query(QuizContract.PlayerResults.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //TODO
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch(match) {

            case ENTRIES_URI_CODE:
                long idEntries = database.insert(QuizContract.PlayerEntry.TABLE_NAME, null, values);
                if(idEntries == -1)
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                else
                    return ContentUris.withAppendedId(uri, idEntries);
                break;

            case RESULTS_URI_CODE:
                long idResults = database.insert(QuizContract.PlayerResults.TABLE_NAME, null, values);
                if(idResults == -1)
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                else
                    return ContentUris.withAppendedId(uri, idResults);
                break;

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //TODO
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //TODO
        return 0;
    }
}
