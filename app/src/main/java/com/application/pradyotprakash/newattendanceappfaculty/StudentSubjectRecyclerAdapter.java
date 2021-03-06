package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StudentSubjectRecyclerAdapter extends RecyclerView.Adapter<StudentSubjectRecyclerAdapter.ViewHolder>{

    private List<StudentSubjects> subjectList;
    private Context context;
    private String studentId = StudentDetailsProctor.getStudentId();

    public StudentSubjectRecyclerAdapter(List<StudentSubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public StudentSubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_student, parent, false);
        return new StudentSubjectRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudentSubjectRecyclerAdapter.ViewHolder holder, final int position) {
        holder.subjectValue.setText(subjectList.get(position).getSubjectName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EachSubjectStudentDetails.class);
                intent.putExtra("subjectCode", subjectList.get(position).getSubjectCode());
                intent.putExtra("studentId", studentId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subjectValue;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subjectValue = mView.findViewById(R.id.subject_value);
        }
    }
}
