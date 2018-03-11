package com.application.pradyotprakash.newattendanceappfaculty;

import android.support.annotation.NonNull;

/**
 * Created by pradyotprakash on 07/03/18.
 */

public class ClassId {
    public String classId;

    public <T extends ClassId> T withId(@NonNull final String id) {
        this.classId = id;
        return (T) this;
    }
}
