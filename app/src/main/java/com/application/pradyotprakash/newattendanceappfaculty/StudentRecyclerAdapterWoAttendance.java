package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pradyot on 26/03/18.
 */

public class StudentRecyclerAdapterWoAttendance extends RecyclerView.Adapter<StudentRecyclerAdapterWoAttendance.ViewHolder> {

    private List<Students> studentsList;
    private Context context;
    final String subjectclass = StudentAttendanceList.getSubject();

    public StudentRecyclerAdapterWoAttendance(Context context, List<Students> studentsList) {
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
        final String student_id = studentsList.get(position).studentId;
        holder.mUsn.setText(studentsList.get(position).getUsn());
        CircleImageView mImageView = holder.mImage;
        Glide.with(context).load(studentsList.get(position).getImage()).into(mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentStatus.class);
                intent.putExtra("subject", subjectclass);
                intent.putExtra("studentid", student_id);
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
