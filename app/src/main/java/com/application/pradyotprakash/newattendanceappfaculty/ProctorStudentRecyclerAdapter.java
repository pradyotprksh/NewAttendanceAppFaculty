package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProctorStudentRecyclerAdapter extends RecyclerView.Adapter<ProctorStudentRecyclerAdapter.ViewHolder>{

    private List<ProctorStudent> studentsList;
    private Context context;
    private FirebaseFirestore mFirestore;
    private String studentId, usn, image;

    public ProctorStudentRecyclerAdapter(List<ProctorStudent> studentsList, Context context) {
        this.studentsList = studentsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proctor_student_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mFirestore = FirebaseFirestore.getInstance();
        studentId = studentsList.get(position).getStudentId();
        mFirestore.collection("Student").document(studentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        usn = task.getResult().getString("usn");
                        image = task.getResult().getString("image");
                        RequestOptions placeHolderRequest = new RequestOptions();
                        placeHolderRequest.placeholder(R.mipmap.default_profile_picture);
                        Glide.with(context).setDefaultRequestOptions(placeHolderRequest).load(image).into(holder.mImage);
                        holder.mUsn.setText(usn);
                    }
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
