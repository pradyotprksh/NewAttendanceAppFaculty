package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pradyotprakash on 08/03/18.
 */

public class TimetableClassRecyclerAdapter extends RecyclerView.Adapter<TimetableClassRecyclerAdapter.ViewHolder>{

    private List<TimetableClasses> classList;
    private Context context;

    public TimetableClassRecyclerAdapter(List<TimetableClasses> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_class_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.classValue.setText(classList.get(position).getClassValue());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classValue = classList.get(position).getClassValue();
                Intent timetableIndent = new Intent(context, FacultySubjectTeacherDetails.class);
                timetableIndent.putExtra("classValue",classValue);
                context.startActivity(timetableIndent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView classValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}
