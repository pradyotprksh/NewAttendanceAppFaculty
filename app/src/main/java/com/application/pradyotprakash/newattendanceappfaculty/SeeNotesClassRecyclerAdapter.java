package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SeeNotesClassRecyclerAdapter extends RecyclerView.Adapter<SeeNotesClassRecyclerAdapter.ViewHolder> {

    private List<TimetableClasses> classList;
    private Context context;
    private String semesterValue, branch;

    public SeeNotesClassRecyclerAdapter(List<TimetableClasses> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public SeeNotesClassRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_class_list_item, parent, false);
        return new SeeNotesClassRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeeNotesClassRecyclerAdapter.ViewHolder holder, final int position) {
        holder.classValue.setText(classList.get(position).getClassValue());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterValue = SelectSemesterClassNotes.getSemesterValue();
                branch = SelectSemesterClassNotes.getBranch();
                String classValue = classList.get(position).getClassValue();
                Intent intent = new Intent(context, FacultySeeNotes.class);
                intent.putExtra("branch", branch);
                intent.putExtra("semesterValue", semesterValue);
                intent.putExtra("classValue", classValue);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView classValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}

