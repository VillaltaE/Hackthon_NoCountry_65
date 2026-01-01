package com.hackathon.churninsight.controller;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;
import com.hackathon.churninsight.service.PredictService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predict")
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @PostMapping
    public ResponseEntity<PredictResponseDTO> predict(
            @Valid @RequestBody PredictRequestDTO request
    ) {
        return ResponseEntity.ok(predictService.predict(request));
    }
}

