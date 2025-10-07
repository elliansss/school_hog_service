package ru.hogwarts.school.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Page<Avatar> getAvatarsPage(int page, int size) {
        return avatarRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Avatar> getAvatarsByStudentId(Long studentId, int page, int size) {
        return avatarRepository.findByStudentId(studentId, PageRequest.of(page, size));
    }
}