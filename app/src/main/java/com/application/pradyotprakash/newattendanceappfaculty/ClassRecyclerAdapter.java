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
 * Created by pradyotprakash on 07/03/18.
 */

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.ViewHolder> {

    private List<Classes> classList;
    private Context context;
    private FirebaseFirestore mFirestore, mFirestore1, mFirestore2;
    private String user_id, classTeacherOf;
    private FirebaseAuth mAuth;

    public ClassRecyclerAdapter(List<Classes> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String classId = classList.get(position).classId;
        final String branch = FacultyClassTeacherDetails.getBranch();
        final String semester = FacultyClassTeacherDetails.getSemesterValue();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore1 = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        holder.classValue.setText(classList.get(position).getClassValue());
        mFirestore2.collection("Class").document(branch).collection(semester).document(classId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        try {
                            String faculty_id = task.getResult().getString("facultyId");
                            if (faculty_id.equals(user_id)) {
                                holder.classValue.setTextColor(Color.rgb(244, 67, 54));
                            }
                        } catch (Exception e) {
                            holder.classValue.setTextColor(Color.BLUE);
                        }
                    }
                } else {
                    String retrieving_error = task.getException().getMessage();
                    Toast.makeText(context, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        user_id = mAuth.getCurrentUser().getUid();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.setClickable(false);
                Toast.makeText(context, "Wait For A Moment.", Toast.LENGTH_SHORT).show();
                mFirestore2.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    classTeacherOf = task.getResult().getString("classTeacherOf");
                                    if (!classTeacherOf.equals("No Data Selected")) {
                                        if (!classTeacherOf.equals(classList.get(position).getClassValue())) {
                                            Toast.makeText(context, "You Are Already The Class Teacher " + classTeacherOf, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "You Are Already The Class Teacher Of This Class. Long Press To Remove it.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        mFirestore2.collection("Class").document(branch).collection(semester).document(classId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        try {
                                                            String faculty_id = task.getResult().getString("facultyId");
                                                            if (faculty_id.equals(user_id)) {
                                                                Toast.makeText(context, "You Are Already The Class Teacher Of " + classList.get(position).getClassValue() + ". Long Press To Remove it.", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Toast.makeText(context, "Someone Else Is The Class Teacher Of " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Exception e) {
                                                            Map<String, Object> classMap = new HashMap<>();
                                                            classMap.put("facultyId", user_id);
                                                            mFirestore.collection("Class").document(branch).collection(semester).document(classId).update(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Map<String, Object> classMapValue = new HashMap<>();
                                                                    classMapValue.put("classTeacherOf", classList.get(position).getClassValue());
                                                                    mFirestore1.collection("Faculty").document(user_id).update(classMapValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(context, "You Are Now The Class Teacher Of " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                                                            holder.classValue.setTextColor(Color.rgb(244, 67, 54));
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    }
                                                } else {
                                                    String retrieving_error = task.getException().getMessage();
                                                    Toast.makeText(context, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                                                }
                                                holder.mView.setClickable(true);
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    mFirestore2.collection("Class").document(branch).collection(semester).document(classId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    try {
                                                        String faculty_id = task.getResult().getString("facultyId");
                                                        if (faculty_id.equals(user_id)) {
                                                            Toast.makeText(context, "You Are Already The Class Teacher Of " + classList.get(position).getClassValue() + ". Long Press To Remove it.", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(context, "Someone Else Is The Class Teacher Of " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        Map<String, Object> classMap = new HashMap<>();
                                                        classMap.put("facultyId", user_id);
                                                        mFirestore.collection("Class").document(branch).collection(semester).document(classId).update(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Map<String, Object> classMapValue = new HashMap<>();
                                                                classMapValue.put("classTeacherOf", classList.get(position).getClassValue());
                                                                mFirestore1.collection("Faculty").document(user_id).update(classMapValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(context, "You Are Now The Class Teacher Of " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                                                        holder.classValue.setTextColor(Color.rgb(244, 67, 54));
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            } else {
                                                String retrieving_error = task.getException().getMessage();
                                                Toast.makeText(context, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                                            }
                                            holder.mView.setClickable(true);
                                        }
                                    });
                                }
                            }
                        } else {
                            String retrieving_error = task.getException().getMessage();
                            Toast.makeText(context, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.mView.setClickable(false);
                Toast.makeText(context, "Wait For A Moment.", Toast.LENGTH_SHORT).show();
                mFirestore2.collection("Faculty").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    classTeacherOf = task.getResult().getString("classTeacherOf");
                                    if (!classTeacherOf.equals(classList.get(position).getClassValue())) {
                                        Toast.makeText(context, "You Are Already The Class Teacher " + classTeacherOf + ". So You Cannot Make Changes For The Class " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        mFirestore2.collection("Class").document(branch).collection(semester).document(classId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        try {
                                                            String faculty_id = task.getResult().getString("facultyId");
                                                            if (faculty_id.equals(user_id)) {
                                                                Map<String, Object> classMap = new HashMap<>();
                                                                classMap.put("facultyId", FieldValue.delete());
                                                                mFirestore.collection("Class").document(branch).collection(semester).document(classId).update(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Map<String, Object> classMapValue = new HashMap<>();
                                                                        classMapValue.put("classTeacherOf", FieldValue.delete());
                                                                        mFirestore1.collection("Faculty").document(user_id).update(classMapValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                holder.classValue.setTextColor(Color.rgb(244, 67, 54));
                                                                                Toast.makeText(context, "You Are Now Not The Class Teacher Of " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                                                                holder.classValue.setTextColor(Color.BLUE);
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            } else {
                                                                Toast.makeText(context, "Someone Else Is The Class Teacher Of " + classList.get(position).getClassValue() + ". So You Cannot Change It.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "No One is the Class Teacher of " + classList.get(position).getClassValue() + ". So if You Want to Become the Class Teacher of " + classList.get(position).getClassValue() + "Then Tap Once", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                } else {
                                                    String retrieving_error = task.getException().getMessage();
                                                    Toast.makeText(context, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                                                }
                                                holder.mView.setClickable(true);
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, "Tap To Become The Class Teacher Of " + classList.get(position).getClassValue(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            String retrieving_error = task.getException().getMessage();
                            Toast.makeText(context, "Error: " + retrieving_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView classValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            classValue = mView.findViewById(R.id.class_value);
        }
    }
}
