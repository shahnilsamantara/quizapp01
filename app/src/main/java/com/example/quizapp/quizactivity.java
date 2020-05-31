package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class quizactivity extends AppCompatActivity {

    public static final String extra_score = "extrascore";

    private static final long count_down = 11000;

    private TextView textViewquestion;
    private TextView textViewscore;
    private TextView textViewquestioncount;
    private TextView textViewcountdown;
    private RadioGroup rbgroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button confirmnext;

    private ColorStateList textcolordefaultrb;
    private ColorStateList textcolordefaulttimer;

    private CountDownTimer countDownTimer;
    private  long timeleft;


    private List<question> questionList;
    private int questioncounter;
    private int questiontoal;
    private question currentquestion;

    private int score;
    private boolean answered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizactivity);

        textViewquestion = findViewById(R.id.question);
        textViewscore = findViewById(R.id.score);
        textViewcountdown = findViewById(R.id.timer);
        textViewquestioncount = findViewById(R.id.questioncount);
        rbgroup = findViewById(R.id.radiogroup);
        rb1 = findViewById(R.id.option1);
        rb2 = findViewById(R.id.option2);
        rb3 = findViewById(R.id.option3);
        confirmnext = findViewById(R.id.confirm);

        textcolordefaultrb = rb1.getTextColors();
        textcolordefaulttimer = textViewcountdown.getTextColors();


        quizdbhelper dbhelper = new quizdbhelper(this);
        questionList = dbhelper.getallquestions();
        questiontoal = questionList.size();
        Collections.shuffle(questionList);

        shownextquestion();
        confirmnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkanswer();
                    } else {
                        Toast.makeText(quizactivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    shownextquestion();
                }
            }
        });

    }

    private void shownextquestion() {
        rb1.setTextColor(textcolordefaultrb);
        rb2.setTextColor(textcolordefaultrb);
        rb3.setTextColor(textcolordefaultrb);
        rbgroup.clearCheck();

        if (questioncounter < questiontoal) {
            currentquestion = questionList.get(questioncounter);

            textViewquestion.setText(currentquestion.getQuestion());
            rb1.setText(currentquestion.getOption1());
            rb2.setText(currentquestion.getOption2());
            rb3.setText(currentquestion.getOption3());
            questioncounter++;
            textViewquestioncount.setText("Question:" + questioncounter + "/" + questiontoal);
            answered = false;
            confirmnext.setText("Confirm");

            timeleft = count_down;
            startcountdown();


        } else finishquiz();


    }

    private void startcountdown(){
        countDownTimer = new CountDownTimer(timeleft, 1000) {
            @Override
            public void onTick(long l) {
                timeleft = l;
                updatecoundowntext();

            }

            @Override
            public void onFinish() {
                timeleft = 0;
                updatecoundowntext();
                checkanswer();

            }
        }.start();
    }

    private void updatecoundowntext(){
        int seconds = (int) (timeleft/1000)%60;

        String timeFormated = String.format("%02d", seconds);
        textViewcountdown.setText(timeFormated);
        if(timeleft < 1000){
            textViewcountdown.setTextColor(Color.RED);
        }else textViewcountdown.setTextColor(textcolordefaulttimer);
    }

    private void checkanswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbselected = findViewById(rbgroup.getCheckedRadioButtonId());
        int answernumber = rbgroup.indexOfChild(rbselected) + 1;

        if (answernumber == currentquestion.getAnswer()) {
            score++;
            textViewscore.setText("Score:" + score);


        }

        showsolution();
    }

    private void showsolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentquestion.getAnswer()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewquestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewquestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewquestion.setText("Answer 3 is correct");
                break;

        }

        if (questioncounter < questiontoal) {
            confirmnext.setText("Next");
        } else {
            confirmnext.setText("finish");
        }



    }

    private void finishquiz() {

        Intent resultintent = new Intent();
        resultintent.putExtra(extra_score,score);
        setResult(RESULT_OK,resultintent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null ) {
            countDownTimer.cancel();
        }
    }
}





