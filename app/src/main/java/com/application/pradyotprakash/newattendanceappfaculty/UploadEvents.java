package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

public class UploadEvents extends AppCompatActivity {

    private String facultyId;
    private Uri facultyMainImageURI = null;
    private Button pickDate, pickTime, uploadEvent;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private int hourValue;
    private int minuteValue;
    private ImageView eventImage;
    private boolean isChanged = false;
    private EditText eventTitle, eventDescription;
    private StorageReference mfacultyStorageReference;
    private FirebaseFirestore mFirestore;
    private ProgressDialog progress;
    private int pickyear, pickmonth, pickday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_events);
        facultyId = getIntent().getStringExtra("facultyId");
        Toolbar mToolbar = findViewById(R.id.uploadNotesToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Upload Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pickDate = findViewById(R.id.eventDate);
        pickDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pickDate.setText("Pick Event Date");
                return true;
            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(UploadEvents.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,
                        month,
                        day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                pickyear = year;
                pickmonth = month;
                pickday = dayOfMonth;
                pickDate.setText(dayOfMonth + " / " + month + " / " + year);
                Toast.makeText(UploadEvents.this, "Long Press To Clear.", Toast.LENGTH_SHORT).show();
            }
        };
        pickTime = findViewById(R.id.eventTiming);
        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(UploadEvents.this, timeFromPickerListener, hourValue, minuteValue, false);
                timePickerDialog.show();
            }
        });
        pickTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pickTime.setText("Pick Event Timing");
                return true;
            }
        });
        eventImage = findViewById(R.id.eventImage);
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ContextCompat.
                            checkSelfPermission(UploadEvents.this,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                            (ContextCompat.
                                    checkSelfPermission(UploadEvents.this,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(UploadEvents.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        ActivityCompat.requestPermissions(UploadEvents.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        cropImage();
                    }
                } else {
                    cropImage();
                }
            }
        });
        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.uploadedBy);
        uploadEvent = findViewById(R.id.uploadEvent);
        mfacultyStorageReference = FirebaseStorage.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        progress = new ProgressDialog(UploadEvents.this);
        progress.setTitle("Please Wait.");
        progress.setMessage("Uploading Event Details.");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                final String title = eventTitle.getText().toString();
                final String description = eventDescription.getText().toString();
                final String date = pickDate.getText().toString();
                String time = pickTime.getText().toString();
                if (time.equals("Pick Event Timing")) {
                    time = "No Timing Specified";
                }
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && facultyMainImageURI != null) {
                    if (!date.equals("Pick Event Date")) {
                        if (isChanged) {
                            StorageReference image_path = mfacultyStorageReference.child("event_images").child(title + ".jpg");
                            final String finalTime = time;
                            image_path.putFile(facultyMainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        storeFireStore(task, title, description, facultyId, finalTime, date);
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(UploadEvents.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                }
                            });
                        } else {
                            storeFireStore(null, title, description, facultyId, time, date);
                        }
                    } else {
                        Toast.makeText(UploadEvents.this, "Add event date.", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                } else {
                    Toast.makeText(UploadEvents.this, "Give title and description for the event. And also give an event image", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    private void storeFireStore(Task<UploadTask.TaskSnapshot> task, String title, String description, String facultyId, String time, String date) {
        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = facultyMainImageURI;
        }
        String uploadedOn = date + " " + time;
        HashMap<String, String> facultyMap = new HashMap<>();
        facultyMap.put("title", title);
        facultyMap.put("description", description);
        facultyMap.put("uploadedBy", facultyId);
        facultyMap.put("imageLink", download_uri.toString());
        facultyMap.put("uploadedOn", uploadedOn);
        facultyMap.put("year", String.valueOf(pickyear));
        facultyMap.put("month", String.valueOf(pickmonth));
        facultyMap.put("day", String.valueOf(pickday));
        mFirestore.collection("Events").document().set(facultyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadEvents.this, "All the Data Has Been Uploaded.", Toast.LENGTH_LONG).show();
                    eventTitle.setText("");
                    eventDescription.setText("");
                    eventImage.setImageResource(R.drawable.no_event_image);
                    pickDate.setText("Pick Event Date");
                    pickTime.setText("Pick Event Timing");
                    progress.dismiss();
                } else {
                    String image_error = task.getException().getMessage();
                    Toast.makeText(UploadEvents.this, "Error: " + image_error, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener timeFromPickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay == 0) {
                hourOfDay = 12;
            }
            String hourString = String.format("%02d", hourOfDay);
            String minuteString = String.valueOf(minute);
            String time = hourString + ":" + minuteString;
            pickTime.setText(time);
            Toast.makeText(UploadEvents.this, "Long Press To Clear.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                facultyMainImageURI = result.getUri();
                eventImage.setImageURI(facultyMainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(UploadEvents.this);
    }

}
