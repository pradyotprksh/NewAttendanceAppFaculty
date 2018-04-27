package com.application.pradyotprakash.newattendanceappfaculty;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    private Button uploadNotes, seeNotes, uploadEvents, seeEvents;
    private FirebaseAuth mAuth;
    private static String user_id;
    private FirebaseFirestore mFirestore;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notes, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        uploadNotes = view.findViewById(R.id.uploadNotes);
        uploadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String branch = task.getResult().getString("branch");
                                Intent intent = new Intent(getContext(), SelectSemesterClassNotes.class);
                                intent.putExtra("branch", branch);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        seeNotes = view.findViewById(R.id.seeNotes);
        seeNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String branch = task.getResult().getString("branch");
                                Intent intent = new Intent(getContext(), SelectSemesterClassSeeNotes.class);
                                intent.putExtra("branch", branch);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        uploadEvents = view.findViewById(R.id.uploadEvents);
        uploadEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UploadEvents.class);
                intent.putExtra("facultyId", user_id);
                startActivity(intent);
            }
        });
        seeEvents = view.findViewById(R.id.seeEvents);
        seeEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SeeEvents.class);
                intent.putExtra("facultyId", user_id);
                startActivity(intent);
            }
        });
        return view;
    }

}
