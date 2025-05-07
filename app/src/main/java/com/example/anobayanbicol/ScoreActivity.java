package com.example.anobayanbicol;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTV, timeTV, totalQTV, correctQTV, wrongQTV, unattemptedQTV;
    private LinearLayout leaderB;
    Button reAttemptB, viewAnsB;
    private long timeTaken;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init();
        loadData();
        setBookmarks();

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });

        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt();
            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                intent.putExtra("SHOW_FRAGMENT", "LEADERBOARD");
                startActivity(intent);


            }
        });

        saveResult();
    }

    private void init(){
        scoreTV = findViewById(R.id.ques_num);
        timeTV = findViewById(R.id.txtTime);
        totalQTV = findViewById(R.id.txtTotalQues);
        correctQTV = findViewById(R.id.correctQues);
        wrongQTV = findViewById(R.id.mistakes);
        unattemptedQTV = findViewById(R.id.unattemptedQues);
        leaderB = findViewById(R.id.leaderB);
        reAttemptB = findViewById(R.id.btnReattempt);
        viewAnsB = findViewById(R.id.btnViewAns);

    }

    private void loadData() {
        int correctQ = 0, wrongQ = 0, unattemptQ = 0;

        for (int i = 0; i < DBQuery.g_questList.size(); i++) {
            int selectedAns = DBQuery.g_questList.get(i).getSelectedAns();
            int correctAns = DBQuery.g_questList.get(i).getCorrectans();

            // Fix: Check for -1 (not 1) for unattempted questions
            if (selectedAns == -1) {
                unattemptQ++;
            } else {
                if (selectedAns == correctAns) {
                    correctQ++;
                } else {
                    wrongQ++;
                }
            }
        }

        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptQ));
        totalQTV.setText(String.valueOf(DBQuery.g_questList.size()));

        if (DBQuery.g_questList.size() > 0) {
            finalScore = (correctQ * 100) / DBQuery.g_questList.size();
        } else {
            finalScore = 0;
        }

        scoreTV.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));
        timeTV.setText(time);

    }

    private void reAttempt(){
        for (int i = 0; i < DBQuery.g_questList.size(); i++){
            DBQuery.g_questList.get(i).setSelectedAns(-1);
            DBQuery.g_questList.get(i).setStatus(DBQuery.NOT_VISITED);
        }
        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();

    }

    private void saveResult(){
        DBQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setBookmarks(){
        for (int i = 0; i<DBQuery.g_questList.size(); i++){

            QuestionModel question = DBQuery.g_questList.get(i);

            if (question.isBookmarked()){
                if ( ! DBQuery.g_bmIdList.contains(question.getqID())){
                    DBQuery.g_bmIdList.add(question.getqID());
                    DBQuery.myProfile.setBookmarkCount(DBQuery.g_bmIdList.size());
                }
            }else {
                if (DBQuery.g_bmIdList.contains(question.getqID())){
                    DBQuery.g_bmIdList.remove(question.getqID());
                    DBQuery.myProfile.setBookmarkCount(DBQuery.g_bmIdList.size());
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}