package com.application.pradyotprakash.newattendanceappfaculty;

/**
 * Created by pradyot on 25/03/18.
 */

public class TodayTimetable {

    private String classValue, day, from, subject, to;


    public TodayTimetable(String classValue, String day, String from, String subject, String to) {
        this.classValue = classValue;
        this.day = day;
        this.from = from;
        this.subject = subject;
        this.to = to;
    }

    public TodayTimetable() {

    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
