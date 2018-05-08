package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryListenOptions;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FacultySeeNotes extends AppCompatActivity {

    private String semesterValue, classValue, branch;
    private FirebaseAuth mAuth;
    private static String user_id;
    private RecyclerView noteList;
    private List<NoteList> notesList;
    private NoteListRecyclerAdapter noteRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_see_notes);
        semesterValue = getIntent().getStringExtra("semesterValue");
        branch = getIntent().getStringExtra("branch");
        classValue = getIntent().getStringExtra("classValue");
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        Toolbar mToolbar = findViewById(R.id.uploadNotesToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notes List");
        mFirestore = FirebaseFirestore.getInstance();
        noteList = findViewById(R.id.noteList);
        notesList = new ArrayList<>();
        noteRecyclerAdapter = new NoteListRecyclerAdapter(notesList, getApplicationContext());
        noteList.setHasFixedSize(true);
        noteList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        noteList.setAdapter(noteRecyclerAdapter);
        notesList.clear();
        mFirestore.collection("Notes").document(branch).collection(semesterValue).document(classValue).collection("Uploaded").orderBy("uploadedOn").addSnapshotListener(FacultySeeNotes.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String note_id = documentChange.getDocument().getId();
                        NoteList noteList1 = documentChange.getDocument().toObject(NoteList.class).withId(note_id);
                        notesList.add(noteList1);
                        noteRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(noteList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(FacultySeeNotes.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        noteList.addItemDecoration(horizontalDecoration);
    }
}
