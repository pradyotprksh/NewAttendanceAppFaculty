package com.application.pradyotprakash.newattendanceappfaculty;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeeEvents extends AppCompatActivity {

    private RecyclerView noteList;
    private List<EventList> notesList;
    private EventListRecyclerAdapter noteRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_events);
        Toolbar mToolbar = findViewById(R.id.uploadNotesToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Uploaded Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();
        noteList = findViewById(R.id.noteList);
        notesList = new ArrayList<>();
        noteRecyclerAdapter = new EventListRecyclerAdapter(notesList, getApplicationContext());
        noteList.setHasFixedSize(true);
        noteList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        noteList.setAdapter(noteRecyclerAdapter);
        notesList.clear();
        mFirestore.collection("Events").orderBy("uploadedOn").addSnapshotListener(SeeEvents.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String note_id = documentChange.getDocument().getId();
                        EventList noteList1 = documentChange.getDocument().toObject(EventList.class).withId(note_id);
                        notesList.add(noteList1);
                        noteRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(noteList.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(SeeEvents.this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        noteList.addItemDecoration(horizontalDecoration);
    }
}
