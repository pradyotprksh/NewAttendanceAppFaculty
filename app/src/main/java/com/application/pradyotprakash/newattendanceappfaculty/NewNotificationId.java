package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;

public class NewNotificationId {
    public String notificationId;

    public <T extends NewNotificationId> T withId(@NonNull final String id) {
        this.notificationId = id;
        return (T) this;
    }
}
