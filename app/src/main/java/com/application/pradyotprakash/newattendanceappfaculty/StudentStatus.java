package com.application.pradyotprakash.newattendanceappfaculty;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class StudentStatus extends AppCompatActivity {

    private static String subject, studentid, name, className;
    private CircleImageView studentImage;
    private TextView studentName, studentUsn, studentBranch, studentSemester, studentClass, studentSubject;
    private FirebaseFirestore studentInformationFirestore;
    private Uri studentImageURI = null;
    private Button sendMessage;
    private RecyclerView studentStatus;
    private List<StudentsStatus> studentsList;
    private StudentsStatusRecyclerAdapter studentRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_status);
        subject = getIntent().getStringExtra("subject");
        studentid = getIntent().getStringExtra("studentid");
        studentStatus = findViewById(R.id.student_status);
        studentImage = findViewById(R.id.student_image);
        studentName = findViewById(R.id.student_name);
        studentUsn = findViewById(R.id.student_usn);
        studentBranch = findViewById(R.id.student_branch);
        studentSemester = findViewById(R.id.student_semester);
        studentClass = findViewById(R.id.student_class);
        sendMessage = findViewById(R.id.send_message);
        studentSubject = findViewById(R.id.student_subject);
        studentInformationFirestore = FirebaseFirestore.getInstance();
        studentInformationFirestore
                .collection("Student")
                .document(studentid)
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
                        studentSubject.setText(subject);
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        try {
                            Glide.with(StudentStatus.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(studentImage);
                        } catch (Exception e) {
                            Toast.makeText(StudentStatus.this, "...", Toast.LENGTH_SHORT).show();
                        }
                        mFirestore = FirebaseFirestore.getInstance();
                        studentsList = new ArrayList<>();
                        studentRecyclerAdapter = new StudentsStatusRecyclerAdapter(getApplicationContext(), studentsList);
                        studentStatus.setHasFixedSize(true);
                        studentStatus.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        studentStatus.setAdapter(studentRecyclerAdapter);
                        studentsList.clear();
                        mFirestore.collection("Attendance").document(className).collection(subject).document(studentid).collection(studentid).orderBy("value", Query.Direction.ASCENDING).addSnapshotListener(StudentStatus.this, new EventListener<QuerySnapshot>() {
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
                    } else {
                        Toast.makeText(StudentStatus.this, "Ask The Student To Fill In The Details.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(StudentStatus.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String getStudentid() {
        return studentid;
    }

    public static String getSubject() {
        return subject;
    }

    public static String getClassName() {
        return className;
    }
}