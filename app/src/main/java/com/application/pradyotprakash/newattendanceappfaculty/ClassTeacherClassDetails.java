package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassTeacherClassDetails extends AppCompatActivity {

    private static String classValue;
    private String facultyId;
    private List<ClassStudentList> studentsList;
    private ClassStudentRecyclerAdapter studentRecyclerAdapter1;
    private FirebaseFirestore mFirestore;
    private RecyclerView mStudentListView;
    private Button sendNotification;
    private EditText finalMessage;
    private boolean isReached = false;
    private ProgressDialog progress;
    private FirebaseFirestore mFirestore1, mFirestore2;
    private int position;
    private String userId, senderName, senderImage;
    private FirebaseAuth mAuth;

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
        finalMessage = findViewById(R.id.finalMessage);
        finalMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(finalMessage.getText().length() == 50 && !isReached) {
                    finalMessage.append("\n");
                    isReached = true;
                }
                if(finalMessage.getText().length() < 10 && isReached) isReached = false;

            }
        });
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        sendNotification = findViewById(R.id.sendNotification);
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(finalMessage.getText().toString())) {
                    progress = new ProgressDialog(ClassTeacherClassDetails.this);
                    progress.setTitle("Please Wait.");
                    progress.setMessage("Sending Message To Every Student Under You.");
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    mFirestore2.collection("Faculty").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    senderName = task.getResult().getString("name");
                                    senderImage = task.getResult().getString("image");
                                    for (position = 0; position < studentsList.size(); position++) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm");
                                        String date = simpleDateFormat.format(new Date());
                                        Map<String, Object> notificationMessage = new HashMap<>();
                                        notificationMessage.put("message", finalMessage.getText().toString());
                                        notificationMessage.put("from", userId);
                                        notificationMessage.put("on", date);
                                        notificationMessage.put("designation", "Faculty");
                                        notificationMessage.put("senderName", senderName);
                                        notificationMessage.put("senderImage", senderImage);
                                        mFirestore1.collection("Student/" + studentsList.get(position).studentId + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progress.dismiss();
                                                finalMessage.setText("");
                                            }
                                        });
                                    }
                                }
                            } else {
                                String retrieving_error = task.getException().getMessage();
                                Toast.makeText(ClassTeacherClassDetails.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                } else {
                    LayoutInflater li = LayoutInflater.from(ClassTeacherClassDetails.this);
                    View promptsView = li.inflate(R.layout.prompts, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ClassTeacherClassDetails.this);
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            finalMessage.setText(userInput.getText());
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsList.clear();
        mFirestore.collection("Student").whereEqualTo("className", classValue).addSnapshotListener(ClassTeacherClassDetails.this, new EventListener<QuerySnapshot>() {
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
