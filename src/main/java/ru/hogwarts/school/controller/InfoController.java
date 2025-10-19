package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.StudentService;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/info")

public class InfoController {

    Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port")
    public String getPort() {
        return serverPort;

    }

    @GetMapping("/sum-parallel")
    public int getSumParallel() {
        logger.info("Was invoked method calculateSumParallel");
        return IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
    }
}