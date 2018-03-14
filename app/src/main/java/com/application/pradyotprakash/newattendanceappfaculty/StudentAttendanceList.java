package com.application.pradyotprakash.newattendanceappfaculty;

import android.os.TokenWatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StudentAttendanceList extends AppCompatActivity {

    private String classValue, subject, from, to, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_list);
        classValue = getIntent().getStringExtra("classValue");
        subject = getIntent().getStringExtra("subject");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        name = getIntent().getStringExtra("name");
        Toast.makeText(StudentAttendanceList.this, classValue + " " + subject + " " + from + " " + to + " " + name, Toast.LENGTH_LONG).show();
    }
}
