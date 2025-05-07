package com.example.anobayanbicol;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditTxt, emailEditTxt, passwordInput, confirmPass;
    Button btnContinueLoad;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEditTxt = findViewById(R.id.nameEditTxt);
        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPass = findViewById(R.id.confirmPass);
        btnContinueLoad = findViewById(R.id.btnContinueLoad);

        mAuth = FirebaseAuth.getInstance();

        btnContinueLoad.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String name = nameEditTxt.getText().toString().trim(); // Optional - unused here
        String email = emailEditTxt.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPass.getText().toString().trim();

        // Validations
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditTxt.setError("Invalid email format");
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPass.setError("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();

                        DBQuery.createUserData(email, name, new MyCompleteListener(){

                            @Override
                            public void onSuccess() {

                                DBQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(SignUpActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(SignUpActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
