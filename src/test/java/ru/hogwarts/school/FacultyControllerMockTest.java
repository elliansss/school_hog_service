package ru.hogwarts.school;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacultyControllerMockTest {

    @MockitoBean
    private FacultyRepository facultyRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void addFacultyTest() throws Exception {
        String name = "Test faculty";
        Faculty inputFaculty = new Faculty();
        inputFaculty.setName(name);

        Faculty returnedFaculty = new Faculty();
        returnedFaculty.setId(1L);
        returnedFaculty.setName(name);

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(returnedFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(objectMapper.writeValueAsString(inputFaculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void findByIdTest() throws Exception {
        Long id = 12L;
        String name = "Test";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void editFacultyTest() throws Exception {
        Long id = 1L;
        String updatedName = "Sport";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(updatedName);

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(id);
        updatedFaculty.setName(updatedName);

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));

    }

    @Test
    public void deleteByIdTest() throws Exception {
        Long id = 3L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(facultyService).deleteFaculty(id);
    }


    @Test
    public void findByColorTest() throws Exception {
        String color = "Red";
        Faculty faculty = new Faculty(87L, "Biology", "Red");

        when(facultyRepository.findByColor(color)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by-color")
                        .param("color", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(87L))
                .andExpect(jsonPath("$.name").value("Biology"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void findByNameOrColorTest() throws Exception {
        String query = "Red";
        Faculty faculty1 = new Faculty(87L, "Biology", "Red");
        Faculty faculty2 = new Faculty(8L, "Sport", "Red");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);

        when(facultyService.findByNameOrColor(query)).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/search")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(87L))
                .andExpect(jsonPath("$[0].name").value("Biology"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[1].id").value(8L))
                .andExpect(jsonPath("$[1].name").value("Sport"))
                .andExpect(jsonPath("$[1].color").value("Red"))
                .andExpect(jsonPath("$.length()").value(2));

        verify(facultyService, times(1)).findByNameOrColor(query);
    }
}




