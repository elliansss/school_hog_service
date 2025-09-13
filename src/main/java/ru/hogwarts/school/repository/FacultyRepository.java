package ru.hogwarts.school.repository;

import ru.hogwarts.school.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByColor(String color);
    Faculty findByNameIgnoreCase(String name);
    Faculty findByColourIgnoreCase(String colour);
}