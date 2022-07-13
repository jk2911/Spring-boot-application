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
    @ResponseBody
    @GetMapping("/students")
    public String getStudents(@RequestParam(name="SID", defaultValue = "0", required = false) int SID, Model model){
        String path="C:/Users/Asus/Desktop/ISiT/Spring-boot-application/src/main/resources/students.json";
        String message="";
        message+=SID+"\n";
        String json;
        Gson gson=new Gson();

        try{
            json = new String(Files.readAllBytes(Paths.get(path)));
        }
        catch (Exception e){
            return "Error read file";
        }
        ArrayList<Student> students=gson.fromJson(json, new TypeToken<ArrayList<Student>>(){}.getType());
        
        for(Student student : students){
            if(student.id % 10 == SID)
                message += student;
            //if(name=)
        }
        return message;
    }
}
