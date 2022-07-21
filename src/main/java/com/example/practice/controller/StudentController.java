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
    private final String XMLList = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><List>";
    private final String XMLStudent = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Student>\"";

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
    @GetMapping("/all")
    public String listAll(@RequestHeader(value = "Accept") String[] headers){
        this.students=getStudents(-1);
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
        Student student=getStudentsOnId(SID, id);
        for(int i = 0 ; i < headers.length;i++){
            if(headers.equals("application/xml"))
                return getXMLStudent(student);
        }
        Gson gson=new Gson();
        String list=gson.toJson(student);
        return list;
    }

    public String getXML(){
        StringBuilder resultXml = new StringBuilder(XMLList);
        for(Student st : this.students){
            resultXml.append("<Student>")
            .append("<id>").append(st.id).append("</id>")
            .append("<name>").append(st.name).append("</name>")
            .append("<lastname>").append(st.lastname).append("</lastname>")
            .append("<patronic>").append(st.patronic).append("</patronic>")
            .append("<course>").append(st.course).append("</course>")
            .append("<faculty>").append(st.faculty).append("</faculty>")
            .append("</Student>");
        }
        resultXml.append("</List>");
        return resultXml.toString();
    }
    public String getXMLStudent(Student st){
        StringBuilder resultXml = new StringBuilder(XMLStudent);
        resultXml.append("<id>").append(st.id).append("</id>")
        .append("<name>").append(st.name).append("</name>")
        .append("<lastname>").append(st.lastname).append("</lastname>")
        .append("<patronic>").append(st.patronic).append("</patronic>")
        .append("<course>").append(st.course).append("</course>")
        .append("<faculty>").append(st.faculty).append("</faculty>")
        .append("</Student>");
        return resultXml.toString();
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

    public Student getStudentsOnId(int SID, int Id){
        ArrayList<Student> students=getStudents(SID);
        Student student=null;
        for(Student st: students){
            if(st.id == Id){
                student=st;
                break;
            }
        }
        return student;
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
        if(SID==-1)
            return students;
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
