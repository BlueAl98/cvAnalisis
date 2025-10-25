package com.blue.cvAnalisis.controller;

import com.blue.cvAnalisis.model.ApiResponse;
import com.blue.cvAnalisis.model.EmailRequest;
import com.blue.cvAnalisis.service.AnalisisService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class Controller {

    private final AnalisisService analisisService;

    public Controller(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @PostMapping
    public ApiResponse<EmailRequest> analisisCv(
            @RequestParam("file") MultipartFile file
    ) {
        analisisService.analisisCv(file);
        return new ApiResponse<>(200, "CV analyzed successfully", null);
    }


}
