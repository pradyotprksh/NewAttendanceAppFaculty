package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pradyot on 14/03/18.
 */

public class ThursdaySubjectRecyclerAdapter extends RecyclerView.Adapter<ThursdaySubjectRecyclerAdapter.ViewHolder>{

    private List<MondaySubjects> subjectList;
    private Context context;
    private String classValue, user_id, facultyName, takenByUserId;
    private FirebaseFirestore mFirestore, mFirestore2, mFirestore3, mFirestore4, mFirestore5;
    private FirebaseAuth mAuth;


    public ThursdaySubjectRecyclerAdapter(List<MondaySubjects> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @Override
    public ThursdaySubjectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monday_subject_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        mFirestore3 = FirebaseFirestore.getInstance();
        mFirestore4 = FirebaseFirestore.getInstance();
        mFirestore5 = FirebaseFirestore.getInstance();
        classValue = FacultySubjectTeacherDetails.getClassValue();
        holder.subject.setText(subjectList.get(position).getSubject());
        holder.from.setText(subjectList.get(position).getFrom());
        holder.to.setText(subjectList.get(position).getTo());
        takenByUserId = subjectList.get(position).getTakenBy();
        final String subject_id = subjectList.get(position).subjectId;
        mFirestore4.collection("Faculty").document(takenByUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String takenByName = task.getResult().getString("name");
                        holder.takenByValue.setText(takenByName);
                    }
                }
            }
        });
        try {
            if (takenByUserId.equals(user_id)) {
                holder.subject.setTextColor(Color.rgb(244, 67, 54));
            }
        } catch (Exception e) {
            holder.subject.setTextColor(Color.BLACK);
        }
        mFirestore3.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        facultyName = task.getResult().getString("name");
                    }
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Wait For A Moment.", Toast.LENGTH_SHORT).show();
                mFirestore4.collection("Timetable").document(classValue).collection("Thursday").document(subject_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String takenBy = task.getResult().getString("takenBy");
                                try {
                                    if (takenBy.equals(user_id)) {
                                        Intent intent = new Intent(context, StudentAttendanceList.class);
                                        intent.putExtra("classValue", classValue);
                                        intent.putExtra("subject", subjectList.get(position).getSubject());
                                        intent.putExtra("from", subjectList.get(position).getFrom());
                                        intent.putExtra("to", subjectList.get(position).getTo());
                                        intent.putExtra("name", facultyName);
                                        intent.putExtra("whichDay", "Thursday");
                                        context.startActivity(intent);
                                    } else if (takenBy.equals("Not Assigned")) {
                                        Map<String, Object> classMap = new HashMap<>();
                                        classMap.put("takenBy", user_id);
                                        mFirestore.collection("Timetable").document(classValue).collection("Thursday").document(subject_id).update(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Map<String, Object> subjectMap = new HashMap<>();
                                                subjectMap.put("subject", subjectList.get(position).getSubject());
                                                subjectMap.put("to", subjectList.get(position).getTo());
                                                subjectMap.put("from", subjectList.get(position).getFrom());
                                                mFirestore2.collection("Faculty").document(user_id).collection("Subject").document(classValue).collection("Thursday").document(subject_id).set(subjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(context, "Subject is assigned to You. Long Press To Remove The Subject.", Toast.LENGTH_SHORT).show();
                                                        holder.subject.setTextColor(Color.rgb(244, 67, 54));
                                                        holder.takenByValue.setText(facultyName);
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(context, "This Subject Is Assigned To Other Faculty. You cannot make any changes.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, "No One Is Assigned To This Subject. Tap To Assign This Subject To You.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Wait For A Moment.", Toast.LENGTH_SHORT).show();
                mFirestore4.collection("Timetable").document(classValue).collection("Thursday").document(subject_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                final String takenBy = task.getResult().getString("takenBy");
                                try {
                                    if (takenBy.equals(user_id)) {
                                        Map<String, Object> classMap = new HashMap<>();
                                        classMap.put("takenBy", "Not Assigned");
                                        mFirestore.collection("Timetable").document(classValue).collection("Thursday").document(subject_id).update(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Map<String, Object> subjectMap = new HashMap<>();
                                                subjectMap.put("subject", FieldValue.delete());
                                                subjectMap.put("to", FieldValue.delete());
                                                subjectMap.put("from", FieldValue.delete());
                                                mFirestore2.collection("Faculty").document(takenBy).collection("Subject").document(classValue).collection("Tuesday").document(subject_id).update(subjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(context, "Subject is Removed.", Toast.LENGTH_SHORT).show();
                                                        holder.subject.setTextColor(Color.BLACK);
                                                        holder.takenByValue.setText("No Data");
                                                    }
                                                });
                                            }
                                        });
                                    } else if (takenBy.equals("Not Assigned")) {
                                        Toast.makeText(context, "No One Is Assigned To This Subject. Tap To Assign This Subject To You.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "This Subject Is Assigned To Other Faculty. You cannot make any changes.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, "No One Is Assigned To This Subject. Tap To Assign This Subject To You.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView subject, from, to, takenByValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            subject = mView.findViewById(R.id.subject_value);
            from = mView.findViewById(R.id.from_value);
            to = mView.findViewById(R.id.to_value);
            takenByValue = mView.findViewById(R.id.taken_by_value);
        }
    }
    
}
