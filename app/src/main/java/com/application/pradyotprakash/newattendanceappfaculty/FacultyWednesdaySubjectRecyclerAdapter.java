package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
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

public class FacultyWednesdaySubjectRecyclerAdapter extends RecyclerView.Adapter<FacultyWednesdaySubjectRecyclerAdapter.ViewHolder> {

    private List<FacultyMondaySubjects> subjectList;
    private Context context;
    private FirebaseAuth mAuth;
    private String userId, name;
    private FirebaseFirestore mFirestore;


    public FacultyWednesdaySubjectRecyclerAdapter(List<FacultyMondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public FacultyWednesdaySubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_monday_subject_list, parent, false);
        return new FacultyWednesdaySubjectRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FacultyWednesdaySubjectRecyclerAdapter.ViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("name");
                    }
                }
            }
        });
        if (subjectList.get(position).getWeekDay().equals("Wednesday")) {
            holder.timeValue.setText(subjectList.get(position).getFrom() + " : " + subjectList.get(position).getTo());
            holder.classValue.setText(subjectList.get(position).getClassValue());
            holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
            holder.subjectValue.setText(subjectList.get(position).getSubjectName());
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, StudentAttendanceList.class);
                        intent.putExtra("classValue", subjectList.get(position).getClassValue());
                        intent.putExtra("subject", subjectList.get(position).getSubjectName());
                        intent.putExtra("subjectCode", subjectList.get(position).getSubjectCode());
                        intent.putExtra("from", subjectList.get(position).getFrom());
                        intent.putExtra("to", subjectList.get(position).getTo());
                        intent.putExtra("name", name);
                        intent.putExtra("whichDay", subjectList.get(position).getWeekDay());
                        context.startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView timeValue, classValue, subjectCode, subjectValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            timeValue = mView.findViewById(R.id.time_value);
            classValue = mView.findViewById(R.id.class_value);
            subjectCode = mView.findViewById(R.id.subject_code);
            subjectValue = mView.findViewById(R.id.subject_value);
        }
    }
}
