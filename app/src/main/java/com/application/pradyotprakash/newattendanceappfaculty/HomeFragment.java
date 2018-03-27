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
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static String user_id;
    private RecyclerView mSubjectListView;
    private List<TodayTimetable> subjectList;
    private TodayTimetableRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mSubjectListView = view.findViewById(R.id.todayClassesList);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new TodayTimetableRecyclerAdapter(subjectList, getContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(user_id).collection("Subjects").orderBy("from").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        mSubjectListView.setVisibility(View.VISIBLE);
                        TodayTimetable subjects = doc.getDocument().toObject(TodayTimetable.class);
                        subjectList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

    public static String getUser_id() {
        return user_id;
    }
}
