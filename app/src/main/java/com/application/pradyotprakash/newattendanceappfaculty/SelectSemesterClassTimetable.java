package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectSemesterClassTimetable extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mClassListView;
    private List<TimetableClasses> classesList;
    private TimetableClassRecyclerAdapter classRecyclerAdapter;
    private AutoCompleteTextView semesterOption;
    private ImageView semesterSpinner;
    private Button getClass;
    String branch;
    private FirebaseFirestore adminGetClassFirestore;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_semester_class_timetable);
        mToolbar = findViewById(R.id.faculty_selectsemester_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Semester");
        branch = getIntent().getStringExtra("branch");
        semesterOption = findViewById(R.id.faculty_selectclass);
        semesterSpinner = findViewById(R.id.semester_spinner);
        getClass = findViewById(R.id.get_class);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(SelectSemesterClassTimetable.this, android.R.layout.simple_dropdown_item_1line, semester);
        semesterOption.setAdapter(adapterSemester);
        semesterSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterOption.showDropDown();
            }
        });
        adminGetClassFirestore = FirebaseFirestore.getInstance();
        mClassListView = findViewById(R.id.faculty_class_list);
        classesList = new ArrayList<>();
        classRecyclerAdapter = new TimetableClassRecyclerAdapter(classesList, getApplicationContext());
        mClassListView.setHasFixedSize(true);
        mClassListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClassListView.setAdapter(classRecyclerAdapter);
        getClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classesList.clear();
                classRecyclerAdapter.notifyDataSetChanged();
                String semester = semesterOption.getText().toString();
                if (!TextUtils.isEmpty(semester)) {
                    adminGetClassFirestore.collection("Class").document(branch).collection(semester).orderBy("classValue").addSnapshotListener(SelectSemesterClassTimetable.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    TimetableClasses timetableClasses = documentChange.getDocument().toObject(TimetableClasses.class);
                                    classesList.add(timetableClasses);
                                    classRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(SelectSemesterClassTimetable.this, "Select Semester", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
