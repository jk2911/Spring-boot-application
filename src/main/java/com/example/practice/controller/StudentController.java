package com.example.practice.controller;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.practice.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class StudentController {
    String path = "students.json";
    ArrayList<Student> students = new ArrayList<Student>();
    int SID;

    @ResponseBody
    @GetMapping("/")
    public String Students(/*@RequestHeader(value = "Accept", defaultValue = "") String accept,*/
        @RequestParam(name="SID",  required = true) int SID,
         @RequestParam(name="faculty", defaultValue = "", required = false) String faculty,
         @RequestParam(name="id", defaultValue = "-1", required = false) int Id,
         Model model){
        /*if(accept.compareTo("") == 0)
            return "Error";*/

        this.students=getStudents(SID);

        String message="<!DOCTYPE html>" +
        "<html lang=\"en\">"+
        "<head>"+
            "<meta charset=\"UTF-8\">"+
            "<title>Main page</title>"+
        "</head>"+
        "<body>" +
        "<p>";
        if(Id != -1){
            this.students = getStudentsOnId(Id);
        }
        else if(!faculty.equals(""))
            this.students = getStudentsOnFaculty(faculty);

        for(Student student: this.students){
            message += student + "</p><p>";
        }
        message += "</p></body></html>";
        return message;
    }

    public ArrayList<Student> getStudentsOnFaculty(String faculty){
        ArrayList<Student> temp=new ArrayList<Student>();
        for(Student student: students){
            if(student.faculty.equals(faculty))
                temp.add(student);
        }
        return temp;
    }

    public ArrayList<Student> getStudentsOnId(int Id){
        ArrayList<Student> temp=new ArrayList<Student>();
        for(Student student: this.students){
            if(student.id == Id){
                temp.add(student);
                break;
            }
        }
        return temp;
    }

    public ArrayList<Student> getStudents(int SID){
        Gson gson = new Gson();
        String json = "";

        try {
            json = new String(Files.readAllBytes(Paths.get(path)));
        } 
        catch (Exception e) {
            return null;
        }

        ArrayList<Student> students = gson.fromJson(json, new TypeToken<ArrayList<Student>>(){}.getType());
        ArrayList<Student> temp = new ArrayList<Student>();
        int id = 0;
        for(Student student: students){
            if(student.id % 10 == SID){
                student.id = id;
                temp.add(student);
                id++;
            }
        }
        return temp;
    }
}
