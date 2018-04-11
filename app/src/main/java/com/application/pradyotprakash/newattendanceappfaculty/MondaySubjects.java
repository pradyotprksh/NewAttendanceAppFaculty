package com.application.pradyotprakash.newattendanceappfaculty;

/**
 * Created by pradyotprakash on 08/03/18.
 */

public class MondaySubjects extends SubjectId{
    String from, subjectCode, subjectName, subjectTeacher, to, weekDay;

    public MondaySubjects() {
    }

    public MondaySubjects(String from, String subjectCode, String subjectName, String subjectTeacher, String to, String weekDay) {
        this.from = from;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectTeacher = subjectTeacher;
        this.to = to;
        this.weekDay = weekDay;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
}
