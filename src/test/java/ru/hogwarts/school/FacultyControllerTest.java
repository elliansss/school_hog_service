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
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testAddFaculty() {
        Faculty faculty = new Faculty(null, "Art", "pink");
        Faculty result = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Art");
        Assertions.assertThat(result.getColor()).isEqualTo("pink");
    }

    @Test
    public void testFindById() {
        Faculty faculty = new Faculty(null, "Medicine", "red");
        faculty = facultyRepository.save(faculty);

        Faculty result = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Medicine");
        Assertions.assertThat(result.getColor()).isEqualTo("red");
    }

    @Test
    public void testEditFaculty() {
        Faculty faculty = new Faculty(null, "Law", "blue");
        Faculty created = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long id = created.getId();

        Faculty updatedFaculty = new Faculty(null, "Law Updated", "blue");
        updatedFaculty.setId(id);

        this.restTemplate.put("http://localhost:" + port + "/faculty", updatedFaculty);

        Faculty fetched = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + id, Faculty.class);
        Assertions.assertThat(fetched.getName()).isEqualTo("Law Updated");
    }

    @Test
    public void testDeleteById() {
        Faculty faculty = new Faculty(null, "Linguistics", "white");
        Faculty created = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long id = created.getId();

        this.restTemplate.delete("http://localhost:" + port + "/faculty/" + id);

        ResponseEntity<Faculty> response = this.restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, Faculty.class);
        Assertions.assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    public void testFindByColor() {
        Faculty faculty = new Faculty(null, "IT", "dark blue");
        this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty fetched = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/by-color?color=dark blue", Faculty.class);
        Assertions.assertThat(fetched).isNotNull();
        Assertions.assertThat(fetched.getColor()).isEqualTo("dark blue");
    }

    @Test
    public void testFindByNameOrColor() {
        Faculty faculty = new Faculty(null, "Biology", "yellow");
        facultyRepository.save(faculty);
        String query = "Biology";
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/search?query=" + query,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );
        List<Faculty> facultyList = response.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyList).isNotNull();
        assertThat(facultyList).isNotEmpty();
    }
}
