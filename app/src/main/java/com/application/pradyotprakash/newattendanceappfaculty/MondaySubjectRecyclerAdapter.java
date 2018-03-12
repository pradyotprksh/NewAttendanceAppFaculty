package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pradyotprakash on 08/03/18.
 */

public class MondaySubjectRecyclerAdapter extends RecyclerView.Adapter<MondaySubjectRecyclerAdapter.ViewHolder> {

    private List<MondaySubjects> subjectList;
    private Context context;
    private String classValue, user_id;
    private FirebaseFirestore mFirestore, mFirestore2;
    private FirebaseAuth mAuth;


    public MondaySubjectRecyclerAdapter(List<MondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monday_subject_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        classValue = FacultySubjectTeacherDetails.getClassValue();
        holder.subject.setText(subjectList.get(position).getSubject());
        holder.from.setText(subjectList.get(position).getFrom());
        holder.to.setText(subjectList.get(position).getTo());
        holder.takenByValue.setText(subjectList.get(position).getTakenBy());
        final String subject_id = subjectList.get(position).subjectId;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Wait For A Moment.", Toast.LENGTH_SHORT).show();
                Map<String, Object> classMap = new HashMap<>();
                classMap.put("takenBy", user_id);
                mFirestore.collection("Timetable").document(classValue).collection("Monday").document(subject_id).update(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Subject is assigned to You.", Toast.LENGTH_SHORT).show();
                        holder.subject.setTextColor(Color.rgb(244, 67, 54));
                        holder.takenByValue.setText(user_id);
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
        private TextView subject, from, to, takenByValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subject = mView.findViewById(R.id.subject_value);
            from = mView.findViewById(R.id.from_value);
            to = mView.findViewById(R.id.to_value);
            takenByValue = mView.findViewById(R.id.taken_by_value);
        }
    }
}
