package ru.hogwarts.school.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping
    public Page<Avatar> getAllAvatars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return avatarService.getAvatarsPage(page, size);
    }

    @GetMapping("/student/{id}")
    public Page<Avatar> getAvatarsByStudent(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return avatarService.getAvatarsByStudentId(id, page, size);
    }
}