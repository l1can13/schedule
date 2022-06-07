package com.schedule;

import java.util.LinkedList;
import java.util.List;

public class DayOfWeek {

    private String nameDay;
    private List<Subject> subjects;
    private String date;

    public DayOfWeek(String name) {
        this.nameDay = name;
        this.subjects = new LinkedList<>();
    }

    public void setSubjects(Subject subject) {
        this.subjects.add(subject);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameDay() {
        return nameDay;
    }

    public void setNameDay(String nameDay) {
        this.nameDay = nameDay;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public String getDate() {
        return date;
    }
}
