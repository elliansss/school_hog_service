package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        System.out.println("15" + student);
        Student student1 = studentService.addStudent(student);
        System.out.println("12" + student1);
        return student1;

    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student updated = studentService.editStudent(student);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-age")
    public List<Student> findByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("/by-age-between")
    public List<Student> getByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {

        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudentCount() {
        Long count = studentService.getTotalStudentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Long> getAverageAge() {
        Long averageAge = studentService.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/the-last-five-students")
    public ResponseEntity<Long> getLastFiveStudents() {
        Long lastStudents = studentService.getLastFiveStudents();
        return ResponseEntity.ok(lastStudents);
    }
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }

        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }
}
