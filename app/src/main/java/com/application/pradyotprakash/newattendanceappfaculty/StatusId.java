package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;

/**
 * Created by pradyot on 16/03/18.
 */

public class StatusId {
    public String statusId;

    public <T extends StatusId> T withId(@NonNull final String id) {
        this.statusId = id;
        return (T) this;
    }
}
