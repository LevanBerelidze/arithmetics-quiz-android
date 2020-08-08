package com.example.android.arithmeticsquiz;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    ArrayList<RandomQuestion> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle args = getIntent().getBundleExtra("bundle");
        answers = (ArrayList<RandomQuestion>) args.getSerializable("answers");

        ListView resultsView = (ListView) findViewById(R.id.resultsBoard);
        QuestionAdapter adapter = new QuestionAdapter(this, answers);
        resultsView.setAdapter(adapter);
    }


}
