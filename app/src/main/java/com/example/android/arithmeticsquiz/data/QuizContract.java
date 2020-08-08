package com.example.android.arithmeticsquiz.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class QuizContract {

    // empty constructor
    public QuizContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.arithmeticsquiz";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ENTRIES = "entries";
    public static final String PATH_RESULTS = "results";

    public static abstract class PlayerEntry implements BaseColumns {

        public static final Uri ENTRY_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ENTRIES);
        public static final String TABLE_NAME = "player_entries";
        public static final String COLUMN_NAME = "name";

    }

    public static abstract class PlayerResults implements BaseColumns {

        public static final Uri RESULTS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RESULTS);
        public static final String TABLE_NAME = "player_results";
        public static final String COLUMN_USER_ID = "uid";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_OPERATIONS = "operations";
        public static final String COLUMN_TIME = "time";

    }

    /**
     * Respective integer values for each operation
     */
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int MULTIPLY = 4;
    public static final int DIVIDE = 8;

}
