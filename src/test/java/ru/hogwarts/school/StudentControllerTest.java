package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import static org.junit.jupiter.api.Assertions.*;


import java.net.URI;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private FacultyRepository facultyRepository;


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testAddStudent() {
        Student student = new Student(null, "Joseph", 17);
        Student result = this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(result.getName()).isEqualTo(student.getName());
        Assertions.assertThat(result.getAge()).isEqualTo(student.getAge());
        Assertions.assertThat(result.getId()).isNotNull();
    }

    @Test
    public void testFindById() {
        Student student = new Student(null, "Eric", 15);
        student = studentRepository.save(student);
        Long id = student.getId();

        Student result = this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(student.getId());
        Assertions.assertThat(result.getName()).isEqualTo(student.getName());
        Assertions.assertThat(result.getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void testEditStudent() {
        Student student = new Student(null, "Eric", 15);
        student = studentRepository.save(student);
        Student updatedStudent = new Student(student.getId(), "Joe Updated", 18);
        RequestEntity<Student> request = RequestEntity
                .put(URI.create("http://localhost:" + port + "/student"))
                .body(updatedStudent);
        ResponseEntity<Student> response = restTemplate.exchange(request, Student.class);
        Student result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Joe Updated");
        assertThat(result.getAge()).isEqualTo(18);
    }

    @Test
    public void testDeleteById() {
        Student student = new Student(null, "Eric", 15);
        student = studentRepository.save(student);
        Long id = student.getId();

        restTemplate.delete("http://localhost:" + port + "/" + id);

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/" + id, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void testFindByAge() {
        Student student = new Student(null, "Eric", 17);
        studentRepository.save(student);
        int age = 17;

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/by-age?age=" + age,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );
    }

    @Test
    public void testGetByAgeBetween() {
        Student student = new Student(null, "Eric", 17);
        studentRepository.save(student);

        int minAge = 16;
        int maxAge = 20;
        String url = "http://localhost:" + port + "/student/by-age-between?&minAge=" + minAge + "&maxAge=" + maxAge;

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = response.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }


    @Test
    public void testGetFacultyByStudent() {
        Faculty faculty = new Faculty(null, "Test F", "red");
        facultyRepository.save(faculty);

        Student student = new Student(null, "Eric", 17);
        student.setFaculty(faculty);
        studentRepository.save(student);
        Long id = student.getId();

        String url = "http://localhost:" + port + "/" + id + "/faculty";

        ResponseEntity<Faculty> response = restTemplate.getForEntity(url, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test F");
    }
}



