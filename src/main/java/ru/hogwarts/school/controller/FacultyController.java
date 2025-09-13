package ru.hogwarts.school.controller;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> findById(@PathVariable Long id) {
        Optional<Faculty> facultyOpt = facultyService.findFacultyById(id);
        return facultyOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty updated = facultyService.editFaculty(faculty);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-color")
    public ResponseEntity<Faculty> findByColor(@RequestParam String color) {
        Faculty faculty = facultyService.findByColor(color);
        if (faculty != null) {
            return ResponseEntity.ok(faculty);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Faculty> findByNameOrColor(@RequestParam String query) {
        Faculty facultyByName = facultyService.findByNameIgnoreCase(query);
        if (facultyByName != null) {
            return ResponseEntity.ok(facultyByName);
        }
        Faculty facultyByColor = facultyService.findByColorIgnoreCase(query);
        if (facultyByColor != null) {
            return ResponseEntity.ok(facultyByColor);
        }
        return ResponseEntity.notFound().build();
    }
    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping("/{facultyId}/students")
    public ResponseEntity<List<Student>> getStudentsByFaculty(@PathVariable Long facultyId) {
        Optional<Faculty> facultyOpt = facultyRepository.findById(facultyId);
        if (facultyOpt.isPresent()) {
            List<Student> students = facultyOpt.get().getStudents();
            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}