package com.example.android.arithmeticsquiz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arithmeticsquiz.data.QuizContract;

import java.io.Serializable;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    final int NUM_POSS_ANSWERS = 4;
    final int NUM_LIVES = 3;

    char[] operations;
    long userId;
    int points;
    int timeElapsed;

    RandomQuestion q;
    TextView ptsView;
    TextView livesView;
    TextView questionView;
    TextView timerView;
    Button[] answButtons;
    Button correctButton; // Variable is needed to make the correct button green.
    Button userButton;
    Button nextButton;
    Button resultsButton;
    Button restartButton;
    Button leaderboardButton;
    final Handler handler = new Handler();
    Runnable runnable;

    boolean allowAnswer; // User can only attempt to answer once.
    int lives;

    ArrayList<RandomQuestion> listAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        operations = getIntent().getCharArrayExtra("operations");
        userId = getIntent().getLongExtra("userId", -1);

        questionView = (TextView) findViewById(R.id.question);
        answButtons = new Button[NUM_POSS_ANSWERS];
        answButtons[0] = (Button) findViewById(R.id.firstAnsw);
        answButtons[1] = (Button) findViewById(R.id.secondAnsw);
        answButtons[2] = (Button) findViewById(R.id.thirdAnsw);
        answButtons[3] = (Button) findViewById(R.id.fourthAnsw);
        correctButton = (Button) findViewById(R.id.fourthAnsw);
        resultsButton = (Button) findViewById(R.id.seeAnswers);
        leaderboardButton = (Button) findViewById(R.id.leaderboard);
        ptsView = (TextView) findViewById(R.id.points);
        livesView = (TextView) findViewById(R.id.lives);
        timerView = (TextView) findViewById(R.id.timer);
        livesView.setText("Lives: " + NUM_LIVES);
        allowAnswer = true;
        points = 0;
        timeElapsed = 0;
        lives = NUM_LIVES;

        listAnswers = new ArrayList<>();

        restartButton = (Button) findViewById(R.id.restart);
        restartButton.setVisibility(View.INVISIBLE);
        nextButton = (Button) findViewById(R.id.userChoice);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        runnable = new Runnable() {
            int sec;
            int minute;
            @Override
            public void run() {
                sec = timeElapsed/1000;
                if(sec >= 60) {
                    minute = sec / 60;
                    sec = sec % 60;
                }
                String timeStr = String.format("%02d:%02d.%d", minute, sec, (timeElapsed/100) % 10 );
                timerView.setText(timeStr);
                handler.postDelayed(this, 50);
                timeElapsed += 50;
            }
        };

        nextQuestion();

    }

    public void nextQuestion() {

        displayTime();

        if(lives <= 0) {
            return;
        }

        nextButton.setVisibility(View.INVISIBLE);

        allowAnswer = true;
        for(int i = 0; i < answButtons.length; i++) {
            answButtons[i].setBackgroundColor(Color.LTGRAY);
        }
        userButton = null;

        q = new RandomQuestion(operations);
        String newQuestion = q.getQuestion();
        int correctAnsw = q.getCorrectAnswer();

        questionView.setText(newQuestion);
        int[] answers = q.getAnswers();
        for(int i = 0; i < NUM_POSS_ANSWERS; i++) {
            answButtons[i].setText("" + answers[i]);
            if(answers[i] == correctAnsw) correctButton = answButtons[i];
        }

    }

    public void checkAnswer(View view) {

        if(!allowAnswer) return;

        handler.removeCallbacks(runnable);

        int buttonID = view.getId();
        userButton = (Button) findViewById(buttonID);

        int userAnswer = Integer.parseInt( (String) userButton.getText());
        boolean isCorrect;

        if(userAnswer == q.getCorrectAnswer()) {
            ptsView.setText("Points: " + (++points));
            isCorrect = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    nextQuestion();
                }
            }, 200);
        } else {
            livesView.setText("Lives: " + (--lives));
            isCorrect = false;
            userButton.setBackgroundColor(Color.RED);
            nextButton.setVisibility(View.VISIBLE);
        }

        q.setCorrect(isCorrect);
        correctButton.setBackgroundColor(Color.GREEN);

        listAnswers.add(q);
        Log.v("listAnswers", "added question");
        allowAnswer = false;
        // After the answer is chosen, user can't change it.

        if(lives <= 0) {
            livesView.setText("No lives remaining.");
            livesView.setTextColor(Color.RED);
            nextButton.setVisibility(View.INVISIBLE);
            resultsButton.setVisibility(View.VISIBLE);
            restartButton.setVisibility(View.VISIBLE);
            leaderboardButton.setVisibility(View.VISIBLE);

            ContentValues contentValues = new ContentValues();
            contentValues.put(QuizContract.PlayerResults.COLUMN_USER_ID, userId);
            contentValues.put(QuizContract.PlayerResults.COLUMN_OPERATIONS, RandomQuestion.getOperationKey(operations));
            contentValues.put(QuizContract.PlayerResults.COLUMN_POINTS, points);
            contentValues.put(QuizContract.PlayerResults.COLUMN_TIME, timeElapsed);
            getContentResolver().insert(QuizContract.PlayerResults.RESULTS_URI,
                    contentValues);
        }

    }

    public void moveToResults(View view) {
        Intent resultsIntent = new Intent(this, ResultsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("answers", (Serializable) listAnswers);
        resultsIntent.putExtra("bundle", args);
        startActivity(resultsIntent);
    }

    public void restartQuiz(View view) {
        Intent startIntent = new Intent(this, MainActivity.class);
        startActivity(startIntent);
    }

    public void displayDatabase(View view) {
        Intent leaderboardIntent = new Intent(this, LeaderboardActivity.class);
        leaderboardIntent.putExtra("userId", userId);
        leaderboardIntent.putExtra("operKey", RandomQuestion.getOperationKey(operations));
        startActivity(leaderboardIntent);
    }

    private void displayTime() {

        handler.post(runnable);
    }

}
