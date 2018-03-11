package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FacultyLoginActivity extends AppCompatActivity {

    private EditText loginEmailText, loginPasswordText;
    private Button loginBtn, loginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);
        loginEmailText = findViewById(R.id.login_email);
        loginPasswordText = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        loginRegBtn = findViewById(R.id.login_reg_btn);
        loginProgress = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = loginEmailText.getText().toString();
                String loginPassword = loginPasswordText.getText().toString();
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)) {
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                String current_id = mAuth.getCurrentUser().getUid();
                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id", token_id);
                                mFirestore.collection("Faculty").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sendToFingerprint();
                                    }
                                });
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(FacultyLoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegister();
            }
        });
    }

    private void sendToFingerprint() {
        Intent intentMain = new Intent(FacultyLoginActivity.this, FacultyMainActivity.class);
        intentMain.putExtra("from", "login");
        startActivity(intentMain);
        finish();
    }

    private void sendToRegister() {
        Intent intentMain = new Intent(FacultyLoginActivity.this, FacultyRegisterActivity.class);
        startActivity(intentMain);
        finish();
    }
}
