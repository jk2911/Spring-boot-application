package com.example.practice.controller;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.bind.JAXB;

import org.springframework.stereotype.Controller;
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
    @GetMapping("/students")
    public String listStudents(@RequestHeader(value = "Accept") String[] headers,
    @RequestParam(name = "SID", required = true) int SID){
        this.students=getStudents(SID);
        for(int i = 0 ; i < headers.length;i++){
            if(headers.equals("application/xml"))
                return getXML();
        }
        Gson gson=new Gson();
        String list=gson.toJson(students);
        return list;
    }

    @ResponseBody
    @GetMapping("/faculty")
    public String listStudentsOnFaculty(@RequestHeader(value = "Accept") String[] headers,
    @RequestParam(name = "SID", required = true) int SID,
    @RequestParam(name = "faculty", required = false) String faculty){
        this.students=getStudentsOnFaculty(SID, faculty);
        for(int i = 0 ; i < headers.length;i++){
            if(headers.equals("application/xml"))
                return getXML();
        }
        Gson gson=new Gson();
        String list=gson.toJson(students);
        return list;
    }

    @ResponseBody
    @GetMapping("/id")
    public String studentOnID(@RequestHeader(value = "Accept") String[] headers,
    @RequestParam(name = "SID", required = true) int SID,
    @RequestParam(name = "id", required = false) int id){
        this.students=getStudentsOnId(SID, id);
        for(int i = 0 ; i < headers.length;i++){
            if(headers.equals("application/xml"))
                return getXML();
        }
        Gson gson=new Gson();
        String list=gson.toJson(students);
        return list;
    }

    public String getXML(){
        String list="";
        JAXB.marshal(students, list);
        return list;
    }

    public ArrayList<Student> getStudentsOnFaculty(int SID, String faculty){
        ArrayList<Student> students=getStudents(SID);
        ArrayList<Student> temp = new ArrayList<>();
        for(Student student: students){
            if(student.faculty.equals(faculty))
                temp.add(student);
        }
        return temp;
    }

    public ArrayList<Student> getStudentsOnId(int SID, int Id){
        ArrayList<Student> students=getStudents(SID);
        ArrayList<Student> temp=new ArrayList<Student>();
        for(Student student: students){
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
