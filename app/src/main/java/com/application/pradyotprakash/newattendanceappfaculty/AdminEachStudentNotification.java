package com.application.pradyotprakash.newattendanceappfaculty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminEachStudentNotification extends AppCompatActivity {

    private TextView label;
    private EditText message;
    private Button sendBtn;
    private String student_id, name, user_id, senderName, senderImage;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressBar mProgressbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore, adminMainFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_each_student_notification);
        student_id = getIntent().getStringExtra("student_id");
        name = getIntent().getStringExtra("name");
        mToolbar = findViewById(R.id.adminStudentNotifyToolbarSingle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Send Notification");
        label = findViewById(R.id.labelSingle);
        message = findViewById(R.id.studentMessageSingle);
        sendBtn = findViewById(R.id.sendToStudentSingle);
        mProgressbar = findViewById(R.id.sendingNotifBarSingle);
        mProgressbar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        label.setText("Send Notification To " + name);
        adminMainFirestore = FirebaseFirestore.getInstance();
        adminMainFirestore.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        senderName = task.getResult().getString("name");
                        senderImage = task.getResult().getString("image");
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(AdminEachStudentNotification.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mFirestore = FirebaseFirestore.getInstance();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = message.getText().toString();
                if (!TextUtils.isEmpty(value)) {
                    mProgressbar.setVisibility(View.VISIBLE);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm");
                    String date = simpleDateFormat.format(new Date());
                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", value);
                    notificationMessage.put("from", user_id);
                    notificationMessage.put("on", date);
                    notificationMessage.put("designation", "Faculty");
                    notificationMessage.put("senderName", senderName);
                    notificationMessage.put("senderImage", senderImage);
                    mFirestore.collection("Student/" + student_id + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AdminEachStudentNotification.this, "Notification Sent.", Toast.LENGTH_SHORT).show();
                            message.setText("");
                            mProgressbar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminEachStudentNotification.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressbar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(AdminEachStudentNotification.this, "Write A Message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
