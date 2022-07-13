package com.example.practice.model;

public class Student {
    public static int count = 1;
    public int id;
    public String name;
    public String lastname;
    public String patronic;
    public int course;
    public String faculty;

    private Student(String name, String lastname, String patronic, int course, String faculty) {
        this.id = count;
        this.count++;
        this.name = name;
        this.lastname = lastname;
        this.patronic = patronic;
        this.course = course;
        this.faculty = faculty;
    }

    public static Student createStudent(String name, String lastname, String patronic, int course, String faculty) {
        return new Student(name, lastname, patronic, course, faculty);
    }

    @Override
    public String toString() {
        return "\nname: " + name +
                "\nlastname: " + lastname +
                "\npatronic: " + patronic +
                "\ncourse: " + course +
                "\nfaculty: " + faculty;
    }

}
