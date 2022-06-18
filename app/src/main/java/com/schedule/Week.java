package com.schedule;

import java.util.LinkedList;
import java.util.List;

public class Week {

    private List<Day> days;

    public Week() {
        days = new LinkedList<>();
    }

    public Week(List<Day> days) {
        this.days = days;
    }

    public Day findDay(String dayName) {
        for (Day day : days) {
            if (day.getName().equals(dayName)) return day;
        }

        return null;
    }

    public boolean isExist(String dayName) {
        if (days != null) {
            for (Day day : days) {
                if (day.getName().equals(dayName)) return true;
            }
        }

        return false;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public void update(List<Day> days) {
        setDays(days);
    }

    public void addDay(Day day) {
        this.days.add(day);
    }

    public List<Day> getDays() {
        return days;
    }
}
