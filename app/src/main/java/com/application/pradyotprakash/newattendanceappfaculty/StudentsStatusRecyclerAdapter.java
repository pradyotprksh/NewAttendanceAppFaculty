package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
/**
 * Created by pradyot on 16/03/18.
 */

public class StudentsStatusRecyclerAdapter extends RecyclerView.Adapter<StudentsStatusRecyclerAdapter.ViewHolder> {

    private List<StudentsStatus> statusList;
    private Context context;

    public StudentsStatusRecyclerAdapter(Context context, List<StudentsStatus> statusList) {
        this.statusList = statusList;
        this.context = context;
    }

    @Override
    public StudentsStatusRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_status_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudentsStatusRecyclerAdapter.ViewHolder holder, int position) {
        holder.dateValue.setText(statusList.get(position).getDate());
        String value = statusList.get(position).getValue();
        if (value.equals("present")) {
            holder.statusValue.setText("Present");
            holder.statusValue.setTextColor(Color.BLUE);
        } else if (value.equals("absent")) {
            holder.statusValue.setText("Absent");
            holder.statusValue.setTextColor(Color.rgb(244, 67, 54));
        }
        holder.timeValue.setText(statusList.get(position).getTime());
        String from = statusList.get(position).getFrom();
        String to = statusList.get(position).getTo();
        String classTime = from + " - " + to;
        holder.classTimeValue.setText(classTime);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView dateValue, statusValue, timeValue, classTimeValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            dateValue = mView.findViewById(R.id.dateValue);
            statusValue = mView.findViewById(R.id.statusValue);
            timeValue = mView.findViewById(R.id.timeValue);
            classTimeValue = mView.findViewById(R.id.classTimeValue);

        }
    }
}
