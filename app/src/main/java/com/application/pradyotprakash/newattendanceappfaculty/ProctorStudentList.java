package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProctorStudentList extends AppCompatActivity {

    private String branch;
    private Button addStudent;
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mStudentListView;
    private List<ProctorStudent> studentsList;
    private ProctorStudentRecyclerAdapter studentRecyclerAdapter;
    private FirebaseFirestore mFirestore;
    private String userId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proctor_student_list);
        mToolbar = findViewById(R.id.studentListProctorToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Semester");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        branch = getIntent().getStringExtra("branch");
        addStudent = findViewById(R.id.addStudent);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProctorStudentList.this, SelectSemesterClassProctor.class);
                intent.putExtra("branch", branch);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mStudentListView = findViewById(R.id.studentList);
        studentsList = new ArrayList<>();
        studentRecyclerAdapter = new ProctorStudentRecyclerAdapter(studentsList, getApplicationContext());
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(userId).collection("Proctor").addSnapshotListener(ProctorStudentList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String proctor_student_id = doc.getDocument().getId();
                        ProctorStudent students = doc.getDocument().toObject(ProctorStudent.class).withId(proctor_student_id);
                        studentsList.add(students);
                        studentRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
