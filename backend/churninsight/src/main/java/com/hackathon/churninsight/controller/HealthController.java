package com.hackathon.churninsight.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para health checks y verificación del estado del servicio.
 * Proporciona endpoints para monitoreo y observabilidad.
 */
@RestController
@RequestMapping("/api/health")
@Slf4j
@Tag(
        name = "Health",
        description = "Monitoreo del estado del backend y sus dependencias"
)
public class HealthController {

    private final WebClient webClient;

    @Value("${ml.service.base-url}")
    private String mlServiceBaseUrl;

    public HealthController(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Health check básico del servicio.
     *
     * @return Estado del servicio con timestamp
     */
    @Operation(
            summary = "Health check básico",
            description = "Verifica si el backend está operativo"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Servicio operativo"
            )
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check solicitado");

        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "ChurnInsight Backend");
        health.put("version", "1.0.0");

        return ResponseEntity.ok(health);
    }

    /**
     * Health check detallado que verifica conectividad con el servicio ML.
     *
     * @return Estado detallado incluyendo verificación de dependencias
     */
    @Operation(
            summary = "Health check detallado",
            description = """
                Verifica el estado del backend y la conectividad
                con el servicio externo de Machine Learning.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Servicio operativo y dependencias disponibles"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Servicio operativo pero dependencia ML no disponible"
            )
    })
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        log.debug("Health check detallado solicitado");

        Map<String, Object> health = new HashMap<>();
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "ChurnInsight Backend");
        health.put("version", "1.0.0");

        // Verificar conectividad con servicio ML
        boolean mlServiceAvailable = checkMlServiceHealth();

        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put("ml-service", Map.of(
                "url", mlServiceBaseUrl,
                "status", mlServiceAvailable ? "UP" : "DOWN"));

        health.put("dependencies", dependencies);
        health.put("status", mlServiceAvailable ? "UP" : "DEGRADED");

        HttpStatus status = mlServiceAvailable ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(health);
    }

    /**
     * Verifica si el servicio ML está disponible.
     *
     * @return true si el servicio responde, false en caso contrario
     */
    private boolean checkMlServiceHealth() {
        try {
            webClient.get()
                    .uri(mlServiceBaseUrl + "/health")
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(2))
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Servicio ML no disponible: {}", e.getMessage());
            return false;
        }
    }
}
