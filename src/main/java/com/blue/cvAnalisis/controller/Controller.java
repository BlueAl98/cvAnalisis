package com.blue.cvAnalisis.controller;

import com.blue.cvAnalisis.model.ApiResponse;
import com.blue.cvAnalisis.model.CvAnalysisResult;
import com.blue.cvAnalisis.service.AnalisisService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CvAnalysisResult>> analisisCvCon(
            @RequestParam("file") MultipartFile file
    ) {
        return  analisisService.analisisCv(file);
    }


}
