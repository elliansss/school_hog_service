package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }


    public Student editStudent(Student student) {
        if (student == null || student.getId() == 0) {
            return null;
        }
        if (!studentRepository.existsById(student.getId())) {
            return null;
        }
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        if (id == null || !studentRepository.existsById(id)) {
            return;
        }
        studentRepository.deleteById(id);
    }
}