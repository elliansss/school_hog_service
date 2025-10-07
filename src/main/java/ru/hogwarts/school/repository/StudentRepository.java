package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int min, int max);

    @Query("SELECT COUNT(s) FROM Student s")
    Long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Long countAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    Long findTheLastFiveStudents();
}

