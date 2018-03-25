package com.application.pradyotprakash.newattendanceappfaculty;

/**
 * Created by pradyot on 16/03/18.
 */

public class StudentsStatus extends StatusId{

    String date;
    String time;
    String from;
    String to;
    String value;
    String weekDay;

    public StudentsStatus(String date, String time, String from, String to, String value, String weekDay) {
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
        this.value = value;
        this.weekDay = weekDay;
    }

    public StudentsStatus() {
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

