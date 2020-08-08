package com.example.android.arithmeticsquiz;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.arithmeticsquiz.data.QuizContract;

public class RecordAdapter extends CursorAdapter {
    public RecordAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // find the individual views to modify
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView userScore = (TextView) view.findViewById(R.id.userScore);
        TextView userTime = (TextView) view.findViewById(R.id.userTime);

        // find the columns of the database
        int nameColumnIndex = cursor.getColumnIndex(QuizContract.PlayerEntry.COLUMN_NAME);
        int scoreColumnIndex = cursor.getColumnIndex(QuizContract.PlayerResults.COLUMN_POINTS);
        int timeColumnIndex = cursor.getColumnIndex(QuizContract.PlayerResults.COLUMN_TIME); // in millis
        int timeMillis = cursor.getInt(timeColumnIndex);

        String timeStr = String.format("%02d:%02d.%02d", (timeMillis/60000) % 60000, (timeMillis/1000) % 1000,
                (timeMillis/100) % 100);

        // read the values from the cursor and write them into the TextViews
        userName.setText(cursor.getString(nameColumnIndex));
        userScore.setText("" + cursor.getInt(scoreColumnIndex));
        userTime.setText(timeStr);
    }
}
