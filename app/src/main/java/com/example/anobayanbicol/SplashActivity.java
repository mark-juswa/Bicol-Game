package com.example.anobayanbicol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int SPLASH_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        ImageView logo = findViewById(R.id.imageView2);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.animation);
        logo.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            if (mAuth.getCurrentUser() != null) {
                DBQuery.loadData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(SplashActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_DELAY);
    }
}
