package com.schedule;

import java.util.List;

public class Day {

    private String name;
    private String date;
    private List<Subject> subjects;

    public Day() {
    }

    public Day(String name, String date, List<Subject> subjects) {
        this.name = name;
        this.date = date;
        this.subjects = subjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
