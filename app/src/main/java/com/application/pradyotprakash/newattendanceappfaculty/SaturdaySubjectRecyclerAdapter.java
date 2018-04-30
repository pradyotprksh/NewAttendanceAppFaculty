package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by pradyot on 14/03/18.
 */

public class SaturdaySubjectRecyclerAdapter extends RecyclerView.Adapter<SaturdaySubjectRecyclerAdapter.ViewHolder>{

    private List<MondaySubjects> subjectList;
    private Context context;
    private String user_id, takenByUserId;
    private FirebaseFirestore mFirestore4;
    private FirebaseAuth mAuth;


    public SaturdaySubjectRecyclerAdapter(List<MondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public SaturdaySubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monday_subject_list, parent, false);
        return new SaturdaySubjectRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SaturdaySubjectRecyclerAdapter.ViewHolder holder, final int position) {
        final String subject_id = subjectList.get(position).subjectId;
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore4 = FirebaseFirestore.getInstance();
        holder.subject.setText(subjectList.get(position).getSubjectName());
        String from = subjectList.get(position).getFrom();
        String to = subjectList.get(position).getTo();
        String timeValue = from + " : " + to;
        holder.time.setText(timeValue);
        holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
        takenByUserId = subjectList.get(position).getSubjectTeacher();
        mFirestore4.collection("Faculty").document(takenByUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String takenByName = task.getResult().getString("name");
                        holder.takenByValue.setText(takenByName);
                    }
                }
            }
        });
        try {
            if (takenByUserId.equals(user_id)) {
                holder.subject.setTextColor(Color.rgb(244, 67, 54));
            }
        } catch (Exception e) {
            holder.subject.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subject, time, takenByValue, subjectCode;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subject = mView.findViewById(R.id.subject_value);
            time = mView.findViewById(R.id.time_value);
            takenByValue = mView.findViewById(R.id.class_value);
            subjectCode = mView.findViewById(R.id.subject_code);
        }
    }
}
