package com.example.anobayanbicol;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnswersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView answersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_answers);


        toolbar = findViewById(R.id.aa_toolBar);
        answersView = findViewById(R.id.aa_recycler_view);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Answers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(layoutManager);

        AnswersAdapter adapter = new AnswersAdapter(DBQuery.g_questList);
        answersView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            AnswersActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}