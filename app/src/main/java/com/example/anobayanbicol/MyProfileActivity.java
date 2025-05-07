package com.example.anobayanbicol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class MyProfileActivity extends AppCompatActivity {

    private EditText name, email, phone;
    private Button btnEdit, btnSave, btnCancel;
    private TextView profileText;
    private LinearLayout layoutBottom;
    private String nameStr, phoneStr;
    private ProgressDialog progressDialog;
    private String originalNameStr, originalPhoneStr;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView profileImage;
    private TextView tvUploadImage;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");
        progressDialog.setCancelable(false);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.etPhone);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        profileText = findViewById(R.id.profile_text);
        layoutBottom = findViewById(R.id.layoutBottom);


        profileImage = findViewById(R.id.profileImage);
        tvUploadImage = findViewById(R.id.tvUploadImage);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        profileImage.setOnClickListener(v -> showImageOptionsDialog());

        tvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageOptionsDialog();
            }
        });



        disableEditing();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restore original values
                name.setText(originalNameStr);
                phone.setText(originalPhoneStr);
                disableEditing();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    saveData();
                }
            }
        });
    }

    private void disableEditing(){
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);

        layoutBottom.setVisibility(View.GONE);

        name.setText(DBQuery.myProfile.getName());
        email.setText(DBQuery.myProfile.getEmail());

        if (DBQuery.myProfile.getPhone() != null)
            phone.setText(DBQuery.myProfile.getPhone());

        String profileName = DBQuery.myProfile.getName();
        profileText.setText(profileName.toUpperCase().substring(0, 1));
    }

    private void enableEditing(){
        name.setEnabled(true);
        phone.setEnabled(true);

        // Cache current values
        originalNameStr = name.getText().toString();
        originalPhoneStr = phone.getText().toString();

        layoutBottom.setVisibility(View.VISIBLE);
    }


    private boolean validate() {
        nameStr = name.getText().toString().trim();
        phoneStr = phone.getText().toString().trim();

        if (nameStr.isEmpty()) {
            name.setError("Name can't be empty");
            return false;
        }

        if (phoneStr.isEmpty()) {
            phone.setError("Phone number can't be empty");
            return false;
        }

        String normalizedPhone = phoneStr;
        if (phoneStr.startsWith("+63")) {
            normalizedPhone = "0" + phoneStr.substring(3); // Convert +63 to 0
        }

        if (normalizedPhone.length() != 11 || !TextUtils.isDigitsOnly(normalizedPhone) || !normalizedPhone.startsWith("09")) {
            phone.setError("Enter a valid Philippine phone number (e.g. 09XXXXXXXXX or +639XXXXXXXXX)");
            return false;
        }

        return true;
    }

    private void saveData(){
        progressDialog.show();

        if (phoneStr.isEmpty())
            phoneStr = null;

        DBQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                disableEditing();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            MyProfileActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showImageOptionsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Profile Image")
                .setItems(new CharSequence[]{"Upload", "Change", "Delete"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                        case 1:
                            pickImageFromGallery();
                            break;
                        case 2:
                            deleteImage();
                            break;
                    }
                })
                .show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            String mimeType = getContentResolver().getType(imageUri);

            if (mimeType != null && (mimeType.equals("image/png") ||
                    mimeType.equals("image/jpeg") || mimeType.equals("image/jpg"))) {

                profileImage.setImageURI(imageUri);
                uploadPicture();

            } else {
                Toast.makeText(this, "Only PNG, JPG, and JPEG files are allowed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadPicture() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference userImageRef = storageRef.child("profile_images/" + userId + ".jpg");

        try {
            // Open an InputStream from the imageUri
            InputStream stream = getContentResolver().openInputStream(imageUri);
            UploadTask uploadTask = userImageRef.putStream(stream);

            uploadTask.addOnSuccessListener(taskSnapshot ->
                    userImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();

                        // Update Firestore with the new image URL
                        DBQuery.g_firestore.collection("USERS")
                                .document(userId)
                                .update("PROFILE_IMAGE_PATH", downloadUrl)
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Snackbar.make(findViewById(android.R.id.content), "Image uploaded.", Snackbar.LENGTH_LONG).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(MyProfileActivity.this, "Failed to update Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } catch (FileNotFoundException e) {
            progressDialog.dismiss();
            Toast.makeText(this, "File not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    private void deleteImage() {
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        DBQuery.g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String fileName = documentSnapshot.getString("PROFILE_IMAGE_PATH");

                    if (fileName == null) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "No image to delete.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    StorageReference deleteRef = storage.getReferenceFromUrl(fileName);
                    deleteRef.delete()
                            .addOnSuccessListener(aVoid -> {
                                profileImage.setImageResource(R.drawable.ic_default_profile);
                                // remove image reference from Firestore
                                DBQuery.g_firestore.collection("USERS")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .update("PROFILE_IMAGE_PATH", null);

                                progressDialog.dismiss();
                                Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Delete Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error retrieving image info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




}