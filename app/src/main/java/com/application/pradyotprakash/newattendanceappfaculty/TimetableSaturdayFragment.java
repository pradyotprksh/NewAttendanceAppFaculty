package com.application.pradyotprakash.newattendanceappfaculty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class TimetableSaturdayFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private String classValue;
    private RecyclerView mSubjectListView;
    private List<MondaySubjects> subjectList;
    private SaturdaySubjectRecyclerAdapter subjectRecyclerAdapter;

    public TimetableSaturdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable_saturday, container, false);
        if (getArguments() != null) {
            classValue = getArguments().getString("class");
        }
        mSubjectListView = view.findViewById(R.id.monday_subject);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new SaturdaySubjectRecyclerAdapter(subjectList, getContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Timetable").document(classValue).collection("Saturday").orderBy("from").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                subjectList.clear();
                subjectRecyclerAdapter.notifyDataSetChanged();
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        MondaySubjects subjects = doc.getDocument().toObject(MondaySubjects.class);
                        subjectList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

}
