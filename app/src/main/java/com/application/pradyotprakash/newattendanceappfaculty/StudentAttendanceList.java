package com.application.pradyotprakash.newattendanceappfaculty;

import android.os.TokenWatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceList extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mStudentListView;
    private static String classValue;
    private static String subject;
    private static String from;
    private static String to;
    private String name;
    private TextView classValueText, subjectText, fromText, toText, nameText;
    private List<Students> studentsList;
    private StudentRecyclerAdapter studentRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_list);
        mToolbar = findViewById(R.id.studentAttendanceToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Student Lists");
        classValue = getIntent().getStringExtra("classValue");
        subject = getIntent().getStringExtra("subject");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        name = getIntent().getStringExtra("name");
        classValueText = findViewById(R.id.classValue);
        subjectText = findViewById(R.id.subjectValue);
        fromText = findViewById(R.id.fromValue);
        toText = findViewById(R.id.toValue);
        nameText = findViewById(R.id.nameValue);
        classValueText.setText(classValue);
        subjectText.setText(subject);
        fromText.setText(from);
        toText.setText(to);
        nameText.setText(name);
        mStudentListView = findViewById(R.id.studentList);
        mFirestore = FirebaseFirestore.getInstance();
        studentsList = new ArrayList<>();
        studentsList.clear();
        studentRecyclerAdapter = new StudentRecyclerAdapter(getApplicationContext(), studentsList);
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsList.clear();
        mFirestore.collection("Student").orderBy("usn").addSnapshotListener(StudentAttendanceList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String student_id = doc.getDocument().getId();
                        Students students = doc.getDocument().toObject(Students.class).withId(student_id);
                        studentsList.add(students);
                        studentRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public static String getClassValue() {
        return classValue;
    }

    public static String getSubject() {
        return subject;
    }

    public static String getFrom() {
        return from;
    }

    public static String getTo() {
        return to;
    }
}
