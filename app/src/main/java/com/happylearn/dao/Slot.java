package com.happylearn.dao;

import java.util.List;

public class Slot {
    private String course;
    private List<Docente> teacherList;
    private int day;			// 0: lunedì, 1: martedì, 2: mercoledì, 3: giovedì, 4: venerdì
    private int time;			// 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public Slot(String course, List <Docente> teacherList, int day, int time) {
        this.course = course;
        this.teacherList = teacherList;
        this.day = day;
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public List<Docente> getTeacherList() {
        return teacherList;
    }

    public void addTeacherToList(Docente d){
        teacherList.add(d);
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }


    @Override
    public String toString() {
        return "Slot{" +
                "course='" + course + '\'' +
                ", teacherList=" + teacherList +
                ", day=" + day +
                ", time=" + time +
                '}';
    }
}
