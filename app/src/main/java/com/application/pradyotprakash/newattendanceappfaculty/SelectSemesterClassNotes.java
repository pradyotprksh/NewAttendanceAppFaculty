package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class SelectSemesterClassNotes extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mClassListView;
    private List<TimetableClasses> classesList;
    private NotesClassRecyclerAdapter classRecyclerAdapter;
    private AutoCompleteTextView semesterOption;
    private ImageView semesterSpinner;
    private Button getClass;
    private static String branch;
    private static String semesterValue;
    private FirebaseFirestore adminGetClassFirestore;
    private static final String[] semester = new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_semester_class_notes);
        mToolbar = findViewById(R.id.faculty_selectsemester_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Semester");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        branch = getIntent().getStringExtra("branch");
        semesterOption = findViewById(R.id.faculty_selectclass);
        semesterSpinner = findViewById(R.id.semester_spinner);
        getClass = findViewById(R.id.get_class);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(SelectSemesterClassNotes.this, android.R.layout.simple_dropdown_item_1line, semester);
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
        classRecyclerAdapter = new NotesClassRecyclerAdapter(classesList, getApplicationContext());
        mClassListView.setHasFixedSize(true);
        mClassListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClassListView.setAdapter(classRecyclerAdapter);
        semesterOption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                classesList.clear();
                classRecyclerAdapter.notifyDataSetChanged();
                semesterValue = semesterOption.getText().toString();
                if (!TextUtils.isEmpty(semesterValue)) {
                    adminGetClassFirestore.collection("Class").document(branch).collection(semesterValue).orderBy("classValue").addSnapshotListener(SelectSemesterClassNotes.this, new EventListener<QuerySnapshot>() {
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
                }
            }
        });
    }

    public static String getSemesterValue() {
        return semesterValue;
    }

    public static String getBranch() {
        return branch;
    }
}

