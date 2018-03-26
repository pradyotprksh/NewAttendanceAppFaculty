package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pradyot on 14/03/18.
 */

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.ViewHolder> {

    private List<Students> studentsList;
    private Context context;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2, mFirestore3;
    final String subjectclass = StudentAttendanceList.getSubject();
    final String classvalue = StudentAttendanceList.getClassValue();
    private double totalDays, studentDays, percentage;

    public StudentRecyclerAdapter(Context context, List<Students> studentsList) {
        this.studentsList = studentsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String whichDay = StudentAttendanceList.getWhichDay();
        final String from = StudentAttendanceList.getFrom();
        final String to = StudentAttendanceList.getTo();
        final String student_id = studentsList.get(position).studentId;
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String currentDateTimeString = sdf.format(d);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        if (classvalue.equals(studentsList.get(position).getClassName())) {
            mFirestore1.collection("Attendance").document(classvalue).collection(subjectclass).document("TotalClass").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            totalDays = task.getResult().getDouble("totalDays");
                        } else {
                            totalDays = 0.0;
                        }
                    }
                }
            });
            mFirestore3.collection("Attendance").document(classvalue).collection(subjectclass).document(student_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            studentDays = task.getResult().getDouble("daysAttended");
                        } else {
                            studentDays = 0.0;
                        }
                    }
                }
            });
            holder.mUsn.setText(studentsList.get(position).getUsn());
            CircleImageView mImageView = holder.mImage;
            Glide.with(context).load(studentsList.get(position).getImage()).into(mImageView);
            holder.present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> attendancePresent = new HashMap<>();
                    attendancePresent.put("value", "present");
                    attendancePresent.put("date", date);
                    attendancePresent.put("from", from);
                    attendancePresent.put("to", to);
                    attendancePresent.put("date", date);
                    attendancePresent.put("time", currentDateTimeString);
                    attendancePresent.put("weekDay", whichDay);
                    mFirestore.collection("Attendance").document(classvalue).collection(subjectclass).document(student_id).collection(student_id).document().set(attendancePresent).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            studentDays = studentDays + 1;
                            percentage = Double.parseDouble(String.format("%.2f", studentDays / totalDays));
                            Map<String, Object> studentAttendance = new HashMap<>();
                            studentAttendance.put("daysAttended", studentDays);
                            studentAttendance.put("percentage", percentage);
                            mFirestore2.collection("Attendance").document(classvalue).collection(subjectclass).document(student_id).update(studentAttendance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Present", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
            holder.absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> attendancePresent = new HashMap<>();
                    attendancePresent.put("value", "absent");
                    attendancePresent.put("date", date);
                    attendancePresent.put("from", from);
                    attendancePresent.put("to", to);
                    attendancePresent.put("date", date);
                    attendancePresent.put("time", currentDateTimeString);
                    attendancePresent.put("weekDay", whichDay);
                    mFirestore.collection("Attendance").document(classvalue).collection(subjectclass).document(student_id).collection(student_id).document().set(attendancePresent).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            studentDays = studentDays + 0;
                            percentage = Double.parseDouble(String.format("%.2f", studentDays / totalDays));
                            Map<String, Object> studentAttendance = new HashMap<>();
                            studentAttendance.put("daysAttended", studentDays);
                            studentAttendance.put("percentage", percentage);
                            mFirestore2.collection("Attendance").document(classvalue).collection(subjectclass).document(student_id).update(studentAttendance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Absent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
            holder.stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StudentStatus.class);
                    intent.putExtra("subject", subjectclass);
                    intent.putExtra("studentid", student_id);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView mImage;
        private TextView mUsn;
        private Button present, absent, stats;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = mView.findViewById(R.id.student_list_image);
            mUsn = mView.findViewById(R.id.student_list_usn);
            present = mView.findViewById(R.id.present);
            absent = mView.findViewById(R.id.absent);
            stats = mView.findViewById(R.id.stats);

        }
    }

}