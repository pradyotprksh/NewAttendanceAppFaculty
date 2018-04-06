package com.application.pradyotprakash.newattendanceappfaculty;

public class ProctorStudent extends ProctorStudentId{

    String studentId;

    public ProctorStudent(String studentId) {
        this.studentId = studentId;
    }

    public ProctorStudent() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
