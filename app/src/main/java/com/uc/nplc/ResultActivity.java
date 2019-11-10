package com.uc.nplc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.uc.nplc.model.Quiz;

import org.jetbrains.annotations.NotNull;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_QUIZ = "extra_quiz";
    private AlphaAnimation click = new AlphaAnimation(1F, 0.6F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView tvTitle = findViewById(R.id.result_title);
        Button btnScan = findViewById(R.id.btn_scan_photo);
        ActionBar toolbar = getSupportActionBar();

        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(getString(R.string.quiz_result));
        }

        if (getIntent().getParcelableExtra(EXTRA_QUIZ) != null) {
            Quiz quiz = getIntent().getParcelableExtra(EXTRA_QUIZ);
            tvTitle.setText(quiz.getTitle());
        }

        btnScan.setOnClickListener(scanListener);
    }

    private View.OnClickListener scanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.startAnimation(click);
            Intent i = new Intent(ResultActivity.this, PlayScanner.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
