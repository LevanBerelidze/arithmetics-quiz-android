package com.example.android.arithmeticsquiz;

import android.util.Log;

import com.example.android.arithmeticsquiz.data.QuizContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class RandomQuestion implements Serializable {


    private final int NUM_POSS_ANSWERS = 4;
    private final int RANGE_ANSW = 20;
    private final char[] possibleOperations = {'+', '-', '·', ':'};
    private final char[] operations;
    private Random rgen;

    private String question;
    private int[] answers;
    private int correctAnswer;
    private int userAnswer;
    private boolean isCorrect;

    private int firstOperand;
    private int secondOperand;

    public RandomQuestion(char[] operations) {
        this.operations = operations;
        rgen = new Random();
        question = generateQuestion();
        answers = generateAnswers();
    }

    public String getQuestion() {
        return question;
    }

    public int[] getAnswers() {
        return this.answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }

    public void setCorrect(boolean b) {
        this.isCorrect = b;
    }

    private String generateQuestion() {

        firstOperand = rgen.nextInt(10);
        secondOperand = rgen.nextInt(10);

        char operation = operations[rgen.nextInt(operations.length)];

        switch(operation) {
            case '+':
                correctAnswer = firstOperand + secondOperand;
                break;
            case '-':
                while(secondOperand > firstOperand)  {
                    // only positive answers
                    firstOperand = rgen.nextInt(10);
                    secondOperand = rgen.nextInt(10);
                }
                correctAnswer = firstOperand - secondOperand;
                break;

            case '·':
                correctAnswer = firstOperand * secondOperand;
                break;

            case ':':
                while(secondOperand == 0 || firstOperand%secondOperand != 0) {
                    // only integer answers
                    firstOperand = rgen.nextInt(10);
                    secondOperand = rgen.nextInt(10);
                }
                correctAnswer = firstOperand / secondOperand;
                break;
        }

        return "" + firstOperand + " " + operation + " " + secondOperand + " = ?";
    }

    private int[] generateAnswers() {

        int[] result = new int[NUM_POSS_ANSWERS];

        do {
            for (int i = 0; i < result.length; i++) {
                int ans;
                do {
                   ans = correctAnswer - RANGE_ANSW/2 + rgen.nextInt(RANGE_ANSW);
                } while(ans < 0);
                result[i] = ans;
            }
        } while (!isUnique(result));

        for (int i = 0; i < result.length; i++) {
            if (result[i] == correctAnswer) {
                return result;
            }
        }

        // If this line is reached, then we don't have correct answer in the list.
        result[rgen.nextInt(NUM_POSS_ANSWERS)] = correctAnswer;
        return result;
    }

    public static int getOperationKey(char[] operations) {
        int key = 0;
        for(int i = 0; i < operations.length; i++) {
            switch(operations[i]) {
                case '+':
                    key += QuizContract.PLUS;
                    break;
                case '-':
                    key += QuizContract.MINUS;
                    break;
                case '·':
                    key += QuizContract.MULTIPLY;
                    break;
                case ':':
                    key += QuizContract.DIVIDE;
                    break;
            }
        }
        return key;
    }

    private static boolean isUnique(int[] arr) {
        ArrayList<Integer> elements = new ArrayList<>();
        for(int i = 0; i < arr.length; i++) {
            if(elements.contains(arr[i])) return false;
            elements.add(arr[i]);
        }
        return true;
    }

}
