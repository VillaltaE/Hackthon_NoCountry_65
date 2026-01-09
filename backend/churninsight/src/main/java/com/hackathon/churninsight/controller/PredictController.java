package com.hackathon.churninsight.controller;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;
import com.hackathon.churninsight.dto.response.SuccessResponseDTO;
import com.hackathon.churninsight.service.PredictService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/predict")
@Slf4j
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDTO<Object>> predict(
            @Valid @RequestBody PredictRequestDTO request,
            HttpServletRequest httpRequest) {

        log.info("Recibida solicitud de predicción para cliente: {}", request.customerId());

        PredictResponseDTO prediction = predictService.predict(request);

        String label = prediction.prediction().label();
        String prevision = label.equals("will_churn") ? "Va a cancelar" : "Va a continuar";

        // Armamos un "data" extendido (mantiene lo técnico + agrega lo humano)
        var data = new java.util.LinkedHashMap<String, Object>();
        data.put("customer_id", prediction.customerId());
        data.put("prediction", prediction.prediction());
        data.put("prevision", prevision);

        log.info("Predicción generada exitosamente para cliente: {} - Resultado: {}",
                request.customerId(), label);

        SuccessResponseDTO<Object> response = new SuccessResponseDTO<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Predicción generada correctamente",
                data,
                httpRequest.getRequestURI());

        return ResponseEntity.ok(response);
    }
}
