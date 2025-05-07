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

public class BookMarksActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView questionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_marks);

        toolbar = findViewById(R.id.ba_toolBar);
        questionsView = findViewById(R.id.ba_recycler_view);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Saved Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        questionsView.setLayoutManager(layoutManager);

        DBQuery.loadBookmarks(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                BookmarksAdapter adapter = new BookmarksAdapter(DBQuery.g_bookmarksList);
                questionsView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            BookMarksActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}