package com.happylearn.dao;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

public class BindableSlots {
    ObservableField<String> course;
    ObservableArrayList<Docente> teacherList;
    ObservableField<Integer> day;           // 0: lunedì, 1: martedì, 2: mercoledì, 3: giovedì, 4: venerdì
    ObservableField<Integer> time;			// 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public BindableSlots(Slot slot) {
        this.course = new ObservableField<>(slot.getCourse());
        this.time = new ObservableField<>(slot.getTime());
        this.teacherList =  new ObservableArrayList<>();
        for(Docente  d : slot.getTeacherList()){
            this.teacherList.add(d);
        }
        this.day = new ObservableField<>(slot.getDay());
        this.time = new ObservableField<>(slot.getTime());
    }

    public void addTeacher (Docente d){
        teacherList.add(d);
    }

    public void deleteTeacher (Docente d){
        teacherList.remove(d);
    }

    public ObservableField<String> getCourse() {
        return course;
    }

    public ObservableArrayList<Docente> getTeacherList() {
        return teacherList;
    }

    public ObservableField<Integer> getDay() {
        return day;
    }

    public ObservableField<Integer> getTime() {
        return time;
    }

    public void setCourse(ObservableField<String> course) {
        this.course = course;
    }
    
    public void setDay(ObservableField<Integer> day) {
        this.day = day;
    }

    public void setTime(ObservableField<Integer> time) {
        this.time = time;
    }

    public Slot getSlot (){
        List<Docente> teacherList = new ArrayList(this.teacherList.size());
        for (int i=0; i<this.teacherList.size(); i++){
            teacherList.add(this.teacherList.get(i));
        }
        return new Slot(course.get(),teacherList, day.get(),time.get());
    }

}
