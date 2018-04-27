package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;

public class EventId {

    public String eventId;

    public <T extends EventId> T withId(@NonNull final String id) {
        this.eventId = id;
        return (T) this;
    }

}
