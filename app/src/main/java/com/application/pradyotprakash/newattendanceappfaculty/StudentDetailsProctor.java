package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDetailsProctor extends AppCompatActivity {

    private CircleImageView studentImage;
    private TextView studentName, studentUsn, studentBranch, studentSemester, studentClass, studentProctor;
    private Uri studentImageURI = null;
    private String studentId, name, className, proctorId, proctorName;
    private FirebaseFirestore studentInformationFirestore, proctorInformationFirestore;

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
        studentClass = findViewById(R.id.student_class);
        studentProctor = findViewById(R.id.student_proctor);
        studentInformationFirestore = FirebaseFirestore.getInstance();
        proctorInformationFirestore = FirebaseFirestore.getInstance();
        studentInformationFirestore
                .collection("Student")
                .document(studentId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("name");
                        String usn = task.getResult().getString("usn");
                        String branch = task.getResult().getString("branch");
                        className = task.getResult().getString("className");
                        String semester = task.getResult().getString("semester");
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
                            proctorInformationFirestore
                                    .collection("Faculty")
                                    .document(proctorId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            proctorName = task.getResult().getString("name");
                                            studentProctor.setText(proctorName);
                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            studentProctor.setText("Proctor Not Assigned Yet");
                        }
                    } else {
                        Toast.makeText(StudentDetailsProctor.this, "Ask The Student To Fill In The Details.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(StudentDetailsProctor.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
