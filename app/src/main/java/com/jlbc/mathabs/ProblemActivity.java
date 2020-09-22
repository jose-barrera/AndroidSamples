package com.jlbc.mathabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProblemActivity extends AppCompatActivity
{
    private final Problem[] problems = new Problem[10];
    private final long[] times = new long[10];
    private Problem problem;
    private TextView viewProblem;
    private EditText editAnswer;
    private long start;
    private int i = 0;
    private int correct = 0;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 10; i++)
        {
            problems[i] = new Problem();
            Log.i("info", problems[i].toString());
        }
        setContentView(R.layout.activity_problem);

        viewProblem = findViewById(R.id.textProblem);
        editAnswer = findViewById(R.id.editAnswer);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                switch(actionID)
                {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                    case EditorInfo.IME_ACTION_PREVIOUS:
                        times[i] = System.currentTimeMillis() - start;
                        problem.setAnswer(Integer.parseInt(editAnswer.getText().toString()));
                        correct += problem.getResult() ? 1 : 0;
                        if (i < 9) {
                            i++;
                            displayProblem();
                            imm.showSoftInput(editAnswer, InputMethodManager.SHOW_FORCED);
                        }
                        else {
                            Toast.makeText(ProblemActivity.this, "Correct: " + correct, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        displayProblem();
        imm.showSoftInput(editAnswer, InputMethodManager.SHOW_FORCED);
    }

    private void displayProblem()
    {
        problem = problems[i];
        viewProblem.setText(String.format("%d %s %d", problem.getA(), problem.getOperator(), problem.getB()));
        editAnswer.setText("");
        editAnswer.requestFocus();
        start = System.currentTimeMillis();
    }
}