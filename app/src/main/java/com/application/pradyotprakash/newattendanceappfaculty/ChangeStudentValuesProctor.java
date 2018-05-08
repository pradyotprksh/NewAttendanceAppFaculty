package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class ChangeStudentValuesProctor extends AppCompatActivity {

    private String studentId;
    private String currentSemester, className;
    private FirebaseFirestore mFirestore, mFirestore1;
    private ImageView semesterSpinner;
    private AutoCompleteTextView semesterOption;
    private EditText classValue;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
    private Button updateValues;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_student_values_proctor);
        Toolbar mToolbar = findViewById(R.id.studentInternalToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Semester & Class");
        studentId = getIntent().getStringExtra("studentId");
        semesterOption = findViewById(R.id.faculty_selectclass);
        semesterSpinner = findViewById(R.id.semester_spinner);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(ChangeStudentValuesProctor.this, android.R.layout.simple_dropdown_item_1line, semester);
        semesterOption.setAdapter(adapterSemester);
        semesterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterOption.showDropDown();
            }
        });
        classValue = findViewById(R.id.currentClassValue);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        updateValues = findViewById(R.id.updateValues);
        mFirestore.collection("Student").document(studentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        currentSemester = task.getResult().getString("semester");
                        className = task.getResult().getString("className");
                        semesterOption.setText(currentSemester);
                        classValue.setText(className);
                    }
                }
            }
        });
        progress = new ProgressDialog(ChangeStudentValuesProctor.this);
        progress.setTitle("Please Wait.");
        progress.setMessage("Updating Values.");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        updateValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                HashMap<String, Object> updatedValues = new HashMap<>();
                updatedValues.put("semester", semesterOption.getText().toString());
                updatedValues.put("className", classValue.getText().toString().toUpperCase());
                mFirestore1.collection("Student").document(studentId).update(updatedValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangeStudentValuesProctor.this, "Updated Values", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
