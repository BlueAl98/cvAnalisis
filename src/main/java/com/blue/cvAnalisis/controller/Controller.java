package com.blue.cvAnalisis.controller;

import com.blue.cvAnalisis.service.AnalisisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api")
public class Controller {

    private final AnalisisService analisisService;
    private final Semaphore semaphore;

    @Value("${spring.parameters.max_Simultaneos_Request}")
    private Integer maxRequests;

    public Controller(AnalisisService analisisService, @Value("${spring.parameters.max_Simultaneos_Request}") Integer maxRequests) {
        this.analisisService = analisisService;
        this.semaphore = new Semaphore(maxRequests);
    }

    @PostMapping
    public ResponseEntity<?> analisisCvCon(@RequestParam("file") MultipartFile file,
                                           @RequestParam("data") String email,
                                           @RequestParam("targetProfile") String targetProfile
        ) {
        boolean acquired = semaphore.tryAcquire(); // try to get a permit without blocking
        if (!acquired) {
            // Too many concurrent requests
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Server is busy. Please try again later."));
        }
        try {
            // Process the request
            return analisisService.analisisCv(file, email, targetProfile);
        } finally {
            semaphore.release(); // Always release permit
        }
    }


}
