package com.example.anobayanbicol;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestActivity extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle(DBQuery.g_catlist.get(DBQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testView = findViewById(R.id.test_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);



        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                DBQuery.loadMyScores(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        TestAdapter adapter = new TestAdapter(DBQuery.g_testList);
                        testView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(TestActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onFailure() {
                Toast.makeText(TestActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // handle back arrow click
        return true;
    }
}
