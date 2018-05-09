package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAttendanceList extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mStudentListView, mStudentListViewEithAttendance;
    private static String classValue;
    private static String subject;
    private static String from;
    private static String to;
    private static String whichDay;
    private static String subjectCode;
    private String name;
    private Button takeAttendance, notTakeAttendance, doneTakeAttendance;
    private TextView classValueText, subjectText, fromText, toText, nameText;
    private List<Students> studentsList, studentList1;
    private StudentRecyclerAdapter studentRecyclerAdapter;
    private StudentRecyclerAdapterWoAttendance studentRecyclerAdapter1;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2, mFirestore3;
    private static double totalDays = 0.0;
    private String user_id;
    private FirebaseAuth mAuth;

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
        whichDay = getIntent().getStringExtra("whichDay");
        subjectCode = getIntent().getStringExtra("subjectCode");
        classValueText = findViewById(R.id.currentClassValue);
        subjectText = findViewById(R.id.subjectValue);
        fromText = findViewById(R.id.fromValue);
        toText = findViewById(R.id.toValue);
        nameText = findViewById(R.id.nameValue);
        takeAttendance = findViewById(R.id.takeAttendance);
        notTakeAttendance = findViewById(R.id.notTakeAttendance);
        doneTakeAttendance = findViewById(R.id.doneTakeAttendance);
        classValueText.setText(classValue);
        subjectText.setText(subject);
        fromText.setText(from);
        toText.setText(to);
        nameText.setText(name);
        mStudentListViewEithAttendance = findViewById(R.id.studentListWithAttendance);
        mFirestore = FirebaseFirestore.getInstance();
        studentsList = new ArrayList<>();
        studentsList.clear();
        studentRecyclerAdapter = new StudentRecyclerAdapter(getApplicationContext(), studentsList);
        mStudentListViewEithAttendance.setHasFixedSize(true);
        mStudentListViewEithAttendance.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListViewEithAttendance.setAdapter(studentRecyclerAdapter);
        mStudentListViewEithAttendance.setVisibility(View.INVISIBLE);
        mStudentListView = findViewById(R.id.studentList);
        mFirestore1 = FirebaseFirestore.getInstance();
        studentList1 = new ArrayList<>();
        studentList1.clear();
        studentRecyclerAdapter1 = new StudentRecyclerAdapterWoAttendance(getApplicationContext(), studentsList);
        mStudentListView.setHasFixedSize(true);
        mStudentListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStudentListView.setAdapter(studentRecyclerAdapter1);
        notTakeAttendance.setVisibility(View.INVISIBLE);
        doneTakeAttendance.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    totalDays = task.getResult().getDouble("totalDays");
                                    totalDays = totalDays + 1;
                                    Map<String, Object> totalClass = new HashMap<>();
                                    totalClass.put("totalDays", totalDays);
                                    mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).update(totalClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mStudentListViewEithAttendance.setVisibility(View.VISIBLE);
                                            mStudentListView.setVisibility(View.INVISIBLE);
                                            takeAttendance.setVisibility(View.INVISIBLE);
                                            notTakeAttendance.setVisibility(View.VISIBLE);
                                            doneTakeAttendance.setVisibility(View.VISIBLE);
                                        }
                                    });
                                } catch (Exception e) {
                                    totalDays = 0.0;
                                    totalDays = totalDays + 1;
                                    Map<String, Object> totalClass = new HashMap<>();
                                    totalClass.put("totalDays", totalDays);
                                    mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).update(totalClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mStudentListViewEithAttendance.setVisibility(View.VISIBLE);
                                            mStudentListView.setVisibility(View.INVISIBLE);
                                            takeAttendance.setVisibility(View.INVISIBLE);
                                            notTakeAttendance.setVisibility(View.VISIBLE);
                                            doneTakeAttendance.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            } else {
                                totalDays = 0.0;
                                totalDays = totalDays + 1;
                                Map<String, Object> totalClass = new HashMap<>();
                                totalClass.put("totalDays", totalDays);
                                mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).update(totalClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mStudentListViewEithAttendance.setVisibility(View.VISIBLE);
                                        mStudentListView.setVisibility(View.INVISIBLE);
                                        takeAttendance.setVisibility(View.INVISIBLE);
                                        notTakeAttendance.setVisibility(View.VISIBLE);
                                        doneTakeAttendance.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        notTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    totalDays = task.getResult().getDouble("totalDays");
                                } catch (Exception e) {
                                    totalDays = 0.0;
                                }
                            } else {
                                totalDays = 0.0;
                            }
                        }
                    }
                });
                totalDays = totalDays - 1;
                Map<String, Object> totalClass = new HashMap<>();
                totalClass.put("totalDays", totalDays);
                mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).update(totalClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mStudentListViewEithAttendance.setVisibility(View.INVISIBLE);
                        mStudentListView.setVisibility(View.VISIBLE);
                        takeAttendance.setVisibility(View.VISIBLE);
                        notTakeAttendance.setVisibility(View.INVISIBLE);
                        doneTakeAttendance.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        doneTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudentListViewEithAttendance.setVisibility(View.INVISIBLE);
                mStudentListView.setVisibility(View.VISIBLE);
                takeAttendance.setVisibility(View.VISIBLE);
                notTakeAttendance.setVisibility(View.INVISIBLE);
                doneTakeAttendance.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirestore1.collection("Student").whereEqualTo("className", classValue).addSnapshotListener(StudentAttendanceList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                studentList1.clear();
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String student_id = doc.getDocument().getId();
                        Students students = doc.getDocument().toObject(Students.class).withId(student_id);
                        studentList1.add(students);
                        studentRecyclerAdapter1.notifyDataSetChanged();
                    }
                }
            }
        });
        mFirestore.collection("Student").whereEqualTo("className", classValue).addSnapshotListener(StudentAttendanceList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                studentsList.clear();
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

    public static String getWhichDay() {
        return whichDay;
    }

    public static String getSubjectCode() {
        return subjectCode;
    }

    public static double getTotalDays() {
        return totalDays;
    }
}
