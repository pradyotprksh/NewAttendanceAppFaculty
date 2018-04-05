package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentRecyclerAdapterProctor extends RecyclerView.Adapter<StudentRecyclerAdapterProctor.ViewHolder> {

    private List<Students> studentsList;
    private Context context;
    private String classValue = FacultyProctorDetails.getClassValue();
    private String userId = HomeFragment.getUser_id();
    private FirebaseFirestore mFirestore;

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
        mFirestore = FirebaseFirestore.getInstance();
        final String student_id = studentsList.get(position).studentId;
        if (classValue.equals(studentsList.get(position).getClassName())) {
            holder.mUsn.setText(studentsList.get(position).getUsn());
            CircleImageView mImageView = holder.mImage;
            Glide.with(context).load(studentsList.get(position).getImage()).into(mImageView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> student = new HashMap<>();
                    student.put("student_id", student_id);
                    mFirestore.collection("Faculty").document(userId).collection("Proctor").document(classValue).collection(classValue).document().set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"This student is assigned to you.", Toast.LENGTH_SHORT).show();
                            holder.mView.setBackgroundColor(Color.rgb(244, 67, 54));
                            holder.mUsn.setTextColor(Color.WHITE);
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
