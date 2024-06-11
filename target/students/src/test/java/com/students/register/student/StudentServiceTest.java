package com.students.register.student;

import com.students.register.exception.StudentAlreadyExistsException;
import com.students.register.exception.StudentDoesNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void readStudentsShouldReturnAllStudents() {
        studentService.readStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void createStudentShouldSaveStudentWhenEmailDoesNotExist() {
        Student student = new Student("John Doe", "john.doe@example.com", LocalDate.of(2000, 1, 1));
        when(studentRepository.findStudentByEmail(student.getEmail())).thenReturn(Optional.empty());

        studentService.createStudent(student);

        verify(studentRepository).save(student);
    }

    @Test
    void createStudentShouldThrowExceptionWhenEmailExists() {
        Student student = new Student("John Doe", "john.doe@example.com", LocalDate.of(2000, 1, 1));
        when(studentRepository.findStudentByEmail(student.getEmail())).thenReturn(Optional.of(student));

        assertThrows(StudentAlreadyExistsException.class, () -> studentService.createStudent(student));

        verify(studentRepository, never()).save(student);
    }

    @Test
    void deleteStudentShouldDeleteStudentWhenIdExists() {
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(true);

        studentService.deleteStudent(studentId);

        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void deleteStudentShouldThrowExceptionWhenIdDoesNotExist() {
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(false);

        assertThrows(StudentDoesNotExistsException.class, () -> studentService.deleteStudent(studentId));

        verify(studentRepository, never()).deleteById(studentId);
    }

    @Test
    void updateStudentShouldUpdateStudentWhenIdExists() {
        Long studentId = 1L;
        Student student = new Student("John Doe", "john.doe@example.com", LocalDate.of(2000, 1, 1));
        when(studentRepository.existsById(studentId)).thenReturn(true);

        studentService.updateStudent(studentId, student);

        verify(studentRepository).updateStudentById(studentId, student.getName(), student.getEmail(), student.getDob());
    }

    @Test
    void updateStudentShouldThrowExceptionWhenIdDoesNotExist() {
        Long studentId = 1L;
        Student student = new Student("John Doe", "john.doe@example.com", LocalDate.of(2000, 1, 1));
        when(studentRepository.existsById(studentId)).thenReturn(false);

        assertThrows(StudentDoesNotExistsException.class, () -> studentService.updateStudent(studentId, student));

        verify(studentRepository, never()).updateStudentById(studentId, student.getName(), student.getEmail(), student.getDob());
    }

    @Test
    void updateStudentWithParamsShouldUpdateStudentWhenIdExists() {
        Long studentId = 1L;
        Student existingStudent = new Student("Jane Doe", "jane.doe@example.com", LocalDate.of(2000, 1, 1));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        studentService.updateStudent(studentId, "John Doe", "john.doe@example.com");

        assertEquals("John Doe", existingStudent.getName());
        assertEquals("john.doe@example.com", existingStudent.getEmail());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void updateStudentWithParamsShouldThrowExceptionWhenIdDoesNotExist() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentDoesNotExistsException.class, () -> studentService.updateStudent(studentId, "John Doe", "john.doe@example.com"));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateStudentWithParamsShouldThrowExceptionWhenEmailExists() {
        Long studentId = 1L;
        Student existingStudent = new Student("Jane Doe", "jane.doe@example.com", LocalDate.of(2000, 1, 1));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.findStudentByEmail("john.doe@example.com")).thenReturn(Optional.of(new Student()));

        assertThrows(StudentAlreadyExistsException.class, () -> studentService.updateStudent(studentId, "John Doe", "john.doe@example.com"));

        verify(studentRepository, never()).save(existingStudent);
    }
}
