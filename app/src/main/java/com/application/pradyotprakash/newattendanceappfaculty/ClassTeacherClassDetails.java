package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClassTeacherClassDetails extends AppCompatActivity {

    private static String classValue;
    private String facultyId;
    private List<ClassStudentList> studentsList;
    private ClassStudentRecyclerAdapter studentRecyclerAdapter1;
    private FirebaseFirestore mFirestore;
    private RecyclerView mStudentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher_class_details);
        classValue = getIntent().getStringExtra("classValue");
        facultyId = getIntent().getStringExtra("facultyId");
        Toolbar mToolbar = findViewById(R.id.facultySetupToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(classValue + " Details");
        mFirestore = FirebaseFirestore.getInstance();
        mStudentListView = findViewById(R.id.studentList);
        studentsList = new ArrayList<>();
        studentsList.clear();
        studentRecyclerAdapter1 = new ClassStudentRecyclerAdapter(getApplicationContext(), studentsList);
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter1);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mStudentListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(ClassTeacherClassDetails.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mStudentListView.addItemDecoration(horizontalDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsList.clear();
        mFirestore.collection("Student").orderBy("usn").addSnapshotListener(ClassTeacherClassDetails.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String student_id = doc.getDocument().getId();
                        ClassStudentList students = doc.getDocument().toObject(ClassStudentList.class).withId(student_id);
                        studentsList.add(students);
                        studentRecyclerAdapter1.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public static String getClassValue() {
        return classValue;
    }
}
