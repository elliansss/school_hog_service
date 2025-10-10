package ru.hogwarts.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar,Long> {
    Page<Avatar> findByStudentId(Long studentId, Pageable pageable);
    Optional<Avatar> findByStudentId(Long studentId);
}