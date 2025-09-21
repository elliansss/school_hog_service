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
import org.springframework.test.annotation.DirtiesContext;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

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



