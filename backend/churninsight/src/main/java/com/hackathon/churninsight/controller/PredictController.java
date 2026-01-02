package com.hackathon.churninsight.controller;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;
import com.hackathon.churninsight.dto.response.SuccessResponseDTO;
import com.hackathon.churninsight.service.PredictService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/predict")
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<PredictResponseDTO>> predict(
            @Valid @RequestBody PredictRequestDTO request,
            HttpServletRequest httpRequest
    ) {
        PredictResponseDTO prediction = predictService.predict(request);

        SuccessResponseDTO<PredictResponseDTO> response = new SuccessResponseDTO<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Predicción generada correctamente",
                prediction,
                httpRequest.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }
}

