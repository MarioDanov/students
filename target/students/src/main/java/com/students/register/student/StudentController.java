package com.students.register.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> readAllStudents() {
        return studentService.readStudents();
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        studentService.createStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") long studentId, @RequestBody Student student) {
        studentService.updateStudent(studentId, student);
    }

    @PutMapping(path = "{studentId}/partial")
    public void updateStudent(@PathVariable("studentId") long studentId,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) {
        studentService.updateStudent(studentId, name, email);
    }
}
