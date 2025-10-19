package ru.hogwarts.school.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
public class AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Page<Avatar> getAvatarsPage(int page, int size) {
        logger.info("Was invoked method for get avatar page");
        return avatarRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Avatar> getAvatarsByStudentId(Long studentId, int page, int size) {
        logger.info("Was invoked method for get avatar by ID");
        return avatarRepository.findByStudentId(studentId, PageRequest.of(page, size));
    }
}