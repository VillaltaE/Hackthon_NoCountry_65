package com.hackathon.churninsight.controller;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;
import com.hackathon.churninsight.dto.response.SuccessResponseDTO;
import com.hackathon.churninsight.service.PredictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(
        name = "Prediction",
        description = "Predicción de churn de clientes usando un modelo de Machine Learning"
)
@RestController
@RequestMapping("/api/predict")
@Slf4j
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @Operation(
            summary = "Generar predicción de churn",
            description = """
                Recibe los datos del cliente y retorna la predicción de churn
                generada por el modelo de Machine Learning, incluyendo la
                probabilidad y una interpretación legible.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Predicción generada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida (error de validación o enums inválidos)"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Servicio de Machine Learning no disponible"
            )
    })
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
