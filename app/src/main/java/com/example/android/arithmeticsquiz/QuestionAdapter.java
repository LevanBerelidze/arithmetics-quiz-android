package com.example.android.arithmeticsquiz;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<RandomQuestion> {

   private static final String LOG_TAG = QuestionAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param answers A List of AndroidFlavor objects to display in a list
     */
    public QuestionAdapter(Activity context, List<RandomQuestion> answers) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, answers);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        RandomQuestion currentQuestion = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.answer_item, parent, false);
        }

        TextView questionView = (TextView) convertView.findViewById(R.id.questionView);
        questionView.setText(currentQuestion.getQuestion());

        TextView correctAnswView = (TextView) convertView.findViewById(R.id.correctAnswerTextView);
        correctAnswView.setText("" + currentQuestion.getCorrectAnswer());

        ImageView iconView = (ImageView) convertView.findViewById(R.id.img);

        if (currentQuestion.isCorrect()) {
            iconView.setImageResource(R.drawable.img_correct);
        } else {
            iconView.setImageResource(R.drawable.img_wrong);
            TextView userAnswerText = (TextView) convertView.findViewById(R.id.userAnswerText);
            TextView userAnswerView = (TextView) convertView.findViewById(R.id.userAnswerTextView);
            userAnswerText.setVisibility(View.VISIBLE);
            userAnswerView.setVisibility(View.VISIBLE);
            userAnswerView.setText("" + currentQuestion.getUserAnswer());
        }

        return convertView;
    }
}
