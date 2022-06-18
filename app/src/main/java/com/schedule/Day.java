package com.schedule;

import java.util.LinkedList;
import java.util.List;

public class Day {

    private String name;
    private String date;
    private List<Subject> subjects;

    public Day() {
        subjects = new LinkedList<>();
    }

    public Day(String name, String date, List<Subject> subjects) {
        this.name = name;
        this.date = date;
        this.subjects = subjects;
    }

    public void update(List<Subject> subjects) {
        setSubjects(subjects);
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
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
