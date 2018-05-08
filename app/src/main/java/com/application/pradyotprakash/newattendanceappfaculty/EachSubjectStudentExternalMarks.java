package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class EachSubjectStudentExternalMarks extends AppCompatActivity {

    private String studentId, subjectCode, semester;
    private EditText marksObtainedValue, totalMarksValue;
    private Button updateMarks;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_subject_student_external_marks);
        Toolbar mToolbar = findViewById(R.id.studentExternalToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("External Marks");
        studentId = getIntent().getStringExtra("studentId");
        subjectCode = getIntent().getStringExtra("subjectCode");
        semester = getIntent().getStringExtra("semester");
        marksObtainedValue = findViewById(R.id.marksObtainedValue);
        totalMarksValue = findViewById(R.id.totalMarksValue);
        updateMarks = findViewById(R.id.updateMarks);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        updateMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(marksObtainedValue.getText().toString())) && !(TextUtils.isEmpty(totalMarksValue.getText().toString()))) {
                    boolean abCheck = Pattern.matches("ab", marksObtainedValue.getText().toString());
                    boolean checkAB = Pattern.matches("AB", marksObtainedValue.getText().toString());
                    boolean intCheck = Pattern.matches("[0-9]+", marksObtainedValue.getText().toString());
                    if (abCheck || checkAB) {
                        HashMap<String, Object> externalMarks = new HashMap<>();
                        externalMarks.put("marksObtained", marksObtainedValue.getText().toString().toUpperCase());
                        externalMarks.put("totalMarks", totalMarksValue.getText().toString());
                        mFirestore.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("External").document("Marks").set(externalMarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EachSubjectStudentExternalMarks.this, "Marks Entered.", Toast.LENGTH_SHORT).show();
                                marksObtainedValue.setText("");
                            }
                        });
                    } else if (intCheck) {
                        if (Integer.valueOf(marksObtainedValue.getText().toString()) < Integer.valueOf(totalMarksValue.getText().toString())) {
                            HashMap<String, Object> externalMarks = new HashMap<>();
                            externalMarks.put("marksObtained", marksObtainedValue.getText().toString());
                            externalMarks.put("totalMarks", totalMarksValue.getText().toString());
                            mFirestore1.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("External").document("Marks").set(externalMarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EachSubjectStudentExternalMarks.this, "Marks Entered.", Toast.LENGTH_SHORT).show();
                                    marksObtainedValue.setText("");
                                }
                            });
                        } else {
                            Toast.makeText(EachSubjectStudentExternalMarks.this, "Obtained Marks is Greater than Total Marks.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EachSubjectStudentExternalMarks.this, "Give Correct Input.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EachSubjectStudentExternalMarks.this, "Enter The Details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirestore2.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("External").document("Marks").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String marksObtained = task.getResult().getString("marksObtained");
                        String totalMarks = task.getResult().getString("totalMarks");
                        marksObtainedValue.setText(marksObtained);
                        totalMarksValue.setText(totalMarks);
                        if (Integer.valueOf(marksObtained) < 35) {
                            marksObtainedValue.setTextColor(Color.rgb(244, 67, 54));
                        }
                    }
                }
            }
        });
    }
}
