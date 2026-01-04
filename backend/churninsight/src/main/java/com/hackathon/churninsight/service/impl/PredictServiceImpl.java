package com.hackathon.churninsight.service.impl;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;
import com.hackathon.churninsight.exception.ExternalServiceException;
import com.hackathon.churninsight.service.PredictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class PredictServiceImpl implements PredictService {

        private final WebClient webClient;

        @Value("${ml.service.timeout}")
        private int timeout;

        @Value("${ml.service.retry.max-attempts}")
        private int maxRetryAttempts;

        public PredictServiceImpl(WebClient webClient) {
                this.webClient = webClient;
        }

        @Override
        public PredictResponseDTO predict(PredictRequestDTO request) {
                log.info("Iniciando predicción para cliente: {}", request.customerId());
                log.debug("Features del cliente: subscription={}, watchHours={}, lastLoginDays={}",
                                request.features().subscriptionType(),
                                request.features().watchHours(),
                                request.features().lastLoginDays());

                try {
                        PredictResponseDTO response = webClient.post()
                                        .uri("/predict")
                                        .bodyValue(request)
                                        .retrieve()
                                        .onStatus(
                                                        HttpStatusCode::is4xxClientError,
                                                        clientResponse -> clientResponse.bodyToMono(String.class)
                                                                        .flatMap(body -> {
                                                                                log.error("Error 4xx del servicio ML para cliente {}: {}",
                                                                                                request.customerId(),
                                                                                                body);
                                                                                return Mono.error(
                                                                                                new ExternalServiceException(
                                                                                                                "Error en validación del servicio ML: "
                                                                                                                                + body));
                                                                        }))
                                        .onStatus(
                                                        HttpStatusCode::is5xxServerError,
                                                        clientResponse -> {
                                                                log.error("Error 5xx del servicio ML para cliente {}",
                                                                                request.customerId());
                                                                return Mono.error(new ExternalServiceException(
                                                                                "Servicio ML no disponible (Error del servidor)"));
                                                        })
                                        .bodyToMono(PredictResponseDTO.class)
                                        .timeout(Duration.ofMillis(timeout))
                                        .retry(maxRetryAttempts)
                                        .doOnError(error -> log.error(
                                                        "Error al predecir para cliente {}: {}",
                                                        request.customerId(),
                                                        error.getMessage()))
                                        .block();

                        if (response != null) {
                                log.info("Predicción exitosa para cliente {}: {} con probabilidad {}",
                                                request.customerId(),
                                                response.prediction().label(),
                                                response.prediction().probability());
                        } else {
                                log.warn("Respuesta nula recibida del servicio ML para cliente {}",
                                                request.customerId());
                                throw new ExternalServiceException("Respuesta vacía del servicio ML");
                        }

                        return response;

                } catch (ExternalServiceException e) {
                        // Re-lanzar excepciones personalizadas
                        throw e;
                } catch (Exception e) {
                        log.error("Fallo crítico en predicción para cliente {}: {}",
                                        request.customerId(), e.getMessage(), e);
                        throw new ExternalServiceException(
                                        "No se pudo obtener la predicción del servicio ML: " + e.getMessage(), e);
                }
        }
}
