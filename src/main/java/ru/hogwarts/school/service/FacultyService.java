package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findFacultyById(Long id) {
        return facultyRepository.findFacultyById(id);
    }


    public Faculty editFaculty(Faculty faculty) {
        if (faculty == null || faculty.getId() == 0) {
            return null;
        }
        if (!facultyRepository.existsById(faculty.getId())) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        facultyRepository.deleteById(id);
    }
}
