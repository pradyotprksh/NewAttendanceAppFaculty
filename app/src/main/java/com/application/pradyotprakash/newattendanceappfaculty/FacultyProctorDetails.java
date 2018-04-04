package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FacultyProctorDetails extends AppCompatActivity {

    private static String classValue;
    private RecyclerView mStudentListView;
    private List<Students> studentsList;
    private StudentRecyclerAdapterProctor studentRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_proctor_details);
        classValue = getIntent().getStringExtra("classValue");
        mStudentListView = findViewById(R.id.studentList);
        studentsList = new ArrayList<>();
        studentRecyclerAdapter = new StudentRecyclerAdapterProctor(getApplicationContext(), studentsList);
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Student").orderBy("usn").addSnapshotListener(FacultyProctorDetails.this, new EventListener<QuerySnapshot>() {
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
}
