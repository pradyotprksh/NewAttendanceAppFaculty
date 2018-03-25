package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pradyot on 16/03/18.
 */

public class StudentsStatusRecyclerAdapter extends RecyclerView.Adapter<StudentsStatusRecyclerAdapter.ViewHolder> {

    private List<StudentsStatus> statusList;
    private Context context;
    private FirebaseFirestore mFirestore, mFirestore1;
    private String currentValue;

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
    public void onBindViewHolder(final StudentsStatusRecyclerAdapter.ViewHolder holder, final int position) {
        final String statusId = statusList.get(position).statusId;
        final String studentId = StudentStatus.getStudentid();
        final String className = StudentStatus.getClassName();
        final String subject = StudentStatus.getSubject();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
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
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore1.collection("Attendance").document(className).collection(subject).document(studentId).collection(studentId).document(statusId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            currentValue = task.getResult().getString("value");
                            if (currentValue.equals("present")) {
                                Map<String, Object> newStatus = new HashMap<>();
                                newStatus.put("value", "absent");
                                mFirestore.collection("Attendance").document(className).collection(subject).document(studentId).collection(studentId).document(statusId).update(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Changed To Absent", Toast.LENGTH_SHORT).show();
                                        holder.statusValue.setText("Absent");
                                        holder.statusValue.setTextColor(Color.rgb(244, 67, 54));
                                    }
                                });
                            } else {
                                Map<String, Object> newStatus = new HashMap<>();
                                newStatus.put("value", "present");
                                mFirestore.collection("Attendance").document(className).collection(subject).document(studentId).collection(studentId).document(statusId).update(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Changed To Present", Toast.LENGTH_SHORT).show();
                                        holder.statusValue.setText("Present");
                                        holder.statusValue.setTextColor(Color.BLUE);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
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
