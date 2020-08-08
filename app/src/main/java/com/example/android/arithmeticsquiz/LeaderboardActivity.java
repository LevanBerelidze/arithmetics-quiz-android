package com.example.android.arithmeticsquiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.arithmeticsquiz.data.QuizContract;
import com.example.android.arithmeticsquiz.data.ResultsProvider;
import com.example.android.arithmeticsquiz.data.ResultsReaderDbHelper;

public class LeaderboardActivity extends AppCompatActivity {

    int operKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        int userId = getIntent().getIntExtra("userId", 0);
        operKey = getIntent().getIntExtra("operKey", 15);
        CheckBox[] operCheck = {(CheckBox) findViewById(R.id.plus), (CheckBox) findViewById(R.id.minus),
                (CheckBox) findViewById(R.id.multiply), (CheckBox) findViewById(R.id.divide)};

        // Mark checkboxes as selected
        for(int i = 0; i < 4; i++) {
            int currentOper = (int) Math.pow(2, i);
            if( (currentOper & operKey) == currentOper) {
                operCheck[i].setChecked(true);
            }
        }

        updateDisplay();

    }

    public void checkBoxClicked(View view) {

        boolean checked = ( (CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.plus:
                if(checked)
                    operKey |= QuizContract.PLUS;
                else
                    operKey ^= QuizContract.PLUS;
                break;

            case R.id.minus:
                if(checked)
                    operKey |= QuizContract.MINUS;
                else
                    operKey ^= QuizContract.MINUS;
                break;

            case R.id.multiply:
                if(checked)
                    operKey |= QuizContract.MULTIPLY;
                else
                    operKey ^= QuizContract.MULTIPLY;
                break;

            case R.id.divide:
                if(checked)
                    operKey |= QuizContract.DIVIDE;
                else
                    operKey ^= QuizContract.DIVIDE;
                break;

        }

        updateDisplay();

    }


    private void updateDisplay() {

        ResultsReaderDbHelper dbHelper = new ResultsReaderDbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String PlayersTable = QuizContract.PlayerEntry.TABLE_NAME;
        String ResultsTable = QuizContract.PlayerResults.TABLE_NAME;
        String PlayersId = QuizContract.PlayerEntry._ID;
        String UserId = QuizContract.PlayerResults.COLUMN_USER_ID;
        String OperationsColumn = QuizContract.PlayerResults.COLUMN_OPERATIONS;

        String joinCmd = ("SELECT * FROM " + PlayersTable + " JOIN " +
                ResultsTable + " ON " + PlayersTable + "." + PlayersId + " = " +
                ResultsTable + "." + UserId);

        String[] selectionArgs = {};

        if(operKey > 0) {
            // If user selects no operations, display all previous results
            joinCmd += " WHERE " + OperationsColumn + " = ?";
            selectionArgs = new String[]{"" + operKey};
        }

        joinCmd += " ORDER BY " + QuizContract.PlayerResults.COLUMN_POINTS + " DESC";

        Cursor cursor = database.rawQuery(joinCmd, selectionArgs);

        ListView leaderboardList = (ListView) findViewById(R.id.leaderboard_list);
        RecordAdapter adapter = new RecordAdapter(this, cursor);
        leaderboardList.setAdapter(adapter);

    }

}
