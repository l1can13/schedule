package com.schedule;

public class Subject {

    private String nameOfSubject;
    private String building;
    private int audition;
    private String nameOfProfessor;
    private String typeOfLesson;
    private String begin;
    private String end;

    public Subject(String name) {
        this.nameOfSubject = name;
    }

    public void setNameOfSubject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setAudition(int audition) {
        this.audition = audition;
    }

    public void setNameOfProfessor(String nameOfProfessor) {
        this.nameOfProfessor = nameOfProfessor;
    }

    public void setTypeOfLesson(String typeOfLesson) {
        this.typeOfLesson = typeOfLesson;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getBuilding() {
        return building;
    }

    public int getAudition() {
        return audition;
    }

    public String getNameOfProfessor() {
        return nameOfProfessor;
    }

    public String getTypeOfLesson() {
        return typeOfLesson;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getNameOfSubject() {
        return nameOfSubject;
    }
}
