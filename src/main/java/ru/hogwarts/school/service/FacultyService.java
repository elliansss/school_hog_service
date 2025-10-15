package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }


    public List<Faculty> findByNameOrColorIgnoreCase(String name, String color) {
        logger.info("Was invoked method for find faculty be name or color (ignore case)");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Optional<Faculty> findFacultyById(Long id) {
        logger.info("Was invoked method for find faculty by ID");
        return facultyRepository.findById(id);
    }

    public Faculty findByColor(String color) {
        logger.info("Was invoked method for find faculty by color");
        return facultyRepository.findByColor(color).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method edit faculty");
        if (faculty == null || faculty.getId() == 0) {
            return null;
        }
        if (!facultyRepository.existsById(faculty.getId())) {
            logger.warn("Faculty with ID {} not found for editing", faculty.getId());
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        if (id == null || !facultyRepository.existsById(id)) {
            logger.info("Was invoked method for delete faculty");
            return;
        }
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByNameOrColor(String query) {
        logger.info("Was invoked method for find faculty be name or color");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(query, query);
    }
}


