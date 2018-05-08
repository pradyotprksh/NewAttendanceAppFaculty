package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyMainActivity extends AppCompatActivity {

    private Toolbar faculty_main_toolbar;
    private String user_id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore facultyMainFirestore;
    private TextView facultyMainName;
    private BottomNavigationView facultyMainBottomNavigation;
    private CircleImageView facultyMainImage;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private NotesFragment notesFragment;
    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        faculty_main_toolbar = findViewById(R.id.admin_main_toolbar);
        setSupportActionBar(faculty_main_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        facultyMainFirestore = FirebaseFirestore.getInstance();
        facultyMainName = findViewById(R.id.faculty_main_name);
        facultyMainImage = findViewById(R.id.faculty_main_image);
        facultyMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSetup();
            }
        });
        faculty_main_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSetup();
            }
        });
        facultyMainName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSetup();
            }
        });
        facultyMainBottomNavigation = findViewById(R.id.facultyBottomNavigation);
        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        notesFragment = new NotesFragment();
        detailsFragment = new DetailsFragment();
        facultyMainBottomNavigation.setEnabled(false);
        facultyMainBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_home:
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.btm_notification:
                        replaceFragment(notificationFragment);
                        return true;
                    case R.id.btm_notes:
                        replaceFragment(notesFragment);
                        return true;
                    case R.id.btm_details:
                        replaceFragment(detailsFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            user_id = mAuth.getCurrentUser().getUid();
            facultyMainFirestore.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            sendToSetup();
                        } else {
                            String name = task.getResult().getString("name");
                            facultyMainName.setText("Welcome, " + name);
                            String image = task.getResult().getString("image");
                            RequestOptions placeHolderRequest = new RequestOptions();
                            placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                            Glide.with(FacultyMainActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(facultyMainImage);
                            facultyMainBottomNavigation.setEnabled(true);
                        }
                    } else {
                        String retrieving_error = task.getException().getMessage();
                        Toast.makeText(FacultyMainActivity.this, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.faculty_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_btn:
                logOut();
                return true;
            case R.id.action_setting_btn:
                sendToSetup();
                return true;
            default:
                return false;
        }
    }

    private void logOut() {
        LayoutInflater li = LayoutInflater.from(FacultyMainActivity.this);
        View promptsView = li.inflate(R.layout.prompts1, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                FacultyMainActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Map<String, Object> tokenRemoveMap = new HashMap<>();
                                tokenRemoveMap.put("token_id", FieldValue.delete());
                                facultyMainFirestore.collection("Faculty").document(user_id).update(tokenRemoveMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mAuth.signOut();
                                        sendToLogin();
                                    }
                                });
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

    private void sendToLogin() {
        Intent intent = new Intent(FacultyMainActivity.this, FacultyLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToSetup() {
        Intent intentSetup = new Intent(FacultyMainActivity.this, FacultySetupActivity.class);
        startActivity(intentSetup);
    }

    private void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.facultyMainContainer, fragment);
        fragmentTransaction.commit();
    }

}
