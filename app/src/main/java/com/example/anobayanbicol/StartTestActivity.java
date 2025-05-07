package com.example.anobayanbicol;

import static com.example.anobayanbicol.DBQuery.g_catlist;
import static com.example.anobayanbicol.DBQuery.g_selected_test_index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName, testNo, totalQ, bestScore, time;
    private Button btnStart;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIX: This was missing!
        setContentView(R.layout.activity_start_test); // <-- Make sure this is the correct layout file

        init(); // Now it's safe to call findViewById

        DBQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
            }

            @Override
            public void onFailure() {
                Toast.makeText(StartTestActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);
        btnStart = findViewById(R.id.btnStart);
        btnBack = findViewById(R.id.buttonBack3);

        btnBack.setOnClickListener(v -> StartTestActivity.this.finish());

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setData() {
        catName.setText(g_catlist.get(DBQuery.g_selected_cat_index).getName());
        testNo.setText("Test number: " + (g_selected_test_index + 1));
        totalQ.setText(String.valueOf(DBQuery.g_questList.size()));
        bestScore.setText(String.valueOf(DBQuery.g_testList.get(g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DBQuery.g_testList.get(g_selected_test_index).getTime()));
    }
}
