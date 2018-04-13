package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EachSubjectStudentDetails extends AppCompatActivity {

    private String subjectCode, student_id, semester;
    private TextView daysAttended, currentPercentage;
    private FirebaseFirestore mFirestore, mFirestore1;
    private RecyclerView studentStatus;
    private List<StudentsStatus> studentsList;
    private StudentsStatusRecyclerAdapter studentRecyclerAdapter;
    private FirebaseFirestore mFirestore2;
    private Button marksStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_subject_student_details);
        subjectCode = getIntent().getStringExtra("subjectCode");
        student_id = getIntent().getStringExtra("studentId");
        Toolbar mToolbar = findViewById(R.id.studentSubjectToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(subjectCode + " Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        daysAttended = findViewById(R.id.daysAttended);
        currentPercentage = findViewById(R.id.currentPercentage);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore.collection("Student").document(student_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        semester = task.getResult().getString("semester");
                        mFirestore1.collection("Student").document(student_id).collection(semester).document("Attendance").collection(subjectCode).document("Details").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        try {
                                            daysAttended.setText("Days Attended: " + String.valueOf(task.getResult().getDouble("daysAttended")) + " out of "+ String.valueOf(task.getResult().getDouble("totalDays")));
                                            Double percentage = task.getResult().getDouble("percentage");
                                            if (percentage < 75.0) {
                                                currentPercentage.setText("Percentage: " + String.valueOf(percentage));
                                                currentPercentage.setTextColor(Color.rgb(244, 67, 54));
                                            } else {
                                                currentPercentage.setText("Percentage: " + String.valueOf(percentage));
                                            }
                                        } catch (Exception e) {
                                            daysAttended.setText("No Classes Till Now.");
                                            currentPercentage.setText("No Percentage");
                                        }
                                    }
                                    mFirestore = FirebaseFirestore.getInstance();
                                    studentStatus = findViewById(R.id.student_status);
                                    studentsList = new ArrayList<>();
                                    studentRecyclerAdapter = new StudentsStatusRecyclerAdapter(getApplicationContext(), studentsList);
                                    studentStatus.setHasFixedSize(true);
                                    studentStatus.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    studentStatus.setAdapter(studentRecyclerAdapter);
                                    studentsList.clear();
                                    mFirestore1.collection("Student").document(student_id).collection(semester).document("Attendance").collection(subjectCode).document("Absent").collection("Absent").orderBy("date").addSnapshotListener(EachSubjectStudentDetails.this, new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                                    String status_id = doc.getDocument().getId();
                                                    StudentsStatus students = doc.getDocument().toObject(StudentsStatus.class).withId(status_id);
                                                    studentsList.add(students);
                                                    studentRecyclerAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
        marksStatus = findViewById(R.id.marksDetails);
        marksStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachSubjectStudentDetails.this, EachSubjectStudentMarks.class);
                intent.putExtra("studentId", student_id);
                intent.putExtra("semester", semester);
                intent.putExtra("subjectCode", subjectCode);
                startActivity(intent);
            }
        });
    }
}
