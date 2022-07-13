package com.example.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController {
    @ResponseBody
    @GetMapping("/students")
    public String getStudents(){
        
        return "student Victor";
    }
}
