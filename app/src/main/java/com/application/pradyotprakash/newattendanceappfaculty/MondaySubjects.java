package com.application.pradyotprakash.newattendanceappfaculty;

/**
 * Created by pradyotprakash on 08/03/18.
 */

public class MondaySubjects extends SubjectId{
    String subject, from, to, takenBy;

    public MondaySubjects(String subject, String from, String to, String takenBy) {
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.takenBy = takenBy;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MondaySubjects() {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
