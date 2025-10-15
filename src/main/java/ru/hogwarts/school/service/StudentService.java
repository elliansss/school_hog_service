package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for add student");
        return studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        logger.info("Was invoked method for find student by id");
        return studentRepository.findById(id);
    }

    public List<Student> findByAge(int age) {
        logger.info("Was invoked method for find student by age");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find student by age between");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method edit student");
        if (student == null || student.getId() == 0) {
            return null;
        }
        if (!studentRepository.existsById(student.getId())) {
            logger.warn("Student with ID {} not found for editing", student.getId());
            return null;
        }
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        if (id == null || !studentRepository.existsById(id)) {
            logger.info("Was invoked method for delete student");
            return;
        }
        studentRepository.deleteById(id);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Getting faculty for student ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                        logger.error("Student not found with ID: {}", studentId);
                        return new RuntimeException("Student not found: " + studentId);
    });

        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.warn("Student with ID {} has no faculty", studentId);
            throw new RuntimeException("Student with id " + studentId + " has no faculty");
        }
        logger.info("Get faculty '{}' for student ID: {}", faculty.getName(), studentId);
        return faculty;
    }

    public Long getTotalStudentCount() {
        logger.info("Was invoked method for get total student count");
        return studentRepository.countAllStudents();
    }

    public Long getAverageAge() {
        logger.info("Was invoked method for count average age of students");
        return studentRepository.countAverageAge();
    }

    public Long getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findTheLastFiveStudents();
    }


    public Avatar findAvatar(long studentId) {
        logger.info("Was invoked method for get avatar for student ID: {}", studentId);
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found for student ID: {}", studentId);
                    return new RuntimeException("Avatar not found: " + studentId);
                });
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        logger.info("Was invoked method for get extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}