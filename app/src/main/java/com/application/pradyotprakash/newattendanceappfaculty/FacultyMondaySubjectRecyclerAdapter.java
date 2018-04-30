package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FacultyMondaySubjectRecyclerAdapter extends RecyclerView.Adapter<FacultyMondaySubjectRecyclerAdapter.ViewHolder> {

    private List<FacultyMondaySubjects> subjectList;
    private Context context;


    public FacultyMondaySubjectRecyclerAdapter(List<FacultyMondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public FacultyMondaySubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_monday_subject_list, parent, false);
        return new FacultyMondaySubjectRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FacultyMondaySubjectRecyclerAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
    }
}
