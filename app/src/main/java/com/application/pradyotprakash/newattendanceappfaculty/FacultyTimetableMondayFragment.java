package com.application.pradyotprakash.newattendanceappfaculty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyTimetableMondayFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private String user_id;
    private FirebaseAuth mAuth;
    private RecyclerView mSubjectListView;
    private List<FacultyMondaySubjects> subjectList;
    private FacultyMondaySubjectRecyclerAdapter subjectRecyclerAdapter;

    public FacultyTimetableMondayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faculty_timetable_monday, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mSubjectListView = view.findViewById(R.id.monday_subject);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new FacultyMondaySubjectRecyclerAdapter(subjectList, getContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(user_id).collection("Timetable").orderBy("from").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                subjectRecyclerAdapter.notifyDataSetChanged();
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String subject_id = doc.getDocument().getId();
                        FacultyMondaySubjects subjects = doc.getDocument().toObject(FacultyMondaySubjects.class).withId(subject_id);
                        subjectList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

}
