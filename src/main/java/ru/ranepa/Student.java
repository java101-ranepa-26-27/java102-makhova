package ru.ranepa;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Student {
    String name;
    int course;
    List<Integer> marks;

    //Alt + insert
    public Student(String name, int course, List<Integer> marks) {
        this.name = name;
        this.course = course;
        this.marks = marks;
    }

    public Student(String name, int course) {
        this.name = name;
        this.course = course;
    }

    public Student() {
    }

    public double averageMarks() {
        int sum = 0;
        for (Integer mark : marks) {
            sum+=mark;
        }
return (double)  sum / marks.size();
        }

    public String getStatus() {
        double avg = averageMarks();
        if (avg >= 4.5) return "отличник";
        if (avg >= 3.5) return "хорошист";
        if (avg >= 2.5) return "троечник";
        return "двоечник";
    }


