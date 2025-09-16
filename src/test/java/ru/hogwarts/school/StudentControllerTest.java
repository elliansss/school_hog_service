package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testAddStudent() {
        Student student = new Student(124643L, "Joseph", 17);
        Student result = this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(result).isEqualTo(student);
    }

    @Test
    public void testFindById() {
        long id = 124545L;
        Student student = this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class);
        Assertions.assertThat(student).isNotNull();
        Assertions.assertThat(student.getId()).isEqualTo(id);
    }

    @Test
    public void testEditStudent() {
        Student updatedStudent = new Student(14343L, "Joe Updated", 18);
        RequestEntity<Student> request = RequestEntity
                .put(URI.create("http://localhost:" + port + "/student"))
                .body(updatedStudent);
        Student result = restTemplate.exchange(request, Student.class).getBody();
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Joe Updated");
        assertThat(result.getAge()).isEqualTo(18);
    }

    @Test
    public void testDeleteById() {
        Long id = 19656L;
        restTemplate.delete("http://localhost:" + port + "/{id}" + id);
        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/{id}" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testFindByAge() {
        int age = 17;
        List<Student> students = restTemplate.getForObject("http://localhost:" + port + "/by-age" + age, List.class);
        assertThat(students).isNotNull();
    }

    @Test
    public void testGetByAgeBetween() {
        int minAge = 16;
        int maxAge = 20;
        List<Student> students = restTemplate.getForObject("http://localhost:" + port + "/by-age-between" + minAge + maxAge, List.class);
        assertThat(students).isNotNull();
    }

    @Test
    public void testGetFacultyByStudent() {
        Long id = 12879L;
        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/{studentId}/faculty", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

    }
    }



