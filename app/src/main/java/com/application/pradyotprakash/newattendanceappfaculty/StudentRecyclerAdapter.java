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
import com.google.firebase.auth.FirebaseAuth;
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
    final String subjectCode = StudentAttendanceList.getSubjectCode();
    final String classvalue = StudentAttendanceList.getClassValue();
    private double totalDays, studentDays, percentage;
    private String user_id;
    private FirebaseAuth mAuth;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore2 = FirebaseFirestore.getInstance();
        if (classvalue.equals(studentsList.get(position).getClassName())) {
            mFirestore3.collection("Student").document(student_id).collection(studentsList.get(position).getSemester()).document("Attendance").collection(subjectCode).document("Details").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            try {
                                studentDays = task.getResult().getDouble("daysAttended");
                            } catch (Exception e) {
                                studentDays = 0.0;
                            }
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
                    mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    totalDays = task.getResult().getDouble("totalDays");
                                    studentDays = studentDays + 1;
                                    percentage = (studentDays / totalDays) * 100;
                                    HashMap<String, Object> presentMap = new HashMap<>();
                                    presentMap.put("totalDays", totalDays);
                                    presentMap.put("daysAttended", studentDays);
                                    presentMap.put("percentage", percentage);
                                    mFirestore.collection("Student").document(student_id).collection(studentsList.get(position).getSemester()).document("Attendance").collection(subjectCode).document("Details").set(presentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Present", Toast.LENGTH_SHORT).show();
                                            holder.absent.setEnabled(false);
                                            holder.present.setEnabled(false);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            });
            holder.absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFirestore2.collection("Faculty").document(user_id).collection("Subjects").document(subjectCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    totalDays = task.getResult().getDouble("totalDays");
                                    studentDays = studentDays + 0;
                                    percentage = (studentDays / totalDays) * 100;
                                    HashMap<String, Object> presentMap = new HashMap<>();
                                    presentMap.put("totalDays", totalDays);
                                    presentMap.put("daysAttended", studentDays);
                                    presentMap.put("percentage", percentage);
                                    mFirestore.collection("Student").document(student_id).collection(studentsList.get(position).getSemester()).document("Attendance").collection(subjectCode).document("Details").set(presentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            HashMap<String, Object> absentMap = new HashMap<>();
                                            absentMap.put("weekDay", whichDay);
                                            absentMap.put("from", from);
                                            absentMap.put("to", to);
                                            absentMap.put("date", date);
                                            absentMap.put("time", currentDateTimeString);
                                            absentMap.put("value", "Absent");
                                            absentMap.put("semester", studentsList.get(position).getSemester());
                                            mFirestore1.collection("Student").document(student_id).collection(studentsList.get(position).getSemester()).document("Attendance").collection(subjectCode).document("Absent").collection("Absent").document().set(absentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Present", Toast.LENGTH_SHORT).show();
                                                    holder.present.setEnabled(false);
                                                    holder.absent.setEnabled(false);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
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
        private Button present, absent;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = mView.findViewById(R.id.student_list_image);
            mUsn = mView.findViewById(R.id.student_list_usn);
            present = mView.findViewById(R.id.present);
            absent = mView.findViewById(R.id.absent);
        }
    }

}