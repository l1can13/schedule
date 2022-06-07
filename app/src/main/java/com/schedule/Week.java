package com.schedule;

import android.widget.Button;

import java.util.LinkedList;
import java.util.List;

public class Week {

    private Button button;
    private int numberOfWeek;
    private List<DayOfWeek> daysOfWeek;

    public Week() {
        this.daysOfWeek = new LinkedList<>();
    }

    public int getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void updateDaysOfWeek(DayOfWeek dayOfWeek) {
        this.daysOfWeek.add(dayOfWeek);
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
