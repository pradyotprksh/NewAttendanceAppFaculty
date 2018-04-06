package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;

public class ProctorStudentId {
    public String proctorStudentId;

    public <T extends ProctorStudentId> T withId(@NonNull final String id) {
        this.proctorStudentId = id;
        return (T) this;
    }
}
