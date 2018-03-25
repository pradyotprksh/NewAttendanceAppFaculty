package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by pradyot on 25/03/18.
 */

public class TodayTimetableRecyclerAdapter extends RecyclerView.Adapter<TodayTimetableRecyclerAdapter.ViewHolder> {

    private List<TodayTimetable> subjectList;
    private Context context;
    private String todayDay;

    public TodayTimetableRecyclerAdapter(List<TodayTimetable> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_timetable_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        todayDay = getCurrentDay();
        if (todayDay.equals("Sunday")) {
            String from = subjectList.get(position).getFrom();
            String to = subjectList.get(position).getTo();
            String timing = from + " - " + to;
            holder.subject.setText(subjectList.get(position).getSubject());
            holder.classValue.setText(subjectList.get(position).getClassValue());
            holder.timing.setText(timing);
            holder.dayValue.setText(subjectList.get(position).getDay());
        } else {
            if (subjectList.get(position).getDay().equals(todayDay)) {
                String from = subjectList.get(position).getFrom();
                String to = subjectList.get(position).getTo();
                String timing = from + " - " + to;
                holder.subject.setText(subjectList.get(position).getSubject());
                holder.classValue.setText(subjectList.get(position).getClassValue());
                holder.timing.setText(timing);
                holder.dayValue.setVisibility(View.GONE);
            } else {
                holder.mView.setVisibility(View.INVISIBLE);
                holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subject, timing, classValue, dayValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subject = mView.findViewById(R.id.subjectName);
            classValue = mView.findViewById(R.id.classValue);
            timing = mView.findViewById(R.id.timing);
            dayValue = mView.findViewById(R.id.dayValue);
        }
    }

    public String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "Wrong Day";
    }

}
