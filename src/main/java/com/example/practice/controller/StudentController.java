package com.example.practice.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.practice.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;




@Controller
public class StudentController {
    String path = "students.json";
    ArrayList<Student> students = new ArrayList<Student>();
    private final String XMLList = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><List>";
    private final String XMLStudent = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Student>";

    @ResponseBody
    @GetMapping("/{SID}/students")
    public String listStudents(HttpServletRequest request,
    @PathVariable("SID") int SID){
        this.students=getStudents(SID);
        if(request.getHeader("Accept") == "")
            return "Accept null";
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"),",");
        if(accept.hasMoreTokens() && accept.nextToken().equals("application/xml"))
            return getXML();
        Gson gson=new Gson();
        String list=gson.toJson(students);
        return list;
    }
    @ResponseBody
    @GetMapping("/all")
    public String listAll(HttpServletRequest request){
        if(request.getHeader("Accept") == "")
            return "Accept null";
        this.students=getStudents(-1);
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"),",");
        if(accept.hasMoreTokens() && accept.nextToken().equals("application/xml"))
            return getXML();
        Gson gson=new Gson();
        String list=gson.toJson(students);
        return list;
    }

    @ResponseBody
    @GetMapping("/{SID}/faculty/{faculty}")
    public String listStudentsOnFaculty(@PathVariable( name = "SID") int SID,
    @PathVariable("faculty") String faculty, HttpServletRequest request){
        if(request.getHeader("Accept") == "")
            return "Accept null";
        this.students=getStudentsOnFaculty(SID, faculty);
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"),",");
        if(accept.hasMoreTokens() && accept.nextToken().equals("application/xml"))
            return getXML();
        Gson gson=new Gson();
        String list=gson.toJson(students);
        return list;
    }

    @ResponseBody
    @GetMapping("")
    public String index(HttpServletResponse res ) throws IOException{
        res.sendRedirect("/swagger-ui.html");
        return null;
    }

    @ResponseBody
    @GetMapping("/{SID}/id")
    public String studentOnID(@PathVariable(name = "SID") int SID,
    @RequestParam(name = "id", required = false) int Id,
    HttpServletRequest request){
        if(request.getHeader("Accept") == "")
            return "Accept null";
        int id;
        try{
            id = Integer.parseInt(request.getParameter("id"));
        }
        catch(Exception e) {
            return "Bad Request 400";
        }
        Student student=getStudentsOnId(SID, id);
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"),",");
        if(accept.hasMoreTokens() && accept.nextToken().equals("application/xml"))
            return getXMLStudent(student);
        Gson gson = new Gson();
        String list = gson.toJson(student);
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
        if(SID == -1)
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
