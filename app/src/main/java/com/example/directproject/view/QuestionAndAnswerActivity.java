package com.example.directproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.directproject.R;

import butterknife.ButterKnife;

public class QuestionAndAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_and_answer);

        ButterKnife.bind(this);
    }
}
