package com.schedule;

public class Subject {

    private String startTime;
    private String endTime;
    private String color;
    private String name;
    private String building;
    private String audition;
    private Lecturer lecturer;
    private String type;

    public Subject() {
    }

    public Subject(String startTime, String endTime, String color, String name, String building, String audition, Lecturer lecturer, String type) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
        this.name = name;
        this.building = building;
        this.audition = audition;
        this.lecturer = lecturer;
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getAudition() {
        return audition;
    }

    public void setAudition(String audition) {
        this.audition = audition;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
