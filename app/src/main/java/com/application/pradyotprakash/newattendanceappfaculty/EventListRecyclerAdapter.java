package com.application.pradyotprakash.newattendanceappfaculty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class EventListRecyclerAdapter extends RecyclerView.Adapter<EventListRecyclerAdapter.ViewHolder> {

    private List<EventList> eventList;
    private Context context;
    private FirebaseAuth mAuth;
    private static String user_id;

    public EventListRecyclerAdapter(List<EventList> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @Override
    public EventListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new EventListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventListRecyclerAdapter.ViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        if (user_id.equals(eventList.get(position).getUploadedBy())) {
            holder.eventTitle.setTextColor(Color.rgb(244, 67, 54));
        }
        holder.eventTitle.setText(eventList.get(position).getTitle());
        String message = eventList.get(position).getDescription();
        if (message.length() >= 15) {
            message = message.substring(0, Math.min(message.length(), 10));
            message = message + "...";
            holder.eventDescription.setText(message);
        } else {
            holder.eventDescription.setText(eventList.get(position).getDescription());
        }
        holder.eventDescription.setText(eventList.get(position).getDescription());
        holder.eventUploadedOn.setText(eventList.get(position).getUploadedOn());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EachEventInformation.class);
                intent.putExtra("title", eventList.get(position).getTitle());
                intent.putExtra("description", eventList.get(position).getDescription());
                intent.putExtra("uploadedOn", eventList.get(position).getUploadedOn());
                intent.putExtra("uploadedBy", eventList.get(position).getUploadedBy());
                intent.putExtra("imageLink", eventList.get(position).getImageLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView eventTitle, eventDescription, eventUploadedOn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            eventTitle = mView.findViewById(R.id.eventTitle);
            eventDescription = mView.findViewById(R.id.uploadedBy);
            eventUploadedOn = mView.findViewById(R.id.eventUploadedOn);
        }
    }
}