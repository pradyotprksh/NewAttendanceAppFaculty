package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EachSubjectStudentMarks extends AppCompatActivity {

    private String studentId, subjectCode, semester;
    private Button internalMarks, externalMarks, seeMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_subject_student_marks);
        Toolbar mToolbar = findViewById(R.id.facultySetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Marks");
        studentId = getIntent().getStringExtra("studentId");
        subjectCode = getIntent().getStringExtra("subjectCode");
        semester = getIntent().getStringExtra("semester");
        internalMarks = findViewById(R.id.internalMarks);
        externalMarks = findViewById(R.id.externalMarks);
        internalMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachSubjectStudentMarks.this, EachSubjectStudentInternalMarks.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("semester", semester);
                intent.putExtra("subjectCode", subjectCode);
                startActivity(intent);
            }
        });
        externalMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachSubjectStudentMarks.this, EachSubjectStudentExternalMarks.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("semester", semester);
                intent.putExtra("subjectCode", subjectCode);
                startActivity(intent);
            }
        });
        seeMarks = findViewById(R.id.seeMarks);
        seeMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachSubjectStudentMarks.this, FacultyEachSubjectStudentMarks.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("semester", semester);
                intent.putExtra("subjectCode", subjectCode);
                startActivity(intent);
            }
        });
    }
}
