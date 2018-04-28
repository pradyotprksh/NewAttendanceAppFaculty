package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EachEventInformation extends AppCompatActivity {

    private String title, description, uploadedOn, uploadedBy, imageLink, eventId;
    private TextView eventTitle, eventDescrption, eventUploadedBy, eventUploadedOn, removeEvent;
    private ImageView eventImage;
    private FirebaseFirestore mFirestore, mFirestore1;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_event_information);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        uploadedOn = getIntent().getStringExtra("uploadedOn");
        uploadedBy = getIntent().getStringExtra("uploadedBy");
        imageLink = getIntent().getStringExtra("imageLink");
        eventId = getIntent().getStringExtra("eventId");
        Toolbar mToolbar = findViewById(R.id.uploadNotesToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        eventTitle = findViewById(R.id.eventTitle);
        eventDescrption = findViewById(R.id.eventDescription);
        eventUploadedBy = findViewById(R.id.uploadedBy);
        eventUploadedOn = findViewById(R.id.uploadedOn);
        eventImage = findViewById(R.id.eventImage);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(uploadedBy).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        eventUploadedOn.setText("Event Date: " + uploadedOn);
                        eventUploadedBy.setText("Uploaded By: " + task.getResult().getString("name"));
                        eventTitle.setText(title);
                        eventDescrption.setText(description);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.drawable.no_event_image);
                        Glide.with(EachEventInformation.this).setDefaultRequestOptions(placeHolderRequest).load(imageLink).into(eventImage);
                    }
                }
            }
        });
        removeEvent = findViewById(R.id.removeEvent);
        mFirestore1 = FirebaseFirestore.getInstance();
        removeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EachEventInformation.this);
                progress.setTitle("Please Wait.");
                progress.setMessage("Uploading File.");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                HashMap<String, Object> facultyMap = new HashMap<>();
                facultyMap.put("title", FieldValue.delete());
                facultyMap.put("description", FieldValue.delete());
                facultyMap.put("uploadedBy", FieldValue.delete());
                facultyMap.put("imageLink", FieldValue.delete());
                facultyMap.put("uploadedOn", FieldValue.delete());
                mFirestore1.collection("Events").document(eventId).update(facultyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EachEventInformation.this, "All the Data Has Been Deleted.", Toast.LENGTH_LONG).show();
                            progress.dismiss();
                            Intent intent = new Intent(EachEventInformation.this, SeeEvents.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progress.dismiss();
                            String image_error = task.getException().getMessage();
                            Toast.makeText(EachEventInformation.this, "Error: " + image_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
