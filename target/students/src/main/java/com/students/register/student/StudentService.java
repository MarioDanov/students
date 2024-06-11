package com.students.register.student;

import com.students.register.exception.StudentAlreadyExistsException;
import com.students.register.exception.StudentDoesNotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> readStudents() {
        logger.info("Reading all students");
        return studentRepository.findAll();
    }

    public void createStudent(Student student) {
        logger.info("Creating student with email: {}", student.getEmail());
        if (studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
            logger.error("A student with email {} already exists", student.getEmail());
            throw new StudentAlreadyExistsException("A student with that email already exists");
        }
        studentRepository.save(student);
        logger.info("Student created with email: {}", student.getEmail());
    }

    public void deleteStudent(Long studentId) {
        logger.info("Deleting student with ID: {}", studentId);
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            logger.error("Student with ID {} does not exist", studentId);
            throw new StudentDoesNotExistsException("Student with id " + studentId + " does not exist!");
        }
        studentRepository.deleteById(studentId);
        logger.info("Student with ID {} deleted", studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, Student student) {
        logger.info("Updating student with ID: {}", studentId);
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            logger.error("Student with ID {} does not exist", studentId);
            throw new StudentDoesNotExistsException("Student with id " + studentId + " does not exist!");
        }
        studentRepository.updateStudentById(studentId, student.getName(), student.getEmail(), student.getDob());
        logger.info("Student with ID {} updated", studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        logger.info("Updating student with ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentDoesNotExistsException("Student with id " + studentId + " does not exist!"));

        if (name != null && !name.isEmpty() && !Objects.equals(student.getName(), name)) {
            student.setName(name);
            logger.info("Updated name for student with ID: {}", studentId);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(student.getEmail(), email)) {
            if (studentRepository.findStudentByEmail(email).isPresent()) {
                logger.error("Student with email {} already exists", email);
                throw new StudentAlreadyExistsException("Student with email " + email + " already exists!");
            }
            student.setEmail(email);
            logger.info("Updated email for student with ID: {}", studentId);
        }
    }
}
