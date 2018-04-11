package com.application.pradyotprakash.newattendanceappfaculty;

/**
 * Created by pradyot on 25/03/18.
 */

public class TodayTimetable {

    private String classValue, weekDay, from, subjectName, subjectCode, to;

    public TodayTimetable() {
    }

    public TodayTimetable(String classValue, String weekDay, String from, String subjectName, String subjectCode, String to) {
        this.classValue = classValue;
        this.weekDay = weekDay;
        this.from = from;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.to = to;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
