package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FacultyWednesdaySubjectRecyclerAdapter extends RecyclerView.Adapter<FacultyWednesdaySubjectRecyclerAdapter.ViewHolder> {

    private List<FacultyMondaySubjects> subjectList;
    private Context context;


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
        if (subjectList.get(position).getWeekDay().equals("Wednesday")) {
            holder.timeValue.setText(subjectList.get(position).getFrom() + " : " + subjectList.get(position).getTo());
            holder.classValue.setText(subjectList.get(position).getClassValue());
            holder.subjectCode.setText(subjectList.get(position).getSubjectCode());
            holder.subjectValue.setText(subjectList.get(position).getSubjectName());
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
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
