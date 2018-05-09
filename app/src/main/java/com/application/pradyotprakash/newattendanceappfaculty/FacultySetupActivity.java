package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultySetupActivity extends AppCompatActivity {

    private CircleImageView facultySetupImage;
    private Uri facultyMainImageURI = null;
    private EditText facultyName, facultyId;
    private AutoCompleteTextView facultyBranch;
    private Button facultySetupBtn, facultyEditSetupBtn;
    private StorageReference mfacultyStorageReference;
    private FirebaseAuth mAuth;
    private ProgressBar facultySetupProgress;
    private FirebaseFirestore facultySetupFirestore;
    private String user_id, classTeacherOf = "No Data Selected";
    private boolean isChanged = false;
    private ImageView branchSpinner;
    private CheckBox classTeacher, proctor;
    String isClassTeacherChecked = "false", isProctorChecked = "false";
    private static final String[] branch = new String[]{"Bio Technology Engineering", "Civil Engineering", "Computer Science Engineering", "Electrical & Electronics Engineering", "Electronics & Comm. Engineering", "Information Science & Engineering", "Mechanical Engineering"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_setup);
        Toolbar mToolbar = findViewById(R.id.facultySetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Your Details");
        facultySetupProgress = findViewById(R.id.faculty_setup_progress);
        facultySetupImage = findViewById(R.id.faculty_setup_image);
        facultyId = findViewById(R.id.faculty_setup_id);
        facultySetupBtn = findViewById(R.id.faculty_setup_btn);
        facultyName = findViewById(R.id.faculty_setup_name);
        facultyBranch = findViewById(R.id.faculty_setup_branch);
        facultyBranch.setThreshold(1);
        branchSpinner = findViewById(R.id.branch_spinner);
        classTeacher = findViewById(R.id.classTeacherOption);
        proctor = findViewById(R.id.proctorOption);
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, branch);
        facultyBranch.setAdapter(adapterBranch);
        branchSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facultyBranch.showDropDown();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mfacultyStorageReference = FirebaseStorage.getInstance().getReference();
        facultySetupFirestore = FirebaseFirestore.getInstance();
        facultySetupProgress.setVisibility(View.VISIBLE);
        facultyName.setEnabled(false);
        facultyId.setEnabled(false);
        classTeacher.setEnabled(false);
        proctor.setEnabled(false);
        facultyBranch.setEnabled(false);
        branchSpinner.setEnabled(false);
        facultySetupFirestore.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String branch = task.getResult().getString("branch");
                        String id = task.getResult().getString("id");
                        String image = task.getResult().getString("image");
                        isClassTeacherChecked = task.getResult().getString("classTeacherValue");
                        isProctorChecked = task.getResult().getString("proctorValue");
                        try {
                            classTeacherOf = task.getResult().getString("classTeacherOf");
                        } catch (Exception e) {
                            classTeacherOf = "No Data Selected";
                        }
                        if (isClassTeacherChecked.equals("true")) {
                            classTeacher.setChecked(true);
                        } else {
                            classTeacher.setChecked(false);
                        }
                        if (isProctorChecked.equals("true")) {
                            proctor.setChecked(true);
                        } else {
                            proctor.setChecked(false);
                        }
                        facultyMainImageURI = Uri.parse(image);
                        facultyName.setText(name);
                        facultyBranch.setText(branch);
                        facultyId.setText(id);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        Glide.with(FacultySetupActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(facultySetupImage);
                        facultyName.setEnabled(false);
                        facultyId.setEnabled(false);
                        facultyBranch.setEnabled(false);
                        branchSpinner.setEnabled(false);
                    } else {
                        Toast.makeText(FacultySetupActivity.this, "Fill All The Data and Upload a Profile Image.", Toast.LENGTH_SHORT).show();
                        facultyName.setEnabled(true);
                        facultyId.setEnabled(true);
                        facultyBranch.setEnabled(true);
                        branchSpinner.setEnabled(true);
                        classTeacher.setClickable(true);
                        proctor.setClickable(true);
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(FacultySetupActivity.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
                facultySetupProgress.setVisibility(View.INVISIBLE);
            }
        });
        facultySetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String faculty_name = facultyName.getText().toString();
                final String faculty_branch = facultyBranch.getText().toString();
                final String faculty_id = facultyId.getText().toString();
                if (!TextUtils.isEmpty(faculty_name) && !TextUtils.isEmpty(faculty_branch) && !TextUtils.isEmpty(faculty_id) && facultyMainImageURI != null) {
                    facultySetupProgress.setVisibility(View.VISIBLE);
                    if (isChanged) {
                        StorageReference image_path = mfacultyStorageReference.child("faculty_profile_images").child(user_id + ".jpg");
                        image_path.putFile(facultyMainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFireStore(task, faculty_name, faculty_branch, faculty_id);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(FacultySetupActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    facultySetupProgress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        storeFireStore(null, faculty_name, faculty_branch, faculty_id);
                    }

                } else {
                    Toast.makeText(FacultySetupActivity.this, "Fill all the details and add a profile image also.", Toast.LENGTH_LONG).show();
                }
            }
        });

        facultyEditSetupBtn = findViewById(R.id.faculty_editsetup_btn);
        facultyEditSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facultyEditSetupBtn.getText().toString().equals("Edit Details")) {
                    Toast.makeText(FacultySetupActivity.this, "Click On Add Details After Editing.", Toast.LENGTH_SHORT).show();
                    facultyName.setEnabled(true);
                    facultyId.setEnabled(true);
                    facultyBranch.setEnabled(true);
                    branchSpinner.setEnabled(true);
                    classTeacher.setEnabled(true);
                    proctor.setEnabled(true);
                    facultyEditSetupBtn.setText("Done Editing");
                } else if (facultyEditSetupBtn.getText().toString().equals("Done Editing")){
                    facultyName.setEnabled(false);
                    facultyId.setEnabled(false);
                    facultyBranch.setEnabled(false);
                    branchSpinner.setEnabled(false);
                    classTeacher.setEnabled(false);
                    proctor.setEnabled(false);
                    facultyEditSetupBtn.setText("Edit Details");
                }
            }
        });

        facultySetupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ContextCompat.
                            checkSelfPermission(FacultySetupActivity.this,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                            (ContextCompat.
                                    checkSelfPermission(FacultySetupActivity.this,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(FacultySetupActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        ActivityCompat.requestPermissions(FacultySetupActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        cropImage();
                    }
                } else {
                    cropImage();
                }
            }
        });
    }

    private void storeFireStore(Task<UploadTask.TaskSnapshot> task, String faculty_name, String faculty_branch, String faculty_id) {
        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = facultyMainImageURI;
        }
        if (classTeacher.isChecked()) {
            isClassTeacherChecked = "true";
        } else {
            isClassTeacherChecked = "false";
        }
        if (proctor.isChecked()) {
            isProctorChecked = "true";
        } else {
            isProctorChecked = "false";
        }
        String token_id = FirebaseInstanceId.getInstance().getToken();
        HashMap<String, String> facultyMap = new HashMap<>();
        facultyMap.put("name", faculty_name);
        facultyMap.put("branch", faculty_branch);
        facultyMap.put("id", faculty_id);
        facultyMap.put("image", download_uri.toString());
        facultyMap.put("token_id", token_id);
        facultyMap.put("classTeacherOf", classTeacherOf);
        facultyMap.put("classTeacherValue", isClassTeacherChecked);
        facultyMap.put("proctorValue", isProctorChecked);
        facultySetupFirestore.collection("Faculty").document(user_id).set(facultyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FacultySetupActivity.this, "All the Data Has Been Uploaded.", Toast.LENGTH_LONG).show();
                    facultySetupProgress.setVisibility(View.INVISIBLE);
                } else {
                    String image_error = task.getException().getMessage();
                    Toast.makeText(FacultySetupActivity.this, "Error: " + image_error, Toast.LENGTH_SHORT).show();
                    facultySetupProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                facultyMainImageURI = result.getUri();
                facultySetupImage.setImageURI(facultyMainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(FacultySetupActivity.this);
    }

}

