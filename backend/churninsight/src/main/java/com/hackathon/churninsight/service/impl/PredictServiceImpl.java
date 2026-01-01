package com.hackathon.churninsight.service.impl;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;
import com.hackathon.churninsight.service.PredictService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PredictServiceImpl implements PredictService {

    private final WebClient webClient;

    public PredictServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public PredictResponseDTO predict(PredictRequestDTO request) {
        return webClient.post()
                .uri("/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredictResponseDTO.class)
                .block();
    }
}
