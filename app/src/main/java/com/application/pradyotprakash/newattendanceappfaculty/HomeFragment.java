package com.application.pradyotprakash.newattendanceappfaculty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static String user_id;
    private String weekDay, date;
    private RecyclerView mSubjectListView;
    private List<TodayTimetable> subjectList;
    private TodayTimetableRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;
    private TextView todayDays;

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
        weekDay = getCurrentDay();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        todayDays = view.findViewById(R.id.dateValue);
        String completeDay = weekDay + " " + date;
        todayDays.setText(completeDay);
        mSubjectListView = view.findViewById(R.id.todayClassesList);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new TodayTimetableRecyclerAdapter(subjectList, getContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(user_id).collection("Timetable").orderBy("from").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
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

    public String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "Wrong Day";
    }

}
