package com.example.android.arithmeticsquiz;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.arithmeticsquiz.data.QuizContract;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Character> operList;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operList = new ArrayList<>();
        startButton = (Button) findViewById(R.id.start);
    }


    public void checkBoxClicked(View view) {
        boolean checked = ( (CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.plus:
                if(checked)
                    operList.add('+');
                else
                    operList.remove(operList.indexOf('+'));
                break;

            case R.id.minus:
                if(checked)
                    operList.add('-');
                else
                    operList.remove(operList.indexOf('-'));
                break;

            case R.id.multiply:
                if(checked)
                    operList.add('·');
                else
                    operList.remove(operList.indexOf('·'));
                break;

            case R.id.divide:
                if(checked)
                    operList.add(':');
                else
                    operList.remove(operList.indexOf(':'));
                break;

        }
    }

    public void start(View view) {

        EditText nameView = (EditText) findViewById(R.id.userName);
        String userName = nameView.getText().toString();

        if(operList.size() > 0 && userName.length() > 0) {

            final char[] selectedOpers = new char[operList.size()];

            for(int i = 0; i < operList.size(); i++) {
                selectedOpers[i] = operList.get(i);
            }

            long userId;

            final Cursor cursor = getContentResolver().query(QuizContract.PlayerEntry.ENTRY_URI,
                    null, QuizContract.PlayerEntry.COLUMN_NAME + " = ?",
                    new String[] {userName}, null);

            if(cursor.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(QuizContract.PlayerEntry.COLUMN_NAME, userName);
                Uri newUri = getContentResolver().insert(QuizContract.PlayerEntry.ENTRY_URI,
                        contentValues);
                userId = ContentUris.parseId(newUri);
            } else {
               cursor.moveToNext();
               userId = cursor.getLong(cursor.getColumnIndex(QuizContract.PlayerEntry._ID));
            }

            Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
            gameIntent.putExtra("operations", selectedOpers);
            gameIntent.putExtra("userId", userId);
            startActivity(gameIntent);
        } else {
            if(operList.size() == 0)
                Toast.makeText(this, "Choose an operation before starting", Toast.LENGTH_SHORT).show();
            else if(userName.length() == 0)
                Toast.makeText(this, "Enter your name before starting", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveToLeaderboard(View view) {
        Intent leaderboardIntent = new Intent(MainActivity.this, LeaderboardActivity.class);
        char[] operArr = new char[operList.size()];
        for(int i = 0; i < operList.size(); i++) {
            operArr[i] = operList.get(i);
        }
        leaderboardIntent.putExtra("operKey", RandomQuestion.getOperationKey(operArr));
        startActivity(leaderboardIntent);
    }
}
