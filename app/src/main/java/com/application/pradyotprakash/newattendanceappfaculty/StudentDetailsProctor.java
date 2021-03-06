package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDetailsProctor extends AppCompatActivity {

    private CircleImageView studentImage;
    private TextView studentName, studentUsn, studentBranch, studentSemester, studentProctor, studentClass, studentClassTeacher;
    private Uri studentImageURI = null;
    private String name, className, proctorId, proctorName, branch, semester;
    private static String studentId;
    private FirebaseFirestore studentInformationFirestore, proctorInformationFirestore, mFirestore, mFirestore1, mFirestore2;
    private RecyclerView mSubjectListView;
    private List<StudentSubjects> subjectList;
    private StudentSubjectRecyclerAdapter subjectRecyclerAdapter;
    private Button sendMessage;
    private Button otherSemesterDetails, checkValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_proctor);
        studentId = getIntent().getStringExtra("studentId");
        studentImage = findViewById(R.id.student_image);
        studentName = findViewById(R.id.student_name);
        studentUsn = findViewById(R.id.student_usn);
        studentBranch = findViewById(R.id.student_branch);
        studentSemester = findViewById(R.id.student_semester);
        studentProctor = findViewById(R.id.student_proctor);
        studentClass = findViewById(R.id.student_class);
        studentClassTeacher = findViewById(R.id.student_class_teacher);
        studentInformationFirestore = FirebaseFirestore.getInstance();
        proctorInformationFirestore = FirebaseFirestore.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mSubjectListView = findViewById(R.id.subjectList);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new StudentSubjectRecyclerAdapter(subjectList, StudentDetailsProctor.this);
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(StudentDetailsProctor.this));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore2 = FirebaseFirestore.getInstance();
        studentInformationFirestore.collection("Student").document(studentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("name");
                        String usn = task.getResult().getString("usn");
                        branch = task.getResult().getString("branch");
                        className = task.getResult().getString("className");
                        semester = task.getResult().getString("semester");
                        String image = task.getResult().getString("image");
                        studentImageURI = Uri.parse(image);
                        studentName.setText(name);
                        studentUsn.setText(usn);
                        studentBranch.setText(branch);
                        studentSemester.setText(semester);
                        studentClass.setText(className);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        try {
                            Glide.with(StudentDetailsProctor.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(studentImage);
                        } catch (Exception e) {
                            Toast.makeText(StudentDetailsProctor.this, "...", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            proctorId = task.getResult().getString("proctor");
                            proctorInformationFirestore.collection("Faculty").document(proctorId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            proctorName = task.getResult().getString("name");
                                            studentProctor.setText("Proctor Name: " + proctorName);
                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            studentProctor.setText("Proctor Not Assigned Yet");
                        }
                        mFirestore.collection("Class").document(branch).collection(semester).document(className).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String classTeacherId = task.getResult().getString("classTeacher");
                                        mFirestore1.collection("Faculty").document(classTeacherId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        String classTeacher = task.getResult().getString("name");
                                                        studentClassTeacher.setText("Class Teacher: " + classTeacher);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        mFirestore2.collection("Subject").document(branch).collection(semester).addSnapshotListener(StudentDetailsProctor.this, new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                subjectRecyclerAdapter.notifyDataSetChanged();
                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        StudentSubjects subjects = doc.getDocument().toObject(StudentSubjects.class);
                                        subjectList.add(subjects);
                                        subjectRecyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(StudentDetailsProctor.this, "Ask The Student To Fill In The Details.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(StudentDetailsProctor.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mSubjectListView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(StudentDetailsProctor.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mSubjectListView.addItemDecoration(horizontalDecoration);
        sendMessage = findViewById(R.id.send_message);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDetailsProctor.this, AdminEachStudentNotification.class);
                intent.putExtra("student_id", studentId);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        otherSemesterDetails = findViewById(R.id.other_semester_details);
        otherSemesterDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (semester.equals("Semester 1")) {
                    Toast.makeText(StudentDetailsProctor.this, "Student is in the first semester.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(StudentDetailsProctor.this, OtherSemesterDetails.class);
                    intent.putExtra("studentId", studentId);
                    intent.putExtra("currentSemester", semester);
                    intent.putExtra("branch", branch);
                    startActivity(intent);
                }
            }
        });
        checkValues = findViewById(R.id.changeValues);
        checkValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDetailsProctor.this, ChangeStudentValuesProctor.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("semester", semester);
                startActivity(intent);
            }
        });
    }

    public static String getStudentId() {
        return studentId;
    }
}
