package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentRecyclerAdapterProctor extends RecyclerView.Adapter<StudentRecyclerAdapterProctor.ViewHolder> {

    private List<Students> studentsList;
    private Context context;
    private String classValue = FacultyProctorDetails.getClassValue();
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2, mFirestore3;
    private String proctorId;

    public StudentRecyclerAdapterProctor(Context context, List<Students> studentsList) {
        this.studentsList = studentsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_wo_attendance_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        final String student_id = studentsList.get(position).studentId;
        if (classValue.equals(studentsList.get(position).getClassName())) {
            holder.mUsn.setText(studentsList.get(position).getUsn());
            CircleImageView mImageView = holder.mImage;
            Glide.with(context).load(studentsList.get(position).getImage()).into(mImageView);
            mFirestore.collection("Student").document(student_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            try {
                                proctorId = task.getResult().getString("proctor");
                                if (proctorId.equals(userId)) {
                                    holder.mView.setBackgroundColor(Color.rgb(244, 67, 54));
                                    holder.mUsn.setTextColor(Color.WHITE);
                                }
                            } catch (Exception e) {
                                holder.mUsn.setTextColor(Color.BLACK);
                            }
                        }
                    }
                }
            });
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFirestore1.collection("Student").document(student_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    try {
                                        proctorId = task.getResult().getString("proctor");
                                        if (proctorId.equals(userId)) {
                                            Toast.makeText(context, "You are already the proctor of this student.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Someone else is already the proctor of this student.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        HashMap<String, Object> studentProctor = new HashMap<>();
                                        studentProctor.put("proctor", userId);
                                        mFirestore2.collection("Student").document(student_id).update(studentProctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                HashMap<String, Object> facultyProctor = new HashMap<>();
                                                facultyProctor.put("studentId", student_id);
                                                mFirestore3.collection("Faculty").document(userId).collection("Proctor").document(student_id).set(facultyProctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        holder.mView.setBackgroundColor(Color.rgb(244, 67, 54));
                                                        holder.mUsn.setTextColor(Color.WHITE);
                                                        Toast.makeText(context, "Student assigned to you.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
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

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = mView.findViewById(R.id.student_list_image);
            mUsn = mView.findViewById(R.id.student_list_usn);
        }
    }
}
