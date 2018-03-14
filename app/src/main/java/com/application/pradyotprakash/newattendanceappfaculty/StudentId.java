package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;

/**
 * Created by pradyot on 14/03/18.
 */

public class StudentId {
    public String studentId;

    public <T extends StudentId> T withId(@NonNull final String id) {
        this.studentId = id;
        return (T) this;
    }
}
