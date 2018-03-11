package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FacultyClassTeacherDetails extends AppCompatActivity {

    public static String branch;
    public static String semesterValue;
    private TextView branchTitle;
    private Toolbar faculty_classteacherdetails_toolbar;
    private RecyclerView mClassListView;
    private List<Classes> classesList;
    private ClassRecyclerAdapter classRecyclerAdapter;
    private AutoCompleteTextView semesterOption;
    private ImageView semesterSpinner;
    private Button getClass;
    private FirebaseFirestore facultyGetClassFirestore;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_class_teacher_details);
        faculty_classteacherdetails_toolbar = findViewById(R.id.faculty_classteacherclass_toolbar);
        setSupportActionBar(faculty_classteacherdetails_toolbar);
        getSupportActionBar().setTitle("Class Teacher Details");
        branch = getIntent().getStringExtra("branch");
        branchTitle = findViewById(R.id.branchValue);
        branchTitle.setText(branch);
        semesterOption = findViewById(R.id.faculty_selectclass);
        semesterSpinner = findViewById(R.id.semester_spinner);
        getClass = findViewById(R.id.get_class);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(FacultyClassTeacherDetails.this, android.R.layout.simple_dropdown_item_1line, semester);
        semesterOption.setAdapter(adapterSemester);
        semesterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterOption.showDropDown();
            }
        });
        facultyGetClassFirestore = FirebaseFirestore.getInstance();
        mClassListView = findViewById(R.id.faculty_class_list);
        classesList = new ArrayList<>();
        classRecyclerAdapter = new ClassRecyclerAdapter(classesList, getApplicationContext());
        mClassListView.setHasFixedSize(true);
        mClassListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClassListView.setAdapter(classRecyclerAdapter);
        getClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classesList.clear();
                classRecyclerAdapter.notifyDataSetChanged();
                semesterValue = semesterOption.getText().toString();
                if (!TextUtils.isEmpty(semesterValue)) {
                    facultyGetClassFirestore.collection("Class").document(branch).collection(semesterValue).orderBy("classValue").addSnapshotListener(FacultyClassTeacherDetails.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String class_id = doc.getDocument().getId();
                                    Classes classes = doc.getDocument().toObject(Classes.class).withId(class_id);
                                    classesList.add(classes);
                                    classRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(FacultyClassTeacherDetails.this, "Select Semester", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static String getBranch() {
        return branch;
    }

    public static String getSemesterValue() {
        return semesterValue;
    }
}
