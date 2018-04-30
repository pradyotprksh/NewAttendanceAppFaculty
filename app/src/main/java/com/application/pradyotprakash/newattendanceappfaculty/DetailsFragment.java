package com.application.pradyotprakash.newattendanceappfaculty;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private String user_id, branch;
    private FirebaseAuth mAuth;
    private String classTeacher = "false", proctor = "false", classTeacherOfValue;
    private Button subjectBtn, proctorBtn, facultySubjectsBtn;
    private TextView classTeacherOf;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        subjectBtn = view.findViewById(R.id.subjectTeacher);
        proctorBtn = view.findViewById(R.id.proctor);
        classTeacherOf = view.findViewById(R.id.classTeacherOf);
        mFirestore.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        classTeacher = task.getResult().getString("classTeacherValue");
                        proctor = task.getResult().getString("proctorValue");
                        branch = task.getResult().getString("branch");
                        if (classTeacher.equals("true")) {
                            classTeacherOfValue = task.getResult().getString("classTeacherOf");
                            classTeacherOf.setText("You are assigned as a class teacher of : " + classTeacherOfValue);
                        } else {
                            classTeacherOf.setText("You Are Not A Class Teacher");
                        }
                    }
                }
            }
        });
        classTeacherOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classTeacherOf.getText().equals("You Are Not A Class Teacher")) {
                    Intent intent = new Intent(getActivity(), ClassTeacherClassDetails.class);
                    intent.putExtra("classValue", classTeacherOfValue);
                    intent.putExtra("facultyId", user_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "You are not a class teacher.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        subjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectSemesterClassTimetable.class);
                intent.putExtra("branch", branch);
                startActivity(intent);
            }
        });
        proctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProctorStudentList.class);
                intent.putExtra("branch", branch);
                startActivity(intent);
            }
        });
        facultySubjectsBtn = view.findViewById(R.id.subjectTimetable);
        facultySubjectsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OnlyFacultySubjectDetails.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
