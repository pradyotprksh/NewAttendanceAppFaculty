package com.application.pradyotprakash.newattendanceappfaculty;

import android.app.ProgressDialog;
import android.content.Context;
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

public class ProctorStatusRecyclerAdapter extends RecyclerView.Adapter<ProctorStatusRecyclerAdapter.ViewHolder> {

    private List<StudentsStatus> statusList;
    private Context context;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2;
    private String currentValue;
    private ProgressDialog progress;

    public ProctorStatusRecyclerAdapter(Context context, List<StudentsStatus> statusList) {
        this.statusList = statusList;
        this.context = context;
    }

    @Override
    public ProctorStatusRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_status_list, parent, false);
        return new ProctorStatusRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProctorStatusRecyclerAdapter.ViewHolder holder, final int position) {
        final String statusId = statusList.get(position).statusId;
        final String studentId = EachSubjectStudentDetails.getStudent_id();
        final String subject = EachSubjectStudentDetails.getSubjectCode();
        final String semester = EachSubjectStudentDetails.getSemester();
        String weekDay = statusList.get(position).getWeekDay();
        String date = statusList.get(position).getDate();
        String day = date + " " + weekDay;
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        holder.dateValue.setText(day);
        if (statusList.get(position).getValue().equals("Present")) {
            holder.statusValue.setText("Present");
            holder.statusValue.setTextColor(Color.BLUE);
        } else {
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
                if (statusList.get(position).getValue().equals("Absent")) {
                    HashMap<String, Object> newStatus = new HashMap<>();
                    newStatus.put("value", "Present");
                    mFirestore.collection("Student").document(studentId).collection(semester).document("Attendance").collection(subject).document("Absent").collection("Absent").document(statusId).update(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFirestore2.collection("Student").document(studentId).collection(semester).document("Attendance").collection(subject).document("Details").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            Double daysAttended = task.getResult().getDouble("daysAttended");
                                            Double totalDays = task.getResult().getDouble("totalDays");
                                            daysAttended = daysAttended + 1;
                                            Double percentage = (daysAttended/totalDays) * 100;
                                            HashMap<String, Object> newValues = new HashMap<>();
                                            newValues.put("daysAttended", daysAttended);
                                            newValues.put("percentage", percentage);
                                            newValues.put("totalDays", totalDays);
                                            mFirestore1.collection("Student").document(studentId).collection(semester).document("Attendance").collection(subject).document("Details").update(newValues).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                } else {
                    HashMap<String, Object> newStatus = new HashMap<>();
                    newStatus.put("value", "Absent");
                    mFirestore.collection("Student").document(studentId).collection(semester).document("Attendance").collection(subject).document("Absent").collection("Absent").document(statusId).update(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFirestore2.collection("Student").document(studentId).collection(semester).document("Attendance").collection(subject).document("Details").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            Double daysAttended = task.getResult().getDouble("daysAttended");
                                            Double totalDays = task.getResult().getDouble("totalDays");
                                            daysAttended = daysAttended - 1;
                                            Double percentage = (daysAttended/totalDays) * 100;
                                            HashMap<String, Object> newValues = new HashMap<>();
                                            newValues.put("daysAttended", daysAttended);
                                            newValues.put("percentage", percentage);
                                            newValues.put("totalDays", totalDays);
                                            mFirestore1.collection("Student").document(studentId).collection(semester).document("Attendance").collection(subject).document("Details").update(newValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context, "Changed To Absent", Toast.LENGTH_SHORT).show();
                                                    holder.statusValue.setText("Absent");
                                                    holder.statusValue.setTextColor(Color.rgb(244, 67, 54));
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
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
