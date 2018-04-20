package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UploadNotes extends AppCompatActivity {

    private String semesterValue, classValue, branch;
    private EditText noteTitle, noteDescription, noteName;
    private Button selectFile, uploadFile;
    private static final int FILE_REQUEST = 9002;
    private static final int REQUEST_READ_PERMISSION = 9003;
    private StorageReference mStorageReference;
    private Uri fileUri;
    private FirebaseFirestore mFirestore;
    private String fileUrl;
    private Uri noteMainUri = null;
    private FirebaseAuth mAuth;
    private static String user_id;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);
        semesterValue = getIntent().getStringExtra("semesterValue");
        branch = getIntent().getStringExtra("branch");
        classValue = getIntent().getStringExtra("classValue");
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        Toolbar mToolbar = findViewById(R.id.uploadNotesToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Upload Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        noteTitle = findViewById(R.id.noteTitle);
        noteDescription = findViewById(R.id.noteDescription);
        noteName = findViewById(R.id.noteName);
        selectFile = findViewById(R.id.selectFile);
        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        mFirestore = FirebaseFirestore.getInstance();
        uploadFile = findViewById(R.id.uploadFile);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(UploadNotes.this);
                progress.setTitle("Please Wait.");
                progress.setMessage("Uploading File.");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                if (!TextUtils.isEmpty(noteName.getText().toString())) {
                    if (!TextUtils.isEmpty(noteTitle.getText().toString())) {
                        if (TextUtils.isEmpty(noteDescription.getText().toString())) {
                            noteDescription.setText("Notes for " + semesterValue + " of class " + classValue);
                        }
                        StorageReference file_path = mStorageReference.child("student_notes").child(noteName.getText().toString());
                        file_path.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFireStore(task, noteTitle.getText().toString(), noteDescription.getText().toString(), noteName.getText().toString());
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(UploadNotes.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(UploadNotes.this,"Enter The Title For The Note", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                } else {
                    Toast.makeText(UploadNotes.this,"Select A File.", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    private void storeFireStore(Task<UploadTask.TaskSnapshot> task, String title, String description, final String name) {
        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = noteMainUri;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm");
        String date = simpleDateFormat.format(new Date());
        HashMap<String, String> noteMap = new HashMap<>();
        noteMap.put("name", name);
        noteMap.put("title", title);
        noteMap.put("description", description);
        noteMap.put("uploadedBy", user_id);
        noteMap.put("branch", branch);
        noteMap.put("noteLink", download_uri.toString());
        noteMap.put("uploadedOn", date);
        mFirestore.collection("Notes").document(branch).collection(semesterValue).document(classValue).collection("Uploaded").document().set(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadNotes.this, name + " Has Been Uploaded.", Toast.LENGTH_LONG).show();
                    noteTitle.setText("");
                    noteDescription.setText("");
                    noteName.setText("");
                    progress.dismiss();
                } else {
                    String image_error = task.getException().getMessage();
                    Toast.makeText(UploadNotes.this, "Error: " + image_error, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.
                    checkSelfPermission(UploadNotes.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.
                            checkSelfPermission(UploadNotes.this,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(UploadNotes.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(UploadNotes.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openFilePicker();
            }
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 9002:
                if (resultCode == RESULT_OK) {
                    fileUri = data.getData();
                    String uriString = fileUri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getApplicationContext().getContentResolver().query(fileUri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    noteName.setText(displayName);
                }
                break;
        }
    }
}
