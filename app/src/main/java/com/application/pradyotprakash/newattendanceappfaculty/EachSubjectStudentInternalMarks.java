package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class EachSubjectStudentInternalMarks extends AppCompatActivity {

    private String studentId, subjectCode, semester, type;
    private AutoCompleteTextView internalValues;
    private ImageView internalSpinner;
    private static final String[] branch = new String[]{"Internal 1", "Internal 2", "Internal 3"};
    private EditText marksObtainedValue, totalMarksValue;
    private FirebaseFirestore mFirestore, mFirestore2, mFirestore1;
    private int marksObtained, totalMarks, internal1, internal2, internal3;
    private Button updateMarks;
    private String internalSelectedValue;
    private TextView currentAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_subject_student_internal_marks);
        Toolbar mToolbar = findViewById(R.id.studentInternalToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Internal Marks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        studentId = getIntent().getStringExtra("studentId");
        subjectCode = getIntent().getStringExtra("subjectCode");
        semester = getIntent().getStringExtra("semester");
        type = getIntent().getStringExtra("type");
        marksObtainedValue = findViewById(R.id.marksObtainedValue);
        totalMarksValue = findViewById(R.id.totalMarksValue);
        internalValues = findViewById(R.id.internalsOption);
        internalSpinner = findViewById(R.id.internal_spinner);
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, branch);
        internalValues.setAdapter(adapterBranch);
        internalSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internalValues.showDropDown();
            }
        });
        mFirestore = FirebaseFirestore.getInstance();
        internalValues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                internalSelectedValue = internalValues.getText().toString();
                if (internalSelectedValue.equals("Internal 1")) {
                    try {
                        mFirestore.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document(internalSelectedValue).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        marksObtained = Integer.valueOf(task.getResult().getString("marksObtained"));
                                        totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                        marksObtainedValue.setText(String.valueOf(marksObtained));
                                        totalMarksValue.setText(String.valueOf(totalMarks));
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(EachSubjectStudentInternalMarks.this, "Enter Marks for " + internalSelectedValue, Toast.LENGTH_SHORT).show();
                        marksObtainedValue.setText("");
                        totalMarksValue.setText("");
                    }
                } else if (internalSelectedValue.equals("Internal 2")) {
                    try {
                        mFirestore.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document(internalSelectedValue).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        marksObtained = Integer.valueOf(task.getResult().getString("marksObtained"));
                                        totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                        marksObtainedValue.setText(String.valueOf(marksObtained));
                                        totalMarksValue.setText(String.valueOf(totalMarks));
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(EachSubjectStudentInternalMarks.this, "Enter Marks for " + internalSelectedValue, Toast.LENGTH_SHORT).show();
                        marksObtainedValue.setText("");
                        totalMarksValue.setText("");
                    }
                } else if (internalSelectedValue.equals("Internal 3")) {
                    try {
                        mFirestore.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document(internalSelectedValue).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        marksObtained = Integer.valueOf(task.getResult().getString("marksObtained"));
                                        totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                        marksObtainedValue.setText(String.valueOf(marksObtained));
                                        totalMarksValue.setText(String.valueOf(totalMarks));
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(EachSubjectStudentInternalMarks.this, "Enter Marks for " + internalSelectedValue, Toast.LENGTH_SHORT).show();
                        marksObtainedValue.setText("");
                        totalMarksValue.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        updateMarks = findViewById(R.id.updateMarks);
        mFirestore2 = FirebaseFirestore.getInstance();
        updateMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(internalSelectedValue) && !(TextUtils.isEmpty(marksObtainedValue.getText().toString())) && !(TextUtils.isEmpty(totalMarksValue.getText().toString()))) {
                    if (Integer.valueOf(marksObtainedValue.getText().toString()) > Integer.valueOf(totalMarksValue.getText().toString())) {
                        Toast.makeText(EachSubjectStudentInternalMarks.this, "Marks Obtained is Greater than Total Marks.", Toast.LENGTH_SHORT).show();
                    }
                    HashMap<String, Object> internalMarks = new HashMap<>();
                    internalMarks.put("marksObtained", marksObtainedValue.getText().toString());
                    internalMarks.put("totalMarks", totalMarksValue.getText().toString());
                    mFirestore2.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document(internalSelectedValue).set(internalMarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EachSubjectStudentInternalMarks.this, "Marks Entered For " + internalSelectedValue, Toast.LENGTH_SHORT).show();
                            marksObtainedValue.setText("");
                        }
                    });
                } else {
                    Toast.makeText(EachSubjectStudentInternalMarks.this, "Enter The Details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore1.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document("Internal 1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            }
        });
    }
}
