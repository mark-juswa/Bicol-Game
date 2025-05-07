package com.example.anobayanbicol;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView signUpTextView;
    LinearLayout btnGoogle;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 104;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        // Init UI
        emailEditText = findViewById(R.id.emailEditTxt);
        passwordEditText = findViewById(R.id.editTextTextEmailAddress); // use the correct ID from XML
        loginButton = findViewById(R.id.btnContinueLoad);
        signUpTextView = findViewById(R.id.signUp);
        btnGoogle = findViewById(R.id.btnGoogle);

        // Google Sign-In Options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // must match Firebase console
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginButton.setOnClickListener(v -> handleLogin());
        signUpTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        btnGoogle.setOnClickListener(v -> googleSignIn());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        DBQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(LoginActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                String errorMessage = "Google Sign-In Failed: Code " + e.getStatusCode() + " - " + e.getMessage();
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                e.printStackTrace(); // Add this for deeper debug
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Google Sign In Success", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();

                        // IF USER AY FIRST TIME MAG LOGIN SA GOOGLE
                        if(task.getResult().getAdditionalUserInfo().isNewUser()){
                            DBQuery.createUserData(user.getEmail(), user.getDisplayName(), new MyCompleteListener(){

                                @Override
                                public void onSuccess() {

                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }

                                        @Override
                                        public void onFailure() {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(LoginActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // ELSE HINDI FIST TIME
                        }else {
                            DBQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(LoginActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }



                    } else {
                        Toast.makeText(LoginActivity.this, "Google Sign In Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
