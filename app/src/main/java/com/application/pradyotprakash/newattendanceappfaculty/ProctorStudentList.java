package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProctorStudentList extends AppCompatActivity {

    private String classValue;
    private Button addStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proctor_student_list);
        classValue = getIntent().getStringExtra("classValue");
        addStudent = findViewById(R.id.addStudent);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProctorStudentList.this, FacultyProctorDetails.class);
                intent.putExtra("classValue", classValue);
                startActivity(intent);
            }
        });
    }
}
