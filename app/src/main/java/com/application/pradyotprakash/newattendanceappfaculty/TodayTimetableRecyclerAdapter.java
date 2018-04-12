package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

/**
 * Created by pradyot on 25/03/18.
 */

public class TodayTimetableRecyclerAdapter extends RecyclerView.Adapter<TodayTimetableRecyclerAdapter.ViewHolder> {

    private List<TodayTimetable> subjectList;
    private Context context;
    private String todayDay, classValue, subject, from, to, day, name;
    private String facultyid = HomeFragment.getUser_id();
    private FirebaseFirestore mFirestore;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(facultyid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("name");
                    }
                }
            }
        });
        classValue = subjectList.get(position).getClassValue();
        subject = subjectList.get(position).getSubjectName();
        from = subjectList.get(position).getFrom();
        to = subjectList.get(position).getTo();
        day = subjectList.get(position).getWeekDay();
        todayDay = getCurrentDay();
        if (todayDay.equals("Sunday")) {
            String from = subjectList.get(position).getFrom();
            String to = subjectList.get(position).getTo();
            String timing = from + " - " + to;
            holder.subject.setText(subjectList.get(position).getSubjectName());
            holder.classValue.setText(subjectList.get(position).getClassValue());
            holder.timing.setText(timing);
            holder.dayValue.setText(subjectList.get(position).getWeekDay());
        } else {
            if (subjectList.get(position).getWeekDay().equals(todayDay)) {
                String from = subjectList.get(position).getFrom();
                String to = subjectList.get(position).getTo();
                String timing = from + " - " + to;
                holder.subject.setText(subjectList.get(position).getSubjectName());
                holder.classValue.setText(subjectList.get(position).getClassValue());
                holder.timing.setText(timing);
                holder.dayValue.setVisibility(View.GONE);
            } else {
                holder.mView.setVisibility(View.INVISIBLE);
                holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentAttendanceList.class);
                intent.putExtra("classValue", classValue);
                intent.putExtra("subject", subject);
                intent.putExtra("subjectCode", subjectList.get(position).getSubjectCode());
                intent.putExtra("from", from);
                intent.putExtra("to", to);
                intent.putExtra("name", name);
                intent.putExtra("whichDay", day);
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
