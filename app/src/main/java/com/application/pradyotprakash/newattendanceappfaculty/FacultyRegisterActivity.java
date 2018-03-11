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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class FacultyRegisterActivity extends AppCompatActivity {

    private EditText regEmailText, regPasswordText, regConfirmPasswordText;
    private Button regBtn, regLoginBtn;
    private ProgressBar regProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_register);
        regEmailText = findViewById(R.id.reg_email);
        regPasswordText = findViewById(R.id.reg_password);
        regConfirmPasswordText = findViewById(R.id.reg_confirm_password);
        regBtn = findViewById(R.id.reg_btn);
        regLoginBtn = findViewById(R.id.reg_login_btn);
        regProgress = findViewById(R.id.reg_progress);
        mAuth = FirebaseAuth.getInstance();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmailText.getText().toString().toLowerCase();
                String password = regPasswordText.getText().toString();
                String confirm = regConfirmPasswordText.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm)) {
                    if (password.equals(confirm)) {
                        regProgress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendToMain();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(FacultyRegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                                regProgress.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        Toast.makeText(FacultyRegisterActivity.this, "Password Dose Not Match", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intentMain = new Intent(FacultyRegisterActivity.this, FacultyMainActivity.class);
        startActivity(intentMain);
        finish();
    }

    private void sendToLogin() {
        Intent intentMain = new Intent(FacultyRegisterActivity.this, FacultyLoginActivity.class);
        startActivity(intentMain);
        finish();
    }

}
