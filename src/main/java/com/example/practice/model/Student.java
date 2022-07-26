package com.example.practice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.SwaggerDefinition;

@XmlRootElement(name = "Student")
public class Student {
    public static int count = 1;
    public int id;
    public String name;
    public String lastname;
    public String patronic;
    public int course;
    public String faculty;
    public Student(){}

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
