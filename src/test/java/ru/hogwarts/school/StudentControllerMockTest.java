package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerMockTest {

    @MockitoBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void addStudentTest() throws Exception {
        String name = "Test Name";
        Student inputStudent = new Student();
        inputStudent.setName(name);

        Student returnedStudent = new Student();
        returnedStudent.setId(1L);
        returnedStudent.setName(name);

        when(studentService.addStudent(any(Student.class))).thenReturn(returnedStudent);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(objectMapper.writeValueAsString(inputStudent))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void findByIdTest() throws Exception {
        Long id = 12L;
        String name = "Mae";

        Student student = new Student();
        student.setId(id);
        student.setName(name);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void editStudentTest() throws Exception {
        Long id = 1L;
        String updatedName = "Charlie";

        Student studentRequest = new Student();
        studentRequest.setId(id);
        studentRequest.setName(updatedName);

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setName(updatedName);

        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(objectMapper.writeValueAsString(studentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName));

    }

    @Test
    public void deleteStudentTest() throws Exception {
        Long id = 3L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(studentService).deleteById(id);
    }

    @Test
    public void findByAgeTest() throws Exception {
        int age = 17;

        Student student1 = new Student(57L, "Mae", 17);
        Student student2 = new Student(8L, "Rick", 15);

        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findByAge(age)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age")
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(57L))
                .andExpect(jsonPath("$[0].age").value(17))
                .andExpect(jsonPath("$[0].name").value("Mae"))
                .andExpect(jsonPath("$[1].id").value(8L))
                .andExpect(jsonPath("$[1].age").value(15))
                .andExpect(jsonPath("$[1].name").value("Rick"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getByAgeBetweenTest() throws Exception {
        int minAge = 11;
        int maxAge = 17;

        Student student1 = new Student(57L, "Mae", 17);
        Student student2 = new Student(8L, "Rick", 15);

        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findByAgeBetween(minAge, maxAge)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age-between")
                        .param("minAge", String.valueOf(minAge))
                        .param("maxAge", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(57L))
                .andExpect(jsonPath("$[0].age").value(17))
                .andExpect(jsonPath("$[0].name").value("Mae"))
                .andExpect(jsonPath("$[1].id").value(8L))
                .andExpect(jsonPath("$[1].age").value(15))
                .andExpect(jsonPath("$[1].name").value("Rick"))
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    public void testGetFacultyByStudentTest() throws Exception{

        Long studentId = 9L;
        Faculty faculty = new Faculty(19L, "Art", "pink");
        Student student = new Student(studentId, "Eric", 16);
        student.setFaculty(faculty);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}/faculty", studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(19L))
                .andExpect(jsonPath("$.name").value("Art"))
                .andExpect(jsonPath("$.color").value("pink"));

    }
}



