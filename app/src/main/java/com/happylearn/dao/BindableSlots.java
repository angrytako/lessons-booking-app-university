package com.happylearn.dao;

import androidx.databinding.ObservableField;

public class BindableSlots {
    ObservableField<String> course;
    ObservableField<Integer> time;			// 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public BindableSlots(Slot slot) {
        this.course = new ObservableField<>(slot.getCourse());
        this.time = new ObservableField<>(slot.getTime());
    }


    public ObservableField<String> getCourse() {
        return course;
    }

    public ObservableField<Integer> getTime() {
        return time;
    }

    public void setCourse(ObservableField<String> course) {
        this.course = course;
    }


    public void setTime(ObservableField<Integer> time) {
        this.time = time;
    }
}
