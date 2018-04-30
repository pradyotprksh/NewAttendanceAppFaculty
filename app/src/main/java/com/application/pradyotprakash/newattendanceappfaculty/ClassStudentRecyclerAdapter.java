package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassStudentRecyclerAdapter extends RecyclerView.Adapter<ClassStudentRecyclerAdapter.ViewHolder> {

    private List<ClassStudentList> studentsList;
    private Context context;

    public ClassStudentRecyclerAdapter(Context context, List<ClassStudentList> studentsList) {
        this.studentsList = studentsList;
        this.context = context;
    }

    @Override
    public ClassStudentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_wo_attendance_item, parent, false);
        return new ClassStudentRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClassStudentRecyclerAdapter.ViewHolder holder, final int position) {
        final String student_id = studentsList.get(position).studentId;
        String classValue = ClassTeacherClassDetails.getClassValue();
        if (classValue.equals(studentsList.get(position).getClassName())) {
            holder.mUsn.setText(studentsList.get(position).getUsn());
            CircleImageView mImageView = holder.mImage;
            Glide.with(context).load(studentsList.get(position).getImage()).into(mImageView);
        } else {
            holder.mView.setVisibility(View.INVISIBLE);
            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentDetailsProctor.class);
                intent.putExtra("studentId", student_id);
                context.startActivity(intent);
            }
        });
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