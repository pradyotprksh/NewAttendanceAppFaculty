package com.application.pradyotprakash.newattendanceappfaculty;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static String user_id;
    private String weekDay, date;
    private RecyclerView mSubjectListView;
    private List<TodayTimetable> subjectList;
    private TodayTimetableRecyclerAdapter subjectRecyclerAdapter;
    private FirebaseFirestore mFirestore;
    private TextView todayDays;
    private List<NewNotification> newNotificationList;
    private TextView message, from, on;
    private CircleImageView mImage;
    private TextView message1, from1, on1;
    private CircleImageView mImage1;
    private ConstraintLayout firstNotification, secondNotification, newEvent;
    private TextView eventTitle, eventDescription, eventUploadedOn;
    private FirebaseFirestore mFirestore5, mFirestore6, mFirestore7;
    private List<EventList> notesList;
    private int position = 0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        weekDay = getCurrentDay();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        todayDays = view.findViewById(R.id.dateValue);
        String completeDay = weekDay + " " + date;
        todayDays.setText(completeDay);
        mSubjectListView = view.findViewById(R.id.todayClassesList);
        subjectList = new ArrayList<>();
        subjectRecyclerAdapter = new TodayTimetableRecyclerAdapter(subjectList, getContext());
        mSubjectListView.setHasFixedSize(true);
        mSubjectListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubjectListView.setAdapter(subjectRecyclerAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Faculty").document(user_id).collection("Timetable").orderBy("from").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        TodayTimetable subjects = doc.getDocument().toObject(TodayTimetable.class);
                        subjectList.add(subjects);
                        subjectRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        message = view.findViewById(R.id.message_value);
        from = view.findViewById(R.id.sender_name);
        on = view.findViewById(R.id.message_on);
        mImage = view.findViewById(R.id.sender_list_image);
        message1 = view.findViewById(R.id.message_value1);
        from1 = view.findViewById(R.id.sender_name1);
        on1 = view.findViewById(R.id.message_on1);
        mImage1 = view.findViewById(R.id.sender_list_image1);
        mFirestore5 = FirebaseFirestore.getInstance();
        newNotificationList = new ArrayList<>();
        message.setVisibility(View.INVISIBLE);
        from.setVisibility(View.INVISIBLE);
        on.setVisibility(View.INVISIBLE);
        mImage.setVisibility(View.INVISIBLE);
        message1.setVisibility(View.INVISIBLE);
        from1.setVisibility(View.INVISIBLE);
        on1.setVisibility(View.INVISIBLE);
        mImage1.setVisibility(View.INVISIBLE);
        firstNotification = view.findViewById(R.id.firstNotification);
        secondNotification = view.findViewById(R.id.secondNotification);
        mFirestore5.collection("Faculty").document(user_id).collection("Notifications").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        NewNotification notification = doc.getDocument().toObject(NewNotification.class);
                        newNotificationList.add(notification);
                    }
                }
                for (position = 0; position < 2; position++) {
                    if (position == 0) {
                        message.setVisibility(View.VISIBLE);
                        from.setVisibility(View.VISIBLE);
                        on.setVisibility(View.VISIBLE);
                        mImage.setVisibility(View.VISIBLE);
                        String messageValue = newNotificationList.get(position).getMessage();
                        if (messageValue.length() >= 15) {
                            messageValue = messageValue.substring(0, Math.min(message.length(), 10));
                            messageValue = messageValue + "...";
                            message.setText(messageValue);
                        } else {
                            message.setText(newNotificationList.get(position).getMessage());
                        }
                        from.setText(newNotificationList.get(position).getSenderName());
                        on.setText(newNotificationList.get(position).getOn());
                        CircleImageView mImageView = mImage;
                        Glide.with(getContext()).load(newNotificationList.get(position).getSenderImage()).into(mImageView);
                    }
                    if (position == 1) {
                        message1.setVisibility(View.VISIBLE);
                        from1.setVisibility(View.VISIBLE);
                        on1.setVisibility(View.VISIBLE);
                        mImage1.setVisibility(View.VISIBLE);
                        String message = newNotificationList.get(position).getMessage();
                        if (message.length() >= 15) {
                            message = message.substring(0, Math.min(message.length(), 10));
                            message = message + "...";
                            message1.setText(message);
                        } else {
                            message1.setText(newNotificationList.get(position).getMessage());
                        }
                        from1.setText(newNotificationList.get(position).getSenderName());
                        on1.setText(newNotificationList.get(position).getOn());
                        CircleImageView mImageView1 = mImage1;
                        Glide.with(getContext()).load(newNotificationList.get(position).getSenderImage()).into(mImageView1);
                    }
                }
            }
        });
        mFirestore6 = FirebaseFirestore.getInstance();
        firstNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore6.collection("Faculty").document(user_id).collection("Notifications").orderBy("on", Query.Direction.DESCENDING).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                NewNotification notification = doc.getDocument().toObject(NewNotification.class);
                                newNotificationList.add(notification);
                            }
                        }
                        for (position = 0; position < 2; position++) {
                            if (position == 0) {
                                Intent notificationIndent = new Intent(getContext(), NotificationActivity.class);
                                notificationIndent.putExtra("message", newNotificationList.get(position).getMessage());
                                notificationIndent.putExtra("from_user_id", newNotificationList.get(position).getFrom());
                                notificationIndent.putExtra("from_designation", newNotificationList.get(position).getDesignation());
                                notificationIndent.putExtra("message_on", newNotificationList.get(position).getOn());
                                startActivity(notificationIndent);
                            }
                        }
                    }
                });
            }
        });
        secondNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore6.collection("Faculty").document(user_id).collection("Notifications").orderBy("on", Query.Direction.DESCENDING).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                NewNotification notification = doc.getDocument().toObject(NewNotification.class);
                                newNotificationList.add(notification);
                            }
                        }
                        for (position = 0; position < 2; position++) {
                            if (position == 1) {
                                Intent notificationIndent = new Intent(getContext(), NotificationActivity.class);
                                notificationIndent.putExtra("message", newNotificationList.get(position).getMessage());
                                notificationIndent.putExtra("from_user_id", newNotificationList.get(position).getFrom());
                                notificationIndent.putExtra("from_designation", newNotificationList.get(position).getDesignation());
                                notificationIndent.putExtra("message_on", newNotificationList.get(position).getOn());
                                startActivity(notificationIndent);
                            }
                        }
                    }
                });
            }
        });
        newEvent = view.findViewById(R.id.newEvent);
        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.uploadedBy);
        eventUploadedOn = view.findViewById(R.id.eventUploadedOn);
        mFirestore7 = FirebaseFirestore.getInstance();
        notesList = new ArrayList<>();
        mFirestore7.collection("Events").orderBy("uploadedOn").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String note_id = documentChange.getDocument().getId();
                        EventList noteList1 = documentChange.getDocument().toObject(EventList.class).withId(note_id);
                        notesList.add(noteList1);
                    }
                }
                for (position = 0; position <= notesList.size(); position++) {
                    if (position == 0) {
                        if (notesList.get(position).getUploadedBy().equals(user_id)) {
                            eventTitle.setTextColor(Color.rgb(244, 67, 54));
                        }
                        eventTitle.setText(notesList.get(position).getTitle());
                        eventDescription.setText(notesList.get(position).getDescription());
                        eventUploadedOn.setText(notesList.get(position).getUploadedOn());
                    }
                }
            }
        });
        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore7.collection("Events").orderBy("uploadedOn").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                String note_id = documentChange.getDocument().getId();
                                EventList noteList1 = documentChange.getDocument().toObject(EventList.class).withId(note_id);
                                notesList.add(noteList1);
                            }
                        }
                        for (position = 0; position <= notesList.size(); position++) {
                            if (position == 0) {
                                Intent intent = new Intent(getContext(), SeeEvents.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        return view;
    }

    public static String getUser_id() {
        return user_id;
    }

    public String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "Wrong Day";
    }

}
