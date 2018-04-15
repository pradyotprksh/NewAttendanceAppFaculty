package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.Color;
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
import java.util.regex.Pattern;

public class EachSubjectStudentInternalMarks extends AppCompatActivity {

    private String studentId, subjectCode, semester;
    private AutoCompleteTextView internalValues;
    private ImageView internalSpinner;
    private static final String[] branch = new String[]{"Internal 1", "Internal 2", "Internal 3"};
    private EditText marksObtainedValue, totalMarksValue;
    private FirebaseFirestore mFirestore, mFirestore2, mFirestore1, mFirestore3, mFirestore4;
    private int marksObtained, totalMarks, internal1, internal2, internal3, averageValue;
    private Button updateMarks, calculateAverage;
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
                                        if (task.getResult().getString("marksObtained").equals("AB")) {
                                            totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                            marksObtainedValue.setText(task.getResult().getString("marksObtained"));
                                            totalMarksValue.setText(String.valueOf(totalMarks));
                                        } else {
                                            marksObtained = Integer.valueOf(task.getResult().getString("marksObtained"));
                                            totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                            marksObtainedValue.setText(String.valueOf(marksObtained));
                                            totalMarksValue.setText(String.valueOf(totalMarks));
                                        }
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
                                        if (task.getResult().getString("marksObtained").equals("AB")) {
                                            totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                            marksObtainedValue.setText(task.getResult().getString("marksObtained"));
                                            totalMarksValue.setText(String.valueOf(totalMarks));
                                        } else {
                                            marksObtained = Integer.valueOf(task.getResult().getString("marksObtained"));
                                            totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                            marksObtainedValue.setText(String.valueOf(marksObtained));
                                            totalMarksValue.setText(String.valueOf(totalMarks));
                                        }
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
                                        if (task.getResult().getString("marksObtained").equals("AB")) {
                                            totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                            marksObtainedValue.setText(task.getResult().getString("marksObtained"));
                                            totalMarksValue.setText(String.valueOf(totalMarks));
                                        } else {
                                            marksObtained = Integer.valueOf(task.getResult().getString("marksObtained"));
                                            totalMarks = Integer.valueOf(task.getResult().getString("totalMarks"));
                                            marksObtainedValue.setText(String.valueOf(marksObtained));
                                            totalMarksValue.setText(String.valueOf(totalMarks));
                                        }
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
                    boolean abCheck = Pattern.matches("ab", marksObtainedValue.getText().toString());
                    boolean checkAB = Pattern.matches("AB", marksObtainedValue.getText().toString());
                    boolean intCheck = Pattern.matches("[0-9]+", marksObtainedValue.getText().toString());
                    if (abCheck || checkAB) {
                        HashMap<String, Object> internalMarks = new HashMap<>();
                        internalMarks.put("marksObtained", marksObtainedValue.getText().toString().toUpperCase());
                        internalMarks.put("totalMarks", totalMarksValue.getText().toString());
                        mFirestore.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document(internalValues.getText().toString()).set(internalMarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EachSubjectStudentInternalMarks.this, "Marks Entered.", Toast.LENGTH_SHORT).show();
                                marksObtainedValue.setText("");
                            }
                        });
                    } else if (intCheck) {
                        if (Integer.valueOf(marksObtainedValue.getText().toString()) < Integer.valueOf(totalMarksValue.getText().toString())) {
                            HashMap<String, Object> internalMarks = new HashMap<>();
                            internalMarks.put("marksObtained", marksObtainedValue.getText().toString());
                            internalMarks.put("totalMarks", totalMarksValue.getText().toString());
                            mFirestore.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document(internalValues.getText().toString()).set(internalMarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EachSubjectStudentInternalMarks.this, "Marks Entered.", Toast.LENGTH_SHORT).show();
                                    marksObtainedValue.setText("");
                                }
                            });
                        } else {
                            Toast.makeText(EachSubjectStudentInternalMarks.this, "Obtained Marks is Greater than Total Marks.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EachSubjectStudentInternalMarks.this, "Give Correct Input.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EachSubjectStudentInternalMarks.this, "Enter The Details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        mFirestore4 = FirebaseFirestore.getInstance();
        currentAverage = findViewById(R.id.currentAverage);
        calculateAverage = findViewById(R.id.calculateAverage);
        calculateAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore1.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document("Internal 1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getString("marksObtained").equals("AB")) {
                                    internal1 = 0;
                                } else {
                                    internal1 = Integer.valueOf(task.getResult().getString("marksObtained"));
                                }
                                mFirestore3.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document("Internal 2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                if (task.getResult().getString("marksObtained").equals("AB")) {
                                                    internal2 = 0;
                                                } else {
                                                    internal2 = Integer.valueOf(task.getResult().getString("marksObtained"));
                                                }
                                                mFirestore4.collection("Student").document(studentId).collection(semester).document("Marks").collection(subjectCode).document("Marks").collection("Internal").document("Internal 3").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().exists()) {
                                                                if (task.getResult().getString("marksObtained").equals("AB")) {
                                                                    internal3 = 0;
                                                                } else {
                                                                    internal3 = Integer.valueOf(task.getResult().getString("marksObtained"));
                                                                }
                                                                if (internal1 >= internal2 && internal1 >= internal3) {
                                                                    if (internal2 >= internal3) {
                                                                        averageValue = (internal1 + internal2) / 2;
                                                                        currentAverage.setText(String.valueOf(averageValue));
                                                                    } else {
                                                                        averageValue = (internal1 + internal3) / 2;
                                                                        currentAverage.setText(String.valueOf(averageValue));
                                                                    }
                                                                } else if (internal2 >= internal1 && internal2 >= internal3) {
                                                                    if (internal1 >= internal3) {
                                                                        averageValue = (internal2 + internal1) / 2;
                                                                        currentAverage.setText(String.valueOf(averageValue));
                                                                    } else {
                                                                        averageValue = (internal2 + internal3) / 2;
                                                                        currentAverage.setText(String.valueOf(averageValue));
                                                                    }
                                                                } else if (internal3 >= internal1 && internal3 >= internal2) {
                                                                    if (internal1 >= internal2) {
                                                                        averageValue = (internal3 + internal1) / 2;
                                                                        currentAverage.setText(String.valueOf(averageValue));
                                                                    } else {
                                                                        averageValue = (internal3 + internal2) / 2;
                                                                        currentAverage.setText(String.valueOf(averageValue));
                                                                    }
                                                                }
                                                                if (averageValue < 15) {
                                                                    currentAverage.setTextColor(Color.rgb(244, 67, 54));
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }
}
