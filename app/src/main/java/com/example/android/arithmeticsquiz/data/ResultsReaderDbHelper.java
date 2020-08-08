package com.example.android.arithmeticsquiz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.arithmeticsquiz.data.QuizContract.PlayerEntry;
import com.example.android.arithmeticsquiz.data.QuizContract.PlayerResults;

public class ResultsReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "ResultsReader.db";

    public ResultsReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_PLAYER_ENTRIES);
        db.execSQL(CREATE_TABLE_PLAYER_RESULTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2){
            db.execSQL("ALTER TABLE " + PlayerResults.TABLE_NAME + " ADD "  + PlayerResults.COLUMN_TIME);
        }
    }

    private static final String CREATE_TABLE_PLAYER_ENTRIES = "CREATE TABLE " +
            PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PlayerEntry.COLUMN_NAME + " TEXT)";

    private static final String CREATE_TABLE_PLAYER_RESULTS = "CREATE TABLE " +
            PlayerResults.TABLE_NAME + "(" + PlayerResults._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PlayerResults.COLUMN_USER_ID + " INTEGER," +
            PlayerResults.COLUMN_POINTS + " INTEGER," +
            PlayerResults.COLUMN_OPERATIONS + " INTEGER" + ")";

}
