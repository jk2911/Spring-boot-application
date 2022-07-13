package com.example.practice.controller;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.practice.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class StudentController {
    String path="C:/Users/Asus/Desktop/ISiT/Spring-boot-application/src/main/resources/students.json";
    @ResponseBody
    @GetMapping("/students")
    public String Students(@RequestParam(name="SID", defaultValue = "-1", required = false) int SID,
         @RequestParam(name="faculty", defaultValue = "", required = false) String faculty,
         @RequestParam(name="ID", defaultValue = "-1", required = false) int Id,
         Model model){
        
        String message="<!DOCTYPE html>" +
        "<html lang=\"en\">"+
        "<head>"+
            "<meta charset=\"UTF-8\">"+
            "<title>Main page</title>"+
        "</head>"+
        "<body>" +
        "<p>";
        ArrayList<Student> students = new ArrayList<Student>();
        if(SID != -1)
            students = getStudentsOnSID(SID);

        else if(Id != -1){
            students = getStudentsOnId(Id);
        }
        else if(!faculty.equals(""))
            students = getStudentsOnFaculty(faculty);
        else
            return "Error";

        for(Student student: students){
            message += student + "</p><p>";
        }
        message += "</p></body></html>";
        return message;
    }

    public ArrayList<Student> getStudentsOnFaculty(String faculty){
        ArrayList<Student> students = getStudents();
        ArrayList<Student> st = new ArrayList<Student>();
        for(Student student: students){
            if(student.faculty.equals(faculty))
                st.add(student);
        }
        return st;
    }

    public ArrayList<Student> getStudentsOnId(int Id){
        ArrayList<Student> students = getStudents();
        ArrayList<Student> st = new ArrayList<Student>();
        for(Student student: students){
            if(student.id == Id){
                st.add(student);
                break;
            }
        }
        return st;
    }

    public ArrayList<Student> getStudentsOnSID(int SID){
        ArrayList<Student> students = getStudents();
        ArrayList<Student> st = new ArrayList<Student>();
        for(Student student: students){
            if(student.id % 10 == SID)
                st.add(student);
        }
        return st;
    }

    public ArrayList<Student> getStudents(){
        Gson gson = new Gson();
        String json = "";

        try {
            json = new String(Files.readAllBytes(Paths.get(path)));
        } 
        catch (Exception e) {
            return null;
        }

        ArrayList<Student> students = gson.fromJson(json, new TypeToken<ArrayList<Student>>(){}.getType());
        return students;
    }
}
