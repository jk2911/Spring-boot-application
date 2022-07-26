package com.example.practice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.practice.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.XML;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class StudentController {
    String path = "students.json";
    ArrayList<Student> students = new ArrayList<Student>();
    private final String XMLList = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Students>";
    private final String XMLStudent = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Student>";

    @Operation(summary = "Список студентов", description = "Студенты по SID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Founded the students", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class))),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = Student.class))
            })
    })
    @ResponseBody
    @GetMapping("/{SID}")
    public String listStudents(HttpServletRequest request, HttpServletResponse response, 
            @PathVariable(value = "SID") int SID) throws IOException { 
        this.students = getStudents(SID);
        if (request.getHeader("Accept") == "")
            return "Accept null";
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"), ",");
        if (accept.hasMoreTokens() && accept.nextToken().equals("application/xml")){
            response.addHeader("Content-Type", "application/xml");
            response.flushBuffer();
            return getXML();
        }
        Gson gson = new Gson();
        String list = gson.toJson(students);
        response.addHeader("Content-Type", "application/json");
        response.flushBuffer();
        return list;
    }

    @Operation(summary = "Список студентов по факультету", description = "Список факультетов: FIT, IEF, TOV, FH, LID, PIM, HTIT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Founded the students", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class))),
                @Content(mediaType = "application/xml", schema = @Schema(implementation = Student.class))
        })
})
    @ResponseBody
    @GetMapping("/{SID}/faculty/{faculty}")
    public String listStudentsOnFaculty(@PathVariable(name = "SID") int SID,
            @PathVariable("faculty") String faculty, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("Accept") == "")
            return "Accept null";
        this.students = getStudentsOnFaculty(SID, faculty);
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"), ",");
        if (accept.hasMoreTokens() && accept.nextToken().equals("application/xml")){
            response.addHeader("Content-Type", "application/xml");
            response.flushBuffer();
            return getXML();
        }
        Gson gson = new Gson();
        String list = gson.toJson(students);
        return list;
    }

    @Hidden
    @ResponseBody
    @GetMapping("")
    public void index(HttpServletResponse res) throws IOException {
        res.sendRedirect("/swagger-ui.html");
        return;
    }

    @Operation(summary = "Поиск студента по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Founded the students", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class))),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = Student.class))
            })

    })
    @ResponseBody
    @GetMapping("/{SID}/id/{id}")
    public String studentOnID(@PathVariable(name = "SID") int SID,
            @RequestParam(name = "id", required = false) int Id,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("Accept") == ""){
            return "Accept null";
        }
        Student student = getStudentsOnId(SID, Id);
        StringTokenizer accept = new StringTokenizer(request.getHeader("Accept"), ",");
        if (accept.hasMoreTokens() && accept.nextToken().equals("application/xml")){
            response.addHeader("Content-Type", "application/xml");
            response.flushBuffer();
            return getXML();
        }
        Gson gson = new Gson();
        String list = gson.toJson(student);
        return list;
    }

    public String getXML() {
        StringBuilder resultXml = new StringBuilder(XMLList);
        for (Student st : this.students) {
            resultXml.append("<Student>")
                    .append("<id>").append(st.id).append("</id>")
                    .append("<name>").append(st.name).append("</name>")
                    .append("<lastname>").append(st.lastname).append("</lastname>")
                    .append("<patronic>").append(st.patronic).append("</patronic>")
                    .append("<course>").append(st.course).append("</course>")
                    .append("<faculty>").append(st.faculty).append("</faculty>")
                    .append("</Student>");
        }
        resultXml.append("</Students>");
        return resultXml.toString();
    }

    public String getXMLStudent(Student st) {
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

    public ArrayList<Student> getStudentsOnFaculty(int SID, String faculty) {
        ArrayList<Student> students = getStudents(SID);
        ArrayList<Student> temp = new ArrayList<>();
        for (Student student : students) {
            if (student.faculty.equals(faculty))
                temp.add(student);
        }
        return temp;
    }

    public Student getStudentsOnId(int SID, int Id) {
        ArrayList<Student> students = getStudents(SID);
        Student student = null;
        for (Student st : students) {
            if (st.id == Id) {
                student = st;
                break;
            }
        }
        return student;
    }

    public ArrayList<Student> getStudents(int SID) {
        Gson gson = new Gson();
        String json = "";

        try {
            json = new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            return null;
        }

        ArrayList<Student> students = gson.fromJson(json, new TypeToken<ArrayList<Student>>() {
        }.getType());
        ArrayList<Student> temp = new ArrayList<Student>();
        if (SID == -1)
            return students;
        int id = 0;
        for (Student student : students) {
            if (student.id % 31 == SID) {
                student.id = id;
                temp.add(student);
                id++;
            }
        }
        return temp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public String getXMLList() {
        return XMLList;
    }

    public String getXMLStudent() {
        return XMLStudent;
    }
}
