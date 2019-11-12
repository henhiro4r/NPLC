package com.uc.nplc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.uc.nplc.model.Quiz;
import com.uc.nplc.preference.Pref;
import com.uc.nplc.viewmodel.QuizViewModel;

import org.jetbrains.annotations.NotNull;

public class QuizActivity extends AppCompatActivity{

    private ProgressDialog pd;
    private ActionBar toolbar;
    private Quiz quiz;
    public static final String EXTRA_QUIZ = "extra_quiz";
    private TextView tvTitle, tvQuestion, tvChance;
    private EditText edtAnswer;
    private AlphaAnimation click = new AlphaAnimation(1F, 0.6F);
    private Pref pref;
    private QuizViewModel vIewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        pd = new ProgressDialog(this);
        pref = new Pref(this);
        tvTitle = findViewById(R.id.txt_ans_title);
        tvQuestion = findViewById(R.id.txt_ans_question);
        tvChance = findViewById(R.id.txt_ans_chance);
        edtAnswer = findViewById(R.id.quiz_answer);
        Button btnSubmit = findViewById(R.id.btn_submit);
        vIewModel = ViewModelProviders.of(this).get(QuizViewModel.class);
        toolbar = getSupportActionBar();

        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getParcelableExtra(EXTRA_QUIZ) != null){
            quiz = getIntent().getParcelableExtra(EXTRA_QUIZ);
            if (quiz != null) {
                setAttribute(quiz);
            }
        }

        btnSubmit.setOnClickListener(ansListener);
    }

    private void setAttribute(final Quiz quiz) {
        toolbar.setTitle("Answer Quiz - " +quiz.getTitle());
        tvTitle.setText(quiz.getTitle());
        if  (quiz.getId().equals("3")){
            tvQuestion.setText(getString(R.string.dummy_text));
            tvQuestion.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(QuizActivity.this)
                            .setTitle("Real Question")
                            .setMessage(quiz.getQuestion())
                            .setCancelable(false)
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    onResume();
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
            });
        } else {
            tvQuestion.setText(quiz.getQuestion());
        }
        String chance = quiz.getChance() + "/3";
        tvChance.setText(chance);
    }

    private View.OnClickListener ansListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.startAnimation(click);
            final String answer = edtAnswer.getText().toString().trim();
            if (TextUtils.isEmpty(answer)){
                showMessage("Please fill the answer");
            } else {
                pd.setCancelable(false);
                pd.setTitle("Loading");
                pd.setMessage("Please wait, submitting your answer...");
                pd.show();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        submitAnswer(quiz, answer);
                    }
                };
                Handler cancel = new Handler();
                cancel.postDelayed(r, 1000);
            }
        }
    };

    private void submitAnswer(Quiz quiz, String answer) {
        vIewModel.answerQuiz(pref.getIdKey(), quiz.getId(), answer);
        vIewModel.getMessage().observe(this, checkMessage);
    }

    private Observer<String> checkMessage = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            switch (message){
                case "Oops, Wrong answer!" :
                    String ch = vIewModel.getChance().getValue() + "/3";
                    tvChance.setText(ch);
                    showMessage(message);
                    pd.dismiss();
                    break;
                case "Used all chance" :
                    intentBack();
                    showMessage(message);
                    pd.dismiss();
                    break;
                case "Congratulation!":
                    toResult();
                    pd.dismiss();
                    showMessage(message);
                    break;
                default:
                     showMessage(message);
                     pd.dismiss();
                     break;
            }
        }
    };

    private void toResult(){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_QUIZ, quiz);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentBack();
    }

    private void intentBack(){
        Intent intent =  new Intent(QuizActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_TO_LOAD, "Quiz");
        startActivity(intent);
        finish();
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
